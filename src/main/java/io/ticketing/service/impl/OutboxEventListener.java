package io.ticketing.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import io.ticketing.dto.Idempotency.ProcessedEventEntity;
import io.ticketing.config.KafkaConfig.OutboxTopicProps;
import io.ticketing.config.ReactiveTx;
import io.ticketing.dto.debezium.DebeziumEnvelope;
import io.ticketing.dto.debezium.OutboxEventRow;
import io.ticketing.repository.ProcessedEventRepository;
import io.ticketing.util.SharedUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.OffsetDateTime;
import java.util.Map;

@Component
public class OutboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventListener.class);

    private static final TypeReference<DebeziumEnvelope<OutboxEventRow>> ENVELOPE_TYPE =
            new TypeReference<>() {
            };

    private static final TypeReference<Map<String, Object>> MAP_TYPE =
            new TypeReference<>() {
            };

    private final SharedUtils sharedUtils;
    private final ProcessedEventRepository processedEventRepo;
    private final ReactiveTx reactiveTx;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxTopicProps topicProps;

    public OutboxEventListener(SharedUtils sharedUtils,
                               ProcessedEventRepository processedEventRepo,
                               ReactiveTx reactiveTx,
                               KafkaTemplate<String, String> kafkaTemplate,
                               OutboxTopicProps topicProps) {
        this.sharedUtils = sharedUtils;
        this.processedEventRepo = processedEventRepo;
        this.reactiveTx = reactiveTx;
        this.kafkaTemplate = kafkaTemplate;
        this.topicProps = topicProps;
    }

    @KafkaListener(
            id = "outbox-listener",
            topics = "${spring.kafka.topics.outbox-events:cdc.ticketing.outbox_event}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String rawValue = record.value();

        if (rawValue == null) {
            ack.acknowledge();
            return;
        }

        processRecord(rawValue, record)
                .doOnSuccess(v -> {
                    log.debug("Acknowledged offset={} partition={}", record.offset(), record.partition());
                    ack.acknowledge();
                })
                .doOnError(ex -> {
                    log.error("Processing failed for offset={} partition={}, forwarding to DLT",
                            record.offset(), record.partition(), ex);
                    sendToDlt(record);
                    ack.acknowledge();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Mono<Void> processRecord(String rawJson, ConsumerRecord<String, String> record) {

        return Mono.fromCallable(() -> parseEnvelope(rawJson))
                .flatMap(envelope -> {
                    if (!envelope.isCreateOrSnapshot() || envelope.getAfter() == null) {
                        log.debug("Skipping non-create op={} offset={}", envelope.getOp(), record.offset());
                        return Mono.empty();
                    }

                    OutboxEventRow row = envelope.getAfter();
                    String eventId = row.getId();

                    return processedEventRepo.existsById(eventId)
                            .flatMap(exists -> {
                                if (exists) {
                                    log.debug("Duplicate event id={}, skipping", eventId);
                                    return Mono.empty();
                                }
                                return reactiveTx.required(() ->
                                        dispatchBusinessEvent(row)
                                                .then(markProcessed(eventId))
                                );
                            });
                });
    }


    private DebeziumEnvelope<OutboxEventRow> parseEnvelope(String rawJson) {
        return sharedUtils.fromJsonToObject(rawJson, ENVELOPE_TYPE);
    }

    private Map<String, Object> parsePayload(String payloadJson) {
        if (this.sharedUtils.isNullOrEmptyOrBlank(payloadJson)) {
            return Map.of();
        }
        return sharedUtils.fromJsonToObject(payloadJson, MAP_TYPE);
    }

    private Mono<Void> dispatchBusinessEvent(OutboxEventRow row) {
        Map<String, Object> payload = parsePayload(row.getPayload());

        log.info("Processing outbox event id={} aggregateType={} type={} aggregateId={}",
                row.getId(), row.getAggregateType(), row.getType(), row.getAggregateId());

        return switch (row.getAggregateType()) {
            case "Product" -> handleProductEvent(row, payload);
            case "Order" -> handleOrderEvent(row, payload);
            default -> Mono.error(new IllegalArgumentException("Unknown aggregateType=" + row.getAggregateType()));
        };
    }

    private Mono<Void> handleProductEvent(OutboxEventRow row, Map<String, Object> payload) {
        log.info("Product event: type={} payload={}", row.getType(), payload);
        return Mono.empty();
    }

    private Mono<Void> handleOrderEvent(OutboxEventRow row, Map<String, Object> payload) {
        log.info("Order event: type={} payload={}", row.getType(), payload);
        return Mono.empty();
    }

    private Mono<Void> markProcessed(String eventId) {
        return processedEventRepo
                .save(new ProcessedEventEntity(eventId, OffsetDateTime.now()))
                .then();
    }

    private void sendToDlt(ConsumerRecord<String, String> record) {
        try {
            kafkaTemplate.send(
                    topicProps.outboxEventsDltTopic(),
                    record.key(),
                    record.value()
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send to DLT topic={} key={}",
                            topicProps.outboxEventsDltTopic(), record.key(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Exception sending to DLT", e);
        }
    }
}

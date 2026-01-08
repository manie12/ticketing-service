package io.ticketing.repository;

import io.ticketing.model.OutboxEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface OutboxEventRepository extends ReactiveCrudRepository<OutboxEvent, UUID> {

    Flux<OutboxEvent> findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(String aggregateType, String aggregateId);

    Flux<OutboxEvent> findByEventTypeOrderByOccurredAtAsc(String eventType);
}
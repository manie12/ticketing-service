package io.ticketing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ticketing.model.OutboxEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class OutboxEventFactory {

    private OutboxEventFactory() {
    }

    public static OutboxEvent create(
            ObjectMapper objectMapper,
            String aggregateType,
            String aggregateId,
            String eventType,
            int eventVersion,
            Object payloadObject,
            String correlationId
    ) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payloadObject);
            return new OutboxEvent(
                    UUID.randomUUID(),
                    aggregateType,
                    aggregateId,
                    eventType,
                    eventVersion,
                    payloadJson,
                    correlationId,
                    OffsetDateTime.now()
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize outbox payload to JSON", e);
        }
    }
}
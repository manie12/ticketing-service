package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("outbox_event")
public class OutboxEvent {

    @Id
    @Column("id")
    private UUID id;

    @Column("aggregate_type")
    private String aggregateType;     // ORDER, TICKET, PRODUCT

    @Column("aggregate_id")
    private String aggregateId;       // entity id (often UUID string)

    @Column("event_type")
    private String eventType;         // ORDER_CREATED, ORDER_STATUS_CHANGED

    @Column("event_version")
    private Integer eventVersion;     // start at 1

    /**
     * Store JSON as String. In Postgres you can keep the column as JSONB.
     * You serialize/deserialize with Jackson in your service code.
     */
    @Column("payload")
    private String payload;

    @Column("correlation_id")
    private String correlationId;     // requestId / trace correlation

    @Column("occurred_at")
    private OffsetDateTime occurredAt;

    public OutboxEvent() {
    }

    public OutboxEvent(UUID id,
                       String aggregateType,
                       String aggregateId,
                       String eventType,
                       Integer eventVersion,
                       String payload,
                       String correlationId,
                       OffsetDateTime occurredAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.payload = payload;
        this.correlationId = correlationId;
        this.occurredAt = occurredAt;
    }

    // Getters/setters (R2DBC uses them or reflection depending on setup)

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
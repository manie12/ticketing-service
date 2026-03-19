package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("outbox_event")
public class OutboxEvent implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

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

    @Transient
    private boolean isNew = false;

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
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public OutboxEvent markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() {
        return id;
    }

    public OutboxEvent setId(UUID id) {
        this.id = id;
        return this;
    }
    public UUID getTenantId() {
        return tenantId;
    }

    public OutboxEvent setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    public String getAggregateType() {
        return aggregateType;
    }

    public OutboxEvent setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
        return this;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public OutboxEvent setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public OutboxEvent setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public OutboxEvent setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxEvent setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public OutboxEvent setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public OutboxEvent setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

}
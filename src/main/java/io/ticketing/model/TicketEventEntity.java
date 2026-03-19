package io.ticketing.model;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.ticket_events
 * <p>
 * DDL:
 * ticket_events(
 * id uuid PK,
 * tenant_id uuid NOT NULL FK tenants(id) ON DELETE RESTRICT,
 * ticket_id uuid NOT NULL FK tickets(id) ON DELETE CASCADE,
 * event_type varchar(60) NOT NULL,
 * event_category varchar(30) NULL,
 * from_status varchar(40) NULL,
 * to_status varchar(40) NULL,
 * actor_type varchar(20) NOT NULL check in (CUSTOMER,AGENT,SYSTEM),
 * actor_id uuid NULL,
 * meta jsonb NULL,
 * occurred_at timestamptz NOT NULL default now(),
 * created_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "ticket_events")
public class TicketEventEntity implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("event_type")
    private String eventType;

    @Column("event_category")
    private String eventCategory; // nullable

    @Column("from_status")
    private String fromStatus;    // nullable

    @Column("to_status")
    private String toStatus;      // nullable

    @Column("actor_type")
    private String actorType;     // CUSTOMER | AGENT | SYSTEM

    @Column("customer_email")
    private String customerEmail;         // nullable

    /**
     * jsonb stored as JSON string for simplicity.
     * If you want JsonNode mapping, add converters later.
     */
    @Column("meta")
    private String meta;

    @Column("occurred_at")
    private OffsetDateTime occurredAt;

    @Column("created_at")
    private OffsetDateTime createdAt;
    @Transient
    private boolean isNew = false;
    public TicketEventEntity() {
    }

    public TicketEventEntity(UUID id, UUID tenantId, UUID ticketId, String eventType, String eventCategory,
                             String fromStatus, String toStatus, String actorType, String customerEmail,
                             String meta, OffsetDateTime occurredAt, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.eventType = eventType;
        this.eventCategory = eventCategory;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.actorType = actorType;
        this.customerEmail = customerEmail;
        this.meta = meta;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
    }

    public static TicketEventEntity newEvent(UUID tenantId,
                                             UUID ticketId,
                                             String eventType,
                                             String eventCategory,
                                             String fromStatus,
                                             String toStatus,
                                             String actorType,
                                             String customerEmail,
                                             String metaJson) {
        TicketEventEntity e = new TicketEventEntity();
        e.id = UUID.randomUUID();
        e.tenantId = tenantId;
        e.ticketId = ticketId;
        e.eventType = eventType;
        e.eventCategory = eventCategory;
        e.fromStatus = fromStatus;
        e.toStatus = toStatus;
        e.actorType = actorType;
        e.customerEmail = customerEmail;
        e.meta = metaJson;
        e.occurredAt = OffsetDateTime.now();
        e.createdAt = OffsetDateTime.now();
        return e;
    }

    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public TicketEventEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() {
        return id;
    }

    public TicketEventEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public TicketEventEntity setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public TicketEventEntity setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public TicketEventEntity setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public TicketEventEntity setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
        return this;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public TicketEventEntity setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
        return this;
    }

    public String getToStatus() {
        return toStatus;
    }

    public TicketEventEntity setToStatus(String toStatus) {
        this.toStatus = toStatus;
        return this;
    }

    public String getActorType() {
        return actorType;
    }

    public TicketEventEntity setActorType(String actorType) {
        this.actorType = actorType;
        return this;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public TicketEventEntity setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public String getMeta() {
        return meta;
    }

    public TicketEventEntity setMeta(String meta) {
        this.meta = meta;
        return this;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public TicketEventEntity setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TicketEventEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
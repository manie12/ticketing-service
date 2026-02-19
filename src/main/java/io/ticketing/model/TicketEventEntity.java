package io.ticketing.model;

// ============================================================
// TICKET EVENTS (AUDIT)
// payload is jsonb -> keep as String for simplicity.
// If you want strong typing, add converters (JsonNode) later.
// ============================================================
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
@Table("ticket_events")
class TicketEventEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("public_id")
    private String publicId;

    @Column("event_type")
    private String eventType;

    @Column("actor_type")
    private String actorType;  // CUSTOMER | AGENT | SYSTEM

    @Column("actor_ref")
    private String actorRef;

    @Column("payload")
    private String payloadJson; // jsonb stored as json string

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketEventEntity() {}

    public TicketEventEntity(UUID id, UUID tenantId, UUID ticketId, String publicId, String eventType,
                             String actorType, String actorRef, String payloadJson, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.publicId = publicId;
        this.eventType = eventType;
        this.actorType = actorType;
        this.actorRef = actorRef;
        this.payloadJson = payloadJson;
        this.createdAt = createdAt;
    }

    public static TicketEventEntity newEvent(UUID tenantId, UUID ticketId, String publicId,
                                             String eventType, String actorType, String actorRef, String payloadJson) {
        TicketEventEntity e = new TicketEventEntity();
        e.id = UUID.randomUUID();
        e.tenantId = tenantId;
        e.ticketId = ticketId;
        e.publicId = publicId;
        e.eventType = eventType;
        e.actorType = actorType;
        e.actorRef = actorRef;
        e.payloadJson = payloadJson == null ? "{}" : payloadJson;
        return e;
    }

    public UUID getId() { return id; }
    public TicketEventEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketEventEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketEventEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public String getPublicId() { return publicId; }
    public TicketEventEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getEventType() { return eventType; }
    public TicketEventEntity setEventType(String eventType) { this.eventType = eventType; return this; }

    public String getActorType() { return actorType; }
    public TicketEventEntity setActorType(String actorType) { this.actorType = actorType; return this; }

    public String getActorRef() { return actorRef; }
    public TicketEventEntity setActorRef(String actorRef) { this.actorRef = actorRef; return this; }

    public String getPayloadJson() { return payloadJson; }
    public TicketEventEntity setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketEventEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}

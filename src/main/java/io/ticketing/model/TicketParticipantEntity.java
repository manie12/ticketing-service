package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: ticket_participants
 *
 * DDL recap:
 *  ticket_participants(
 *    id uuid PK,
 *    tenant_id uuid NOT NULL,
 *    ticket_id uuid NOT NULL,
 *    participant_type text NOT NULL,  -- CUSTOMER|AGENT|EXTERNAL|SYSTEM
 *    participant_ref text NOT NULL,
 *    role text NOT NULL,              -- REQUESTER|CC|WATCHER
 *    is_active boolean NOT NULL default true,
 *    created_at timestamptz NOT NULL default now()
 *  )
 */
@Table("ticket_participants")
public class TicketParticipantEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("participant_type")
    private String participantType;

    @Column("participant_ref")
    private String participantRef;

    @Column("role")
    private String role;

    @Column("is_active")
    private Boolean isActive;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketParticipantEntity() {}

    public TicketParticipantEntity(UUID id, UUID tenantId, UUID ticketId,
                                   String participantType, String participantRef,
                                   String role, Boolean isActive, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.participantType = participantType;
        this.participantRef = participantRef;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static TicketParticipantEntity newParticipant(UUID tenantId, UUID ticketId,
                                                         String participantType, String participantRef, String role) {
        TicketParticipantEntity p = new TicketParticipantEntity();
        p.id = UUID.randomUUID();
        p.tenantId = tenantId;
        p.ticketId = ticketId;
        p.participantType = participantType;
        p.participantRef = participantRef;
        p.role = role;
        p.isActive = true;
        return p;
    }

    public UUID getId() { return id; }
    public TicketParticipantEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketParticipantEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketParticipantEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public String getParticipantType() { return participantType; }
    public TicketParticipantEntity setParticipantType(String participantType) { this.participantType = participantType; return this; }

    public String getParticipantRef() { return participantRef; }
    public TicketParticipantEntity setParticipantRef(String participantRef) { this.participantRef = participantRef; return this; }

    public String getRole() { return role; }
    public TicketParticipantEntity setRole(String role) { this.role = role; return this; }

    public Boolean getIsActive() { return isActive; }
    public TicketParticipantEntity setIsActive(Boolean active) { isActive = active; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketParticipantEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}
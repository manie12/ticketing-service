package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.ticket_participants
 * <p>
 * DDL:
 * ticket_participants(
 * tenant_id uuid NOT NULL FK tenants(id),
 * ticket_id uuid NOT NULL FK tickets(id) ON DELETE CASCADE,
 * participant_type varchar(20) NOT NULL check in (CUSTOMER,AGENT,SYSTEM),
 * participant_id uuid NOT NULL,
 * role varchar(30) NOT NULL default REQUESTER check in (REQUESTER,ASSIGNEE,WATCHER),
 * is_primary boolean NOT NULL default false,
 * added_at timestamptz NOT NULL default now(),
 * primary key (tenant_id, ticket_id, participant_type, participant_id)
 * )
 * <p>
 * Note: composite primary key -> no single @Id field.
 */
@Table(schema = "ticketing", name = "ticket_participants")
public class TicketParticipantEntity implements org.springframework.data.domain.Persistable<UUID>{
    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("participant_type")
    private String participantType; // CUSTOMER | AGENT | SYSTEM

    @Column("customer_email")
    private String customerEmail;

    @Column("role")
    private String role; // REQUESTER | ASSIGNEE | WATCHER

    @Column("is_primary")
    private Boolean isPrimary;

    @Column("added_at")
    private OffsetDateTime addedAt;

    @Transient
    private boolean isNew = false;

    public TicketParticipantEntity() {
    }

    public TicketParticipantEntity(UUID id,UUID tenantId, UUID ticketId, String participantType, String customerEmail,
                                   String role, Boolean isPrimary, OffsetDateTime addedAt) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.participantType = participantType;
        this.customerEmail = customerEmail;
        this.role = role;
        this.isPrimary = isPrimary;
        this.addedAt = addedAt;
    }

    public static TicketParticipantEntity requester(UUID tenantId, UUID ticketId, String customerEmail) {
        TicketParticipantEntity p = new TicketParticipantEntity();
        p.id = UUID.randomUUID();
        p.tenantId = tenantId;
        p.ticketId = ticketId;
        p.participantType = "";
        p.customerEmail = customerEmail;
        p.role = "";
        p.isPrimary = true;
        p.addedAt = OffsetDateTime.now();
        return p;
    }
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public TicketParticipantEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() {
        return id;
    }

    public TicketParticipantEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public TicketParticipantEntity setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }

    public TicketParticipantEntity setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }
    public UUID getTicketId() {
        return ticketId;
    }

    public TicketParticipantEntity setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public String getParticipantType() {
        return participantType;
    }

    public TicketParticipantEntity setParticipantType(String participantType) {
        this.participantType = participantType;
        return this;
    }

    public String getRole() {
        return role;
    }

    public TicketParticipantEntity setRole(String role) {
        this.role = role;
        return this;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public TicketParticipantEntity setIsPrimary(Boolean primary) {
        isPrimary = primary;
        return this;
    }

    public OffsetDateTime getAddedAt() {
        return addedAt;
    }

    public TicketParticipantEntity setAddedAt(OffsetDateTime addedAt) {
        this.addedAt = addedAt;
        return this;
    }
}
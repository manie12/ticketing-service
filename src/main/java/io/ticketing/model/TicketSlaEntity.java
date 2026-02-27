package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: ticket_sla
 *
 * DDL recap:
 *  ticket_sla(
 *    id uuid PK,
 *    tenant_id uuid NOT NULL,
 *    ticket_id uuid NOT NULL,
 *    policy_id uuid NOT NULL,
 *    first_response_due_at timestamptz NOT NULL,
 *    resolve_due_at timestamptz NOT NULL,
 *    first_response_at timestamptz NULL,
 *    resolved_at timestamptz NULL,
 *    first_response_breached boolean NOT NULL default false,
 *    resolve_breached boolean NOT NULL default false,
 *    created_at timestamptz NOT NULL default now(),
 *    updated_at timestamptz NOT NULL default now()
 *  )
 */
@Table("ticket_sla")
public class TicketSlaEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("policy_id")
    private UUID policyId;

    @Column("first_response_due_at")
    private OffsetDateTime firstResponseDueAt;

    @Column("resolve_due_at")
    private OffsetDateTime resolveDueAt;

    @Column("first_response_at")
    private OffsetDateTime firstResponseAt;

    @Column("resolved_at")
    private OffsetDateTime resolvedAt;

    @Column("first_response_breached")
    private Boolean firstResponseBreached;

    @Column("resolve_breached")
    private Boolean resolveBreached;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public TicketSlaEntity() {}

    public TicketSlaEntity(UUID id, UUID tenantId, UUID ticketId, UUID policyId,
                           OffsetDateTime firstResponseDueAt, OffsetDateTime resolveDueAt,
                           OffsetDateTime firstResponseAt, OffsetDateTime resolvedAt,
                           Boolean firstResponseBreached, Boolean resolveBreached,
                           OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.policyId = policyId;
        this.firstResponseDueAt = firstResponseDueAt;
        this.resolveDueAt = resolveDueAt;
        this.firstResponseAt = firstResponseAt;
        this.resolvedAt = resolvedAt;
        this.firstResponseBreached = firstResponseBreached;
        this.resolveBreached = resolveBreached;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---------- Factory helper ----------
    public static TicketSlaEntity newTicketSla(UUID tenantId, UUID ticketId, UUID policyId,
                                              OffsetDateTime firstResponseDueAt, OffsetDateTime resolveDueAt) {
        TicketSlaEntity s = new TicketSlaEntity();
        s.id = UUID.randomUUID();
        s.tenantId = tenantId;
        s.ticketId = ticketId;
        s.policyId = policyId;
        s.firstResponseDueAt = firstResponseDueAt;
        s.resolveDueAt = resolveDueAt;
        s.firstResponseBreached = false;
        s.resolveBreached = false;
        return s;
    }

    // ---------- Getters / Setters ----------
    public UUID getId() { return id; }
    public TicketSlaEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketSlaEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketSlaEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public UUID getPolicyId() { return policyId; }
    public TicketSlaEntity setPolicyId(UUID policyId) { this.policyId = policyId; return this; }

    public OffsetDateTime getFirstResponseDueAt() { return firstResponseDueAt; }
    public TicketSlaEntity setFirstResponseDueAt(OffsetDateTime firstResponseDueAt) { this.firstResponseDueAt = firstResponseDueAt; return this; }

    public OffsetDateTime getResolveDueAt() { return resolveDueAt; }
    public TicketSlaEntity setResolveDueAt(OffsetDateTime resolveDueAt) { this.resolveDueAt = resolveDueAt; return this; }

    public OffsetDateTime getFirstResponseAt() { return firstResponseAt; }
    public TicketSlaEntity setFirstResponseAt(OffsetDateTime firstResponseAt) { this.firstResponseAt = firstResponseAt; return this; }

    public OffsetDateTime getResolvedAt() { return resolvedAt; }
    public TicketSlaEntity setResolvedAt(OffsetDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }

    public Boolean getFirstResponseBreached() { return firstResponseBreached; }
    public TicketSlaEntity setFirstResponseBreached(Boolean firstResponseBreached) { this.firstResponseBreached = firstResponseBreached; return this; }

    public Boolean getResolveBreached() { return resolveBreached; }
    public TicketSlaEntity setResolveBreached(Boolean resolveBreached) { this.resolveBreached = resolveBreached; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketSlaEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public TicketSlaEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
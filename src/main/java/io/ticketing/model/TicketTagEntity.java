package io.ticketing.model;

// ============================================================
// TICKET_TAGS (Join table, composite PK in DB)
// In R2DBC entity, represent composite key as fields.
// ============================================================
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
@Table("ticket_tags")
class TicketTagEntity {

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("tag_id")
    private UUID tagId;

    public TicketTagEntity() {}

    public TicketTagEntity(UUID tenantId, UUID ticketId, UUID tagId) {
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.tagId = tagId;
    }

    public UUID getTenantId() { return tenantId; }
    public TicketTagEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketTagEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public UUID getTagId() { return tagId; }
    public TicketTagEntity setTagId(UUID tagId) { this.tagId = tagId; return this; }
}
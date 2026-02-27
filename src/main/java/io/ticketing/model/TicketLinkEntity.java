package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: ticket_links
 *
 * DDL recap:
 *  ticket_links(
 *    id uuid PK,
 *    tenant_id uuid NOT NULL,
 *    from_ticket_id uuid NOT NULL,
 *    to_ticket_id uuid NOT NULL,
 *    link_type text NOT NULL, -- DUPLICATE_OF|RELATES_TO|BLOCKS|PARENT_OF|CHILD_OF
 *    note text NULL,
 *    created_at timestamptz NOT NULL default now()
 *  )
 */
@Table("ticket_links")
public class TicketLinkEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("from_ticket_id")
    private UUID fromTicketId;

    @Column("to_ticket_id")
    private UUID toTicketId;

    @Column("link_type")
    private String linkType;

    @Column("note")
    private String note;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketLinkEntity() {}

    public TicketLinkEntity(UUID id, UUID tenantId, UUID fromTicketId, UUID toTicketId,
                            String linkType, String note, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.fromTicketId = fromTicketId;
        this.toTicketId = toTicketId;
        this.linkType = linkType;
        this.note = note;
        this.createdAt = createdAt;
    }

    public static TicketLinkEntity newLink(UUID tenantId, UUID fromTicketId, UUID toTicketId,
                                           String linkType, String note) {
        TicketLinkEntity l = new TicketLinkEntity();
        l.id = UUID.randomUUID();
        l.tenantId = tenantId;
        l.fromTicketId = fromTicketId;
        l.toTicketId = toTicketId;
        l.linkType = linkType;
        l.note = note;
        return l;
    }

    public UUID getId() { return id; }
    public TicketLinkEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketLinkEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getFromTicketId() { return fromTicketId; }
    public TicketLinkEntity setFromTicketId(UUID fromTicketId) { this.fromTicketId = fromTicketId; return this; }

    public UUID getToTicketId() { return toTicketId; }
    public TicketLinkEntity setToTicketId(UUID toTicketId) { this.toTicketId = toTicketId; return this; }

    public String getLinkType() { return linkType; }
    public TicketLinkEntity setLinkType(String linkType) { this.linkType = linkType; return this; }

    public String getNote() { return note; }
    public TicketLinkEntity setNote(String note) { this.note = note; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketLinkEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}
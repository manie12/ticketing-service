package io.ticketing.model;

// ============================================================
// TICKET MESSAGES
// ============================================================
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
@Table("ticket_messages")
class TicketMessageEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("public_id")
    private String publicId;

    @Column("author_type")
    private String authorType;     // CUSTOMER | AGENT | SYSTEM

    @Column("author_ref")
    private String authorRef;

    @Column("body")
    private String body;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketMessageEntity() {}

    public TicketMessageEntity(UUID id, UUID tenantId, UUID ticketId, String publicId,
                               String authorType, String authorRef, String body, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.publicId = publicId;
        this.authorType = authorType;
        this.authorRef = authorRef;
        this.body = body;
        this.createdAt = createdAt;
    }

    public static TicketMessageEntity newMessage(UUID tenantId, UUID ticketId, String publicId,
                                                 String authorType, String authorRef, String body) {
        TicketMessageEntity m = new TicketMessageEntity();
        m.id = UUID.randomUUID();
        m.tenantId = tenantId;
        m.ticketId = ticketId;
        m.publicId = publicId;
        m.authorType = authorType;
        m.authorRef = authorRef;
        m.body = body;
        return m;
    }

    public UUID getId() { return id; }
    public TicketMessageEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketMessageEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketMessageEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public String getPublicId() { return publicId; }
    public TicketMessageEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getAuthorType() { return authorType; }
    public TicketMessageEntity setAuthorType(String authorType) { this.authorType = authorType; return this; }

    public String getAuthorRef() { return authorRef; }
    public TicketMessageEntity setAuthorRef(String authorRef) { this.authorRef = authorRef; return this; }

    public String getBody() { return body; }
    public TicketMessageEntity setBody(String body) { this.body = body; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketMessageEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}


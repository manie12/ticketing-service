package io.ticketing.model;

// ============================================================
// TICKETS
// ============================================================
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("tickets")
class TicketEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("public_id")
    private String publicId;

    @Column("request_id")
    private String requestId;

    @Column("customer_id")
    private UUID customerId;

    @Column("channel")
    private String channel;     // WEB | EMAIL | WHATSAPP | API | PHONE | IN_APP

    @Column("status")
    private String status;      // OPEN | PENDING | RESOLVED | CLOSED

    @Column("priority")
    private String priority;    // LOW | MEDIUM | HIGH | URGENT

    @Column("category_id")
    private UUID categoryId;    // optional categories table

    @Column("subject")
    private String subject;

    @Column("description")
    private String description;

    @Column("assignee_ref")
    private String assigneeRef;

    @Column("opened_at")
    private OffsetDateTime openedAt;

    @Column("closed_at")
    private OffsetDateTime closedAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public TicketEntity() {}

    public TicketEntity(UUID id, UUID tenantId, String publicId, String requestId, UUID customerId,
                        String channel, String status, String priority, UUID categoryId,
                        String subject, String description, String assigneeRef,
                        OffsetDateTime openedAt, OffsetDateTime closedAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.requestId = requestId;
        this.customerId = customerId;
        this.channel = channel;
        this.status = status;
        this.priority = priority;
        this.categoryId = categoryId;
        this.subject = subject;
        this.description = description;
        this.assigneeRef = assigneeRef;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.updatedAt = updatedAt;
    }

    public static TicketEntity newTicket(UUID tenantId, String publicId, String requestId, UUID customerId,
                                         String channel, String priority, String subject, String description) {
        TicketEntity t = new TicketEntity();
        t.id = UUID.randomUUID();
        t.tenantId = tenantId;
        t.publicId = publicId;
        t.requestId = requestId;
        t.customerId = customerId;
        t.channel = channel;
        t.status = "OPEN";
        t.priority = priority;
        t.subject = subject;
        t.description = description;
        t.openedAt = OffsetDateTime.now();
        t.updatedAt = t.openedAt;
        return t;
    }

    public UUID getId() { return id; }
    public TicketEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getPublicId() { return publicId; }
    public TicketEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getRequestId() { return requestId; }
    public TicketEntity setRequestId(String requestId) { this.requestId = requestId; return this; }

    public UUID getCustomerId() { return customerId; }
    public TicketEntity setCustomerId(UUID customerId) { this.customerId = customerId; return this; }

    public String getChannel() { return channel; }
    public TicketEntity setChannel(String channel) { this.channel = channel; return this; }

    public String getStatus() { return status; }
    public TicketEntity setStatus(String status) { this.status = status; return this; }

    public String getPriority() { return priority; }
    public TicketEntity setPriority(String priority) { this.priority = priority; return this; }

    public UUID getCategoryId() { return categoryId; }
    public TicketEntity setCategoryId(UUID categoryId) { this.categoryId = categoryId; return this; }

    public String getSubject() { return subject; }
    public TicketEntity setSubject(String subject) { this.subject = subject; return this; }

    public String getDescription() { return description; }
    public TicketEntity setDescription(String description) { this.description = description; return this; }

    public String getAssigneeRef() { return assigneeRef; }
    public TicketEntity setAssigneeRef(String assigneeRef) { this.assigneeRef = assigneeRef; return this; }

    public OffsetDateTime getOpenedAt() { return openedAt; }
    public TicketEntity setOpenedAt(OffsetDateTime openedAt) { this.openedAt = openedAt; return this; }

    public OffsetDateTime getClosedAt() { return closedAt; }
    public TicketEntity setClosedAt(OffsetDateTime closedAt) { this.closedAt = closedAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public TicketEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}

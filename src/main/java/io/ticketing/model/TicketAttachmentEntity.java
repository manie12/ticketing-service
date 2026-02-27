package io.ticketing.model;

// ============================================================
// TICKET ATTACHMENTS
// ============================================================
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
@Table("ticket_attachments")
class TicketAttachmentEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("message_id")
    private UUID messageId;

    @Column("public_id")
    private String publicId;

    @Column("file_name")
    private String fileName;

    @Column("content_type")
    private String contentType;

    @Column("size_bytes")
    private Long sizeBytes;

    @Column("storage_key")
    private String storageKey;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketAttachmentEntity() {}

    public TicketAttachmentEntity(UUID id, UUID tenantId, UUID ticketId, UUID messageId, String publicId,
                                  String fileName, String contentType, Long sizeBytes, String storageKey,
                                  OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.messageId = messageId;
        this.publicId = publicId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storageKey = storageKey;
        this.createdAt = createdAt;
    }

    public static TicketAttachmentEntity newAttachment(UUID tenantId, UUID ticketId, UUID messageId, String publicId,
                                                       String fileName, String contentType, Long sizeBytes, String storageKey) {
        TicketAttachmentEntity a = new TicketAttachmentEntity();
        a.id = UUID.randomUUID();
        a.tenantId = tenantId;
        a.ticketId = ticketId;
        a.messageId = messageId;
        a.publicId = publicId;
        a.fileName = fileName;
        a.contentType = contentType;
        a.sizeBytes = sizeBytes;
        a.storageKey = storageKey;
        return a;
    }

    public UUID getId() { return id; }
    public TicketAttachmentEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TicketAttachmentEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getTicketId() { return ticketId; }
    public TicketAttachmentEntity setTicketId(UUID ticketId) { this.ticketId = ticketId; return this; }

    public UUID getMessageId() { return messageId; }
    public TicketAttachmentEntity setMessageId(UUID messageId) { this.messageId = messageId; return this; }

    public String getPublicId() { return publicId; }
    public TicketAttachmentEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getFileName() { return fileName; }
    public TicketAttachmentEntity setFileName(String fileName) { this.fileName = fileName; return this; }

    public String getContentType() { return contentType; }
    public TicketAttachmentEntity setContentType(String contentType) { this.contentType = contentType; return this; }

    public Long getSizeBytes() { return sizeBytes; }
    public TicketAttachmentEntity setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; return this; }

    public String getStorageKey() { return storageKey; }
    public TicketAttachmentEntity setStorageKey(String storageKey) { this.storageKey = storageKey; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TicketAttachmentEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}

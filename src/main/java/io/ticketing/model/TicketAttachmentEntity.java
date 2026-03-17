package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.ticket_attachments
 * <p>
 * DDL (summary):
 * ticket_attachments(
 * id uuid PK,
 * tenant_id uuid NOT NULL FK tenants(id),
 * ticket_id uuid NOT NULL FK tickets(id) ON DELETE CASCADE,
 * message_id uuid NULL FK ticket_messages(id) ON DELETE SET NULL,
 * file_name varchar(260) NOT NULL,
 * content_type varchar(120) NOT NULL,
 * file_size_bytes bigint NOT NULL check(file_size_bytes >= 0),
 * storage_provider varchar(30) NOT NULL check in (S3,AZURE_BLOB,LOCAL),
 * storage_path text NOT NULL,
 * checksum_sha256 varchar(64) NULL,
 * created_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "ticket_attachments")
public class TicketAttachmentEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("message_id")
    private UUID messageId; // nullable

    @Column("customer_email")
    private String customerEmail; // nullable

    @Column("file_name")
    private String fileName;

    @Column("content_type")
    private String contentType;

    @Column("file_size_bytes")
    private Long fileSizeBytes;

    @Column("storage_provider")
    private String storageProvider; // S3 | AZURE_BLOB | LOCAL

    @Column("storage_path")
    private String storagePath;

    @Column("checksum_sha256")
    private String checksumSha256; // nullable

    @Column("created_at")
    private OffsetDateTime createdAt;

    public TicketAttachmentEntity() {
    }

    public TicketAttachmentEntity(UUID id, UUID tenantId, UUID ticketId, UUID messageId,
                                  String customerEmail, String fileName, String contentType, Long fileSizeBytes,
                                  String storageProvider, String storagePath, String checksumSha256,
                                  OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.messageId = messageId;
        this.customerEmail = customerEmail;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSizeBytes = fileSizeBytes;
        this.storageProvider = storageProvider;
        this.storagePath = storagePath;
        this.checksumSha256 = checksumSha256;
        this.createdAt = createdAt;
    }

    public static TicketAttachmentEntity newAttachment(UUID tenantId,
                                                       UUID ticketId,
                                                       UUID messageId,
                                                       String customerEmail,
                                                       String fileName,
                                                       String contentType,
                                                       long fileSizeBytes,
                                                       String storageProvider,
                                                       String storagePath) {
        TicketAttachmentEntity a = new TicketAttachmentEntity();
        a.id = UUID.randomUUID();
        a.tenantId = tenantId;
        a.ticketId = ticketId;
        a.messageId = messageId;
        a.customerEmail = customerEmail;
        a.fileName = fileName;
        a.contentType = contentType;
        a.fileSizeBytes = fileSizeBytes;
        a.storageProvider = storageProvider;
        a.storagePath = storagePath;
        return a;
    }

    public UUID getId() {
        return id;
    }

    public TicketAttachmentEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public TicketAttachmentEntity setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public TicketAttachmentEntity setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public TicketAttachmentEntity setMessageId(UUID messageId) {
        this.messageId = messageId;
        return this;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }

    public TicketAttachmentEntity setCustomerEmail(String  customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public TicketAttachmentEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public TicketAttachmentEntity setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public TicketAttachmentEntity setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
        return this;
    }

    public String getStorageProvider() {
        return storageProvider;
    }

    public TicketAttachmentEntity setStorageProvider(String storageProvider) {
        this.storageProvider = storageProvider;
        return this;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public TicketAttachmentEntity setStoragePath(String storagePath) {
        this.storagePath = storagePath;
        return this;
    }

    public String getChecksumSha256() {
        return checksumSha256;
    }

    public TicketAttachmentEntity setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TicketAttachmentEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
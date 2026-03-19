-- V9__create_ticket_attachments.sql
-- Creates: ticketing.ticket_attachments
-- Matches TicketAttachmentEntity columns exactly.
-- Depends on: tenants (V2), tickets (V6), ticket_messages (V7)

CREATE TABLE IF NOT EXISTS ticketing.ticket_attachments
(
    id               UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id        UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    ticket_id        UUID                                   NOT NULL
    REFERENCES ticketing.tickets
    ON DELETE CASCADE,

    message_id       UUID
    REFERENCES ticketing.ticket_messages
    ON DELETE SET NULL,

    customer_email   VARCHAR(254),

    file_name        VARCHAR(260)                           NOT NULL,

    content_type     VARCHAR(120)                           NOT NULL,

    file_size_bytes  BIGINT                                 NOT NULL
    CONSTRAINT chk_file_size
    CHECK (file_size_bytes >= 0),

    storage_provider VARCHAR(30)                            NOT NULL
    CONSTRAINT chk_storage_provider
    CHECK (storage_provider = ANY (ARRAY['S3','AZURE_BLOB','LOCAL'])),

    storage_path     TEXT                                   NOT NULL,

    checksum_sha256  VARCHAR(64),

    created_at       TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_ticket_attachments_ticket
    ON ticketing.ticket_attachments (ticket_id);

CREATE INDEX IF NOT EXISTS ix_ticket_attachments_message
    ON ticketing.ticket_attachments (message_id);

CREATE INDEX IF NOT EXISTS ix_ticket_attachments_tenant_ticket
    ON ticketing.ticket_attachments (tenant_id, ticket_id);

CREATE INDEX IF NOT EXISTS ix_ticket_attachments_customer_email
    ON ticketing.ticket_attachments (tenant_id, customer_email)
    WHERE customer_email IS NOT NULL;

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.ticket_attachments REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.ticket_attachments
(id, tenant_id, ticket_id, message_id, customer_email, file_name, content_type, file_size_bytes,
 storage_provider, storage_path, checksum_sha256, created_at)
VALUES
    (
        'ee0b1c2d-3e4f-4a5b-9c01-2a3b4c5da001',
        '11111111-1111-1111-1111-111111111111',
        '7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01',
        'bb0b1c2d-3e4f-4a5b-9c01-2a3b4c5dc001',
        'kip@example.com',
        'payment-error.png',
        'image/png',
        245233,
        'S3',
        'tmp/tickets/REQ-WEB-20260319-000001/payment-error.png',
        'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855',
        NOW()
    ),
    (
        'ee0b1c2d-3e4f-4a5b-9c01-2a3b4c5da002',
        '11111111-1111-1111-1111-111111111111',
        '9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02',
        NULL,
        'esther@example.com',
        'otp-screenshot.jpg',
        'image/jpeg',
        180044,
        'AZURE_BLOB',
        'tmp/tickets/REQ-WHATSAPP-20260319-000002/otp-screenshot.jpg',
        NULL,
        NOW()
    );

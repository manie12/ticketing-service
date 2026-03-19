-- V7__create_ticket_messages.sql
-- Creates: ticketing.ticket_messages
-- Matches TicketMessageEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.ticket_messages
(
    id           UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id     UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    ticket_id     UUID                                   NOT NULL
    REFERENCES ticketing.tickets
    ON DELETE CASCADE,

    sender_type   VARCHAR(20)                            NOT NULL
    CONSTRAINT chk_sender_type
    CHECK (sender_type = ANY (ARRAY['CUSTOMER','AGENT','SYSTEM'])),

    sender_email  VARCHAR(254),

    message_type  VARCHAR(30)              DEFAULT 'TEXT' NOT NULL
    CONSTRAINT chk_message_type
    CHECK (message_type = ANY (ARRAY['TEXT','DESCRIPTION','SYSTEM_NOTE'])),

    body          TEXT                                   NOT NULL,

    channel_code  VARCHAR(40)
    CONSTRAINT chk_ticket_messages_channel_code
    CHECK (channel_code IS NULL OR channel_code ~ '^[A-Z0-9_]+$'),

    created_at    TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_ticket_messages_ticket_created
    ON ticketing.ticket_messages (ticket_id, created_at);

CREATE INDEX IF NOT EXISTS ix_ticket_messages_tenant_ticket_created
    ON ticketing.ticket_messages (tenant_id, ticket_id, created_at);

CREATE INDEX IF NOT EXISTS ix_ticket_messages_sender_email
    ON ticketing.ticket_messages (tenant_id, sender_email)
    WHERE sender_email IS NOT NULL;

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.ticket_messages REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.ticket_messages
(id, tenant_id, ticket_id, sender_type, sender_email, message_type, body, channel_code, created_at)
VALUES
    (
        'bb0b1c2d-3e4f-4a5b-9c01-2a3b4c5dc001',
        '11111111-1111-1111-1111-111111111111',
        '7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01',
        'CUSTOMER',
        'kip@example.com',
        'DESCRIPTION',
        'Tried paying via card and it failed with error P123. Please assist.',
        'WEB',
        NOW()
    ),
    (
        'bb0b1c2d-3e4f-4a5b-9c01-2a3b4c5dc002',
        '11111111-1111-1111-1111-111111111111',
        '9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02',
        'SYSTEM',
        NULL,
        'SYSTEM_NOTE',
        'Ticket auto-routed to L1 queue based on channel=WHATSAPP and category=ACCOUNT_ACCESS.',
        'WHATSAPP',
        NOW()
    );

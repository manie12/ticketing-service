-- V6__create_tickets.sql
-- Creates: ticketing.tickets
-- Matches TicketEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.tickets
(
    id             UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id      UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    public_id      VARCHAR(40)                            NOT NULL,

    request_id     VARCHAR(128)                           NOT NULL,

    customer_email VARCHAR(254)                           NOT NULL,

    channel_code   VARCHAR(40)                            NOT NULL
    CONSTRAINT chk_tickets_channel_code
    CHECK (channel_code ~ '^[A-Z0-9_]+$'),

    status         VARCHAR(30)              DEFAULT 'OPEN' NOT NULL
    CONSTRAINT chk_tickets_status
    CHECK (status = ANY (ARRAY['OPEN','PENDING','RESOLVED','CLOSED'])),

    priority       VARCHAR(20)              DEFAULT 'MEDIUM' NOT NULL
    CONSTRAINT chk_tickets_priority
    CHECK (priority = ANY (ARRAY['LOW','MEDIUM','HIGH','URGENT'])),

    category_code  VARCHAR(60),

    subject        VARCHAR(500)                           NOT NULL,

    description    TEXT,

    assignee_ref   VARCHAR(200),

    opened_at      TIMESTAMPTZ              DEFAULT NOW() NOT NULL,

    closed_at      TIMESTAMPTZ,

    updated_at     TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Uniqueness: one request per tenant+channel
CREATE UNIQUE INDEX IF NOT EXISTS uq_tickets_tenant_channel_request
    ON ticketing.tickets (tenant_id, channel_code, request_id);

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_tickets_tenant_status
    ON ticketing.tickets (tenant_id, status);

CREATE INDEX IF NOT EXISTS ix_tickets_tenant_channel
    ON ticketing.tickets (tenant_id, channel_code);

CREATE INDEX IF NOT EXISTS ix_tickets_tenant_customer_email
    ON ticketing.tickets (tenant_id, customer_email);

CREATE INDEX IF NOT EXISTS ix_tickets_public_id
    ON ticketing.tickets (public_id);

CREATE INDEX IF NOT EXISTS ix_tickets_opened_at
    ON ticketing.tickets (opened_at);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.tickets REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.tickets
(id, tenant_id, public_id, request_id, customer_email, channel_code, status, priority,
 category_code, subject, description, assignee_ref, opened_at, closed_at, updated_at)
VALUES
    (
        '7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01',
        '11111111-1111-1111-1111-111111111111',
        'TCK-2026-A1B2C3D4',
        'REQ-WEB-20260319-000001',
        'kip@example.com',
        'WEB',
        'OPEN',
        'MEDIUM',
        'BILLING',
        'Card payment failed with error P123',
        'Tried paying via card and it failed with error P123. Please assist.',
        NULL,
        NOW(),
        NULL,
        NOW()
    ),
    (
        '9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02',
        '11111111-1111-1111-1111-111111111111',
        'TCK-2026-E5F6G7H8',
        'REQ-WHATSAPP-20260319-000002',
        'esther@example.com',
        'WHATSAPP',
        'PENDING',
        'HIGH',
        'ACCOUNT_ACCESS',
        'Cannot login – OTP not received',
        'I have not received an OTP for the last 30 minutes. My account is locked.',
        NULL,
        NOW(),
        NULL,
        NOW()
    );

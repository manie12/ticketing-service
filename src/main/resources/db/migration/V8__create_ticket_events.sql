-- V8__create_ticket_events.sql
-- Creates: ticketing.ticket_events
-- Matches TicketEventEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.ticket_events
(
    id             UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id       UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    ticket_id       UUID                                   NOT NULL
    REFERENCES ticketing.tickets
    ON DELETE CASCADE,

    event_type      VARCHAR(60)                            NOT NULL,

    event_category  VARCHAR(30),

    from_status     VARCHAR(40),

    to_status       VARCHAR(40),

    actor_type      VARCHAR(20)                            NOT NULL
    CONSTRAINT chk_event_actor_type
    CHECK (actor_type = ANY (ARRAY['CUSTOMER','AGENT','SYSTEM'])),

    customer_email  VARCHAR(254),

    meta            TEXT,

    occurred_at     TIMESTAMPTZ              DEFAULT NOW() NOT NULL,
    created_at      TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_ticket_events_ticket
    ON ticketing.ticket_events (ticket_id);

CREATE INDEX IF NOT EXISTS ix_ticket_events_tenant_ticket
    ON ticketing.ticket_events (tenant_id, ticket_id);

CREATE INDEX IF NOT EXISTS ix_ticket_events_type
    ON ticketing.ticket_events (event_type);

CREATE INDEX IF NOT EXISTS ix_ticket_events_occurred_at
    ON ticketing.ticket_events (occurred_at);

CREATE INDEX IF NOT EXISTS ix_ticket_events_customer_email
    ON ticketing.ticket_events (tenant_id, customer_email)
    WHERE customer_email IS NOT NULL;

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.ticket_events REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.ticket_events
(id, tenant_id, ticket_id, event_type, event_category, from_status, to_status, actor_type,
 customer_email, meta, occurred_at, created_at)
VALUES
    (
        'cc0b1c2d-3e4f-4a5b-9c01-2a3b4c5db001',
        '11111111-1111-1111-1111-111111111111',
        '7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01',
        'TICKET_CREATED',
        'BILLING',
        NULL,
        'OPEN',
        'CUSTOMER',
        'kip@example.com',
        '{"channelCode":"WEB","categoryCode":"BILLING","priority":"MEDIUM"}'::jsonb,
        NOW(),
        NOW()
    ),
    (
        'cc0b1c2d-3e4f-4a5b-9c01-2a3b4c5db002',
        '11111111-1111-1111-1111-111111111111',
        '9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02',
        'TICKET_STATUS_CHANGED',
        'ACCOUNT_ACCESS',
        'OPEN',
        'PENDING',
        'SYSTEM',
        'esther@example.com',
        '{"reason":"OTP retry limit reached","priority":"HIGH"}'::jsonb,
        NOW(),
        NOW()
    );

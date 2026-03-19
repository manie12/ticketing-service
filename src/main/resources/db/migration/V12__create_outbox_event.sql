-- V12__create_outbox_event.sql
-- Creates: ticketing.outbox_event (outbox pattern table)
-- Matches OutboxEvent columns exactly.
-- Depends on: tenants (V2)

CREATE TABLE IF NOT EXISTS ticketing.outbox_event
(
    id             UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id       UUID
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    aggregate_type  VARCHAR(50)                            NOT NULL,

    aggregate_id    VARCHAR(64)                            NOT NULL,

    event_type      VARCHAR(100)                           NOT NULL,

    event_version   INTEGER                  DEFAULT 1     NOT NULL,

    payload         TEXT                                   NOT NULL,

    correlation_id  VARCHAR(128),

    occurred_at     TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_outbox_event_tenant_occurred_at
    ON ticketing.outbox_event (tenant_id, occurred_at);

CREATE INDEX IF NOT EXISTS ix_outbox_event_aggregate
    ON ticketing.outbox_event (aggregate_type, aggregate_id);

CREATE INDEX IF NOT EXISTS ix_outbox_event_tenant_aggregate
    ON ticketing.outbox_event (tenant_id, aggregate_type, aggregate_id);

CREATE INDEX IF NOT EXISTS ix_outbox_event_type
    ON ticketing.outbox_event (event_type);

CREATE INDEX IF NOT EXISTS ix_outbox_event_occurred_at
    ON ticketing.outbox_event (occurred_at);

CREATE INDEX IF NOT EXISTS ix_outbox_event_correlation_id
    ON ticketing.outbox_event (correlation_id);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.outbox_event REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.outbox_event
(id, tenant_id, aggregate_type, aggregate_id, event_type, event_version, payload, correlation_id, occurred_at)
VALUES
    (
        'f1a2b3c4-d5e6-4f70-8a90-1b2c3d4e8001',
        '11111111-1111-1111-1111-111111111111',
        'TICKET',
        '7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01',
        'TICKET_CREATED',
        1,
        '{"ticketId":"7b2c9b3c-3f65-4a8d-9d58-7b8a4f7f6f01","publicId":"TCK-2026-A1B2C3D4","channelCode":"WEB","priority":"MEDIUM","status":"OPEN"}'::jsonb,
        'REQ-WEB-20260319-000001',
        NOW()
    ),
    (
        'f1a2b3c4-d5e6-4f70-8a90-1b2c3d4e8002',
        '11111111-1111-1111-1111-111111111111',
        'TICKET',
        '9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02',
        'TICKET_CREATED',
        1,
        '{"ticketId":"9d6f1f1c-9c5b-4c3a-8cbb-1e0b2d3a4f02","publicId":"TCK-2026-E5F6G7H8","channelCode":"WHATSAPP","priority":"HIGH","status":"PENDING"}'::jsonb,
        'REQ-WHATSAPP-20260319-000002',
        NOW()
    );

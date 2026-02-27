-- V1__create_outbox_event.sql
CREATE TABLE IF NOT EXISTS outbox_event
(
    id             UUID PRIMARY KEY,
    aggregate_type VARCHAR(50)  NOT NULL,
    aggregate_id   VARCHAR(64)  NOT NULL,
    event_type     VARCHAR(100) NOT NULL,
    event_version  INTEGER      NOT NULL DEFAULT 1,
    payload        JSONB        NOT NULL,
    correlation_id VARCHAR(128),
    occurred_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS ix_outbox_event_aggregate ON outbox_event (aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS ix_outbox_event_type ON outbox_event (event_type);
CREATE INDEX IF NOT EXISTS ix_outbox_event_occurred_at ON outbox_event (occurred_at);
CREATE INDEX IF NOT EXISTS ix_outbox_event_correlation_id ON outbox_event (correlation_id);

ALTER TABLE outbox_event REPLICA IDENTITY FULL;

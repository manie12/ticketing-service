-- V13__create_processed_event.sql
-- Creates: ticketing.processed_event (idempotency table for Kafka consumers)
-- Matches ProcessedEventEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.processed_event
(
    id           VARCHAR(256)                           NOT NULL
    PRIMARY KEY,

    processed_at TIMESTAMPTZ              DEFAULT NOW() NOT NULL
);

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_processed_event_processed_at
    ON ticketing.processed_event (processed_at);

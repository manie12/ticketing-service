-- V3__create_channels.sql
-- Creates: ticketing.channels
-- Matches ChannelEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.channels
(
    id         UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id  UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    code       VARCHAR(40)                            NOT NULL
    CONSTRAINT chk_channels_code
    CHECK (code ~ '^[A-Z0-9_]+$'),

    name       VARCHAR(120)                           NOT NULL,

    is_enabled BOOLEAN                  DEFAULT TRUE  NOT NULL,

    config     TEXT,

    created_at TIMESTAMPTZ              DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ              DEFAULT NOW() NOT NULL,

    CONSTRAINT uq_channels_tenant_code
    UNIQUE (tenant_id, code)
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_channels_tenant_enabled
    ON ticketing.channels (tenant_id, is_enabled);

CREATE INDEX IF NOT EXISTS ix_channels_tenant_code
    ON ticketing.channels (tenant_id, code);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.channels REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.channels
    (id, tenant_id, code, name, is_enabled, config, created_at, updated_at)
VALUES ('a2d1f0c3-4b10-4a52-9c1a-1a2b3c4d6001',
        '11111111-1111-1111-1111-111111111111',
        'WEB',
        'Web Portal',
        TRUE,
        '{"inbound":"portal","maxAttachmentMb":10,"supportedFormats":["png","jpg","pdf"]}'::jsonb,
        NOW(),
        NOW()),
       ('a2d1f0c3-4b10-4a52-9c1a-1a2b3c4d6002',
        '11111111-1111-1111-1111-111111111111',
        'WHATSAPP',
        'WhatsApp',
        TRUE,
        '{"inbound":"whatsapp","provider":"twilio","webhookPath":"/webhooks/whatsapp","autoReplyEnabled":true}'::jsonb,
        NOW(),
        NOW());

-- V11__create_settings.sql
-- Creates: ticketing.settings
-- Matches SettingEntity columns exactly.
-- Depends on: tenants (V2), channels (V3), categories (V4)

CREATE TABLE IF NOT EXISTS ticketing.settings
(
    id           UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id    UUID
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    channel_id   UUID
    REFERENCES ticketing.channels
    ON DELETE RESTRICT,

    category_id  UUID
    REFERENCES ticketing.categories
    ON DELETE RESTRICT,

    key          VARCHAR(120)                           NOT NULL,

    value_type   VARCHAR(20)              DEFAULT 'STRING' NOT NULL
    CONSTRAINT chk_settings_type
    CHECK (value_type = ANY (ARRAY['STRING','NUMBER','BOOLEAN','JSON'])),

    value        TEXT,

    value_json   TEXT,

    is_active    BOOLEAN                 DEFAULT TRUE   NOT NULL,

    description  VARCHAR(400),

    created_at   TIMESTAMPTZ             DEFAULT NOW()  NOT NULL,
    updated_at   TIMESTAMPTZ             DEFAULT NOW()  NOT NULL
    );

-- Query helpers (effective setting resolution)
CREATE INDEX IF NOT EXISTS ix_settings_key_active
    ON ticketing.settings (key, is_active);

CREATE INDEX IF NOT EXISTS ix_settings_tenant_key_active
    ON ticketing.settings (tenant_id, key, is_active);

CREATE INDEX IF NOT EXISTS ix_settings_scope_key_active
    ON ticketing.settings (tenant_id, channel_id, category_id, key, is_active);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.settings REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.settings
(id, tenant_id, channel_id, category_id, key, value_type, value, value_json, is_active, description, created_at, updated_at)
VALUES
    (
        'aa0b1c2d-3e4f-4a5b-9c01-2a3b4c5d9001',
        NULL,
        NULL,
        NULL,
        'ticket.default_priority',
        'STRING',
        'LOW',
        NULL,
        TRUE,
        'Global default priority for new tickets when not provided.',
        NOW(),
        NOW()
    ),
    (
        'aa0b1c2d-3e4f-4a5b-9c01-2a3b4c5d9002',
        '11111111-1111-1111-1111-111111111111',
        'a2d1f0c3-4b10-4a52-9c1a-1a2b3c4d6002',
        NULL,
        'ticket.default_priority',
        'STRING',
        'HIGH',
        NULL,
        TRUE,
        'Tenant override: WhatsApp tickets default to HIGH priority.',
        NOW(),
        NOW()
    );

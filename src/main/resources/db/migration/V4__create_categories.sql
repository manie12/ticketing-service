-- V4__create_categories.sql
-- Creates: ticketing.categories
-- Matches CategoryEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.categories
(
    id                    UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id             UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    code                  VARCHAR(60)                            NOT NULL
    CONSTRAINT chk_categories_code
    CHECK (code ~ '^[A-Z0-9_]+$'),

    name                  VARCHAR(160)                           NOT NULL,

    description           VARCHAR(500),

    is_active             BOOLEAN                  DEFAULT TRUE  NOT NULL,

    allowed_channel_codes TEXT[],

    created_at            TIMESTAMPTZ              DEFAULT NOW() NOT NULL,
    updated_at            TIMESTAMPTZ              DEFAULT NOW() NOT NULL,

    CONSTRAINT uq_categories_tenant_code
    UNIQUE (tenant_id, code)
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_categories_tenant_active
    ON ticketing.categories (tenant_id, is_active);

CREATE INDEX IF NOT EXISTS ix_categories_tenant_code
    ON ticketing.categories (tenant_id, code);

CREATE INDEX IF NOT EXISTS ix_categories_tenant_name
    ON ticketing.categories (tenant_id, name);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.categories REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.categories
(id, tenant_id, code, name, description, is_active, allowed_channel_codes, created_at, updated_at)
VALUES ('b7b0a6c1-2fd6-4f2d-9b9d-1f2a3b4c5001',
        '11111111-1111-1111-1111-111111111111',
        'BILLING',
        'Billing & Payments',
        'Issues related to card payments, M-Pesa, charges, and invoices.',
        TRUE,
        ARRAY['WEB', 'EMAIL', 'WHATSAPP'],
        NOW(),
        NOW()),
       ('b7b0a6c1-2fd6-4f2d-9b9d-1f2a3b4c5002',
        '11111111-1111-1111-1111-111111111111',
        'ACCOUNT_ACCESS',
        'Account Access',
        'Login issues, OTP problems, locked accounts, and credential recovery.',
        TRUE,
        ARRAY['WEB', 'WHATSAPP'],
        NOW(),
        NOW());

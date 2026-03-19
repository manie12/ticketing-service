-- V5__create_customers.sql
-- Creates: ticketing.customers
-- Matches CustomerEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.customers
(
    id                UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id         UUID                                   NOT NULL
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    customer_code     VARCHAR(80),

    full_name         VARCHAR(200)                           NOT NULL,

    email             VARCHAR(254),

    phone_e164        VARCHAR(20)
    CONSTRAINT chk_phone_format
    CHECK ((phone_e164 IS NULL) OR (phone_e164 ~ '^\+[1-9]\d{7,14}$')),

    preferred_channel VARCHAR(40),

    status            VARCHAR(20)              DEFAULT 'ACTIVE' NOT NULL
    CONSTRAINT chk_customer_status
    CHECK (status = ANY (ARRAY['ACTIVE','SUSPENDED','DELETED'])),

    created_at        TIMESTAMPTZ              DEFAULT NOW() NOT NULL,
    updated_at        TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_customers_tenant_status
    ON ticketing.customers (tenant_id, status);

CREATE INDEX IF NOT EXISTS ix_customers_tenant_created_at
    ON ticketing.customers (tenant_id, created_at);

-- Optional (recommended) uniqueness per tenant
-- CREATE UNIQUE INDEX IF NOT EXISTS uq_customers_tenant_email
--     ON ticketing.customers (tenant_id, lower(email))
--     WHERE email IS NOT NULL;

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.customers REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.customers
(id, tenant_id, customer_code, full_name, email, phone_e164, preferred_channel, status, created_at, updated_at)
VALUES
    (
        'd1e2f3a4-b5c6-4d7e-8f90-1a2b3c4d7001',
        '11111111-1111-1111-1111-111111111111',
        'CUST-001',
        'Kip Kurui',
        'kip@example.com',
        '+254700123456',
        'WEB',
        'ACTIVE',
        NOW(),
        NOW()
    ),
    (
        'd1e2f3a4-b5c6-4d7e-8f90-1a2b3c4d7002',
        '11111111-1111-1111-1111-111111111111',
        'CUST-002',
        'Esther Wanjiku',
        'esther@example.com',
        '+254711654321',
        'WHATSAPP',
        'ACTIVE',
        NOW(),
        NOW()
    );

-- V2__create_tenants.sql
-- Creates: ticketing.tenants
-- Matches TenantEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.tenants
(
    id               UUID                                       NOT NULL
    PRIMARY KEY,

    code             VARCHAR(50)                                NOT NULL
    CONSTRAINT uq_tenants_code UNIQUE,

    name             VARCHAR(200)                               NOT NULL,

    status           VARCHAR(30)                DEFAULT 'ACTIVE'    NOT NULL,
    CONSTRAINT chk_tenants_status
    CHECK (status = ANY (ARRAY['ACTIVE','SUSPENDED','DELETED'])),

    default_timezone VARCHAR(64)                DEFAULT 'Africa/Nairobi' NOT NULL,
    default_locale   VARCHAR(20)                DEFAULT 'en-KE'          NOT NULL,

    created_at       TIMESTAMPTZ                DEFAULT NOW()            NOT NULL,
    updated_at       TIMESTAMPTZ                DEFAULT NOW()            NOT NULL
    );

-- Query helpers
CREATE INDEX IF NOT EXISTS ix_tenants_status
    ON ticketing.tenants (status);

CREATE INDEX IF NOT EXISTS ix_tenants_created_at
    ON ticketing.tenants (created_at);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.tenants REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.tenants
(id, code, name, status, default_timezone, default_locale, created_at, updated_at)
VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'TEKSET',
        'Tekset Global Innovation - Support',
        'ACTIVE',
        'Africa/Nairobi',
        'en-KE',
        NOW(),
        NOW()
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'EQUITY_SUPPORT',
        'Equity Support Portal',
        'ACTIVE',
        'Africa/Nairobi',
        'en-KE',
        NOW(),
        NOW()
    );

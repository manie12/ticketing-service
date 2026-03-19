-- V10__create_business_rules.sql
-- Creates: ticketing.business_rules
-- Matches BusinessRuleEntity columns exactly.

CREATE TABLE IF NOT EXISTS ticketing.business_rules
(
    id             UUID                                   NOT NULL
    PRIMARY KEY,

    tenant_id      UUID
    REFERENCES ticketing.tenants
    ON DELETE RESTRICT,

    name           VARCHAR(200)                           NOT NULL,

    is_enabled     BOOLEAN                  DEFAULT TRUE  NOT NULL,

    priority       INTEGER                  DEFAULT 100   NOT NULL
    CONSTRAINT chk_rule_priority_nonneg
    CHECK (priority >= 0),

    channel_codes  TEXT[],
    category_codes TEXT[],

    match_expr     JSON,
    actions        JSON                                  NOT NULL,

    created_at     TIMESTAMPTZ              DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMPTZ              DEFAULT NOW() NOT NULL
    );

-- Query helpers (rule evaluation)
CREATE INDEX IF NOT EXISTS ix_business_rules_tenant_enabled_priority
    ON ticketing.business_rules (tenant_id, is_enabled, priority);

CREATE INDEX IF NOT EXISTS ix_business_rules_enabled_priority
    ON ticketing.business_rules (is_enabled, priority);

CREATE INDEX IF NOT EXISTS ix_business_rules_created_at
    ON ticketing.business_rules (created_at);

-- For Debezium/logical decoding completeness if updates occur
ALTER TABLE ticketing.business_rules REPLICA IDENTITY FULL;


-- 2 example rows

INSERT INTO ticketing.business_rules
(id, tenant_id, name, is_enabled, priority, channel_codes, category_codes, match_expr, actions, created_at, updated_at)
VALUES
    (
        'c1c7d6c6-8c40-4e38-9d2a-2d6b2a5f1001',
        NULL,
        'GLOBAL_DEFAULT_PRIORITY',
        TRUE,
        100,
        NULL,
        NULL,
        NULL,
        '{"setPriority":"LOW"}'::jsonb,
        NOW(),
        NOW()
    ),
    (
        'c1c7d6c6-8c40-4e38-9d2a-2d6b2a5f1002',
        '11111111-1111-1111-1111-111111111111',
        'WHATSAPP_ACCOUNT_ACCESS_ESCALATE',
        TRUE,
        10,
        ARRAY['WHATSAPP'],
        ARRAY['ACCOUNT_ACCESS'],
        '{"all":[{"field":"ticket.priority","op":"IN","value":["HIGH","URGENT"]}]}'::jsonb,
        '{"assignTeam":"TEAM-SUPPORT-L2","setPriority":"URGENT","addTags":["OTP","LOCKED"]}'::jsonb,
        NOW(),
        NOW()
    );

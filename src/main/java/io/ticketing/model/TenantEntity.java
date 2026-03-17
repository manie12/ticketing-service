package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.tenants
 *
 * DDL (summary):
 * tenants(
 *   id uuid PK,
 *   code varchar(50) UNIQUE NOT NULL,
 *   name varchar(200) NOT NULL,
 *   status varchar(30) NOT NULL default 'ACTIVE' check in (ACTIVE,SUSPENDED,DELETED),
 *   default_timezone varchar(64) NOT NULL default 'Africa/Nairobi',
 *   default_locale varchar(20) NOT NULL default 'en-KE',
 *   created_at timestamptz NOT NULL default now(),
 *   updated_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "tenants")
public class TenantEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("status")
    private String status; // ACTIVE | SUSPENDED | DELETED

    @Column("default_timezone")
    private String defaultTimezone;

    @Column("default_locale")
    private String defaultLocale;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public TenantEntity() {}

    public TenantEntity(UUID id, String code, String name, String status,
                        String defaultTimezone, String defaultLocale,
                        OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.defaultTimezone = defaultTimezone;
        this.defaultLocale = defaultLocale;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TenantEntity newTenant(String code, String name) {
        TenantEntity t = new TenantEntity();
        t.id = UUID.randomUUID();
        t.code = code;
        t.name = name;
        t.status = "ACTIVE";
        t.defaultTimezone = "Africa/Nairobi";
        t.defaultLocale = "en-KE";
        return t;
    }

    public UUID getId() { return id; }
    public TenantEntity setId(UUID id) { this.id = id; return this; }

    public String getCode() { return code; }
    public TenantEntity setCode(String code) { this.code = code; return this; }

    public String getName() { return name; }
    public TenantEntity setName(String name) { this.name = name; return this; }

    public String getStatus() { return status; }
    public TenantEntity setStatus(String status) { this.status = status; return this; }

    public String getDefaultTimezone() { return defaultTimezone; }
    public TenantEntity setDefaultTimezone(String defaultTimezone) { this.defaultTimezone = defaultTimezone; return this; }

    public String getDefaultLocale() { return defaultLocale; }
    public TenantEntity setDefaultLocale(String defaultLocale) { this.defaultLocale = defaultLocale; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TenantEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public TenantEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
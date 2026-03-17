package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.channels
 *
 * DDL (summary):
 * channels(
 *   id uuid PK,
 *   tenant_id uuid FK tenants(id),
 *   code varchar(40) NOT NULL,
 *   name varchar(120) NOT NULL,
 *   is_enabled boolean NOT NULL default true,
 *   config jsonb NULL,
 *   created_at timestamptz NOT NULL default now(),
 *   updated_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "channels")
public class ChannelEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("is_enabled")
    private Boolean isEnabled;

    /**
     * jsonb - keep as String for simplicity (store JSON text).
     * If you want JsonNode, add converters later.
     */
    @Column("config")
    private String config;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public ChannelEntity() {}

    public ChannelEntity(UUID id, UUID tenantId, String code, String name, Boolean isEnabled,
                         String config, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.code = code;
        this.name = name;
        this.isEnabled = isEnabled;
        this.config = config;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ChannelEntity newChannel(UUID tenantId, String code, String name) {
        ChannelEntity c = new ChannelEntity();
        c.id = UUID.randomUUID();
        c.tenantId = tenantId;
        c.code = code;
        c.name = name;
        c.isEnabled = true;
        return c;
    }

    public UUID getId() { return id; }
    public ChannelEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public ChannelEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getCode() { return code; }
    public ChannelEntity setCode(String code) { this.code = code; return this; }

    public String getName() { return name; }
    public ChannelEntity setName(String name) { this.name = name; return this; }

    public Boolean getIsEnabled() { return isEnabled; }
    public ChannelEntity setIsEnabled(Boolean enabled) { isEnabled = enabled; return this; }

    public String getConfig() { return config; }
    public ChannelEntity setConfig(String config) { this.config = config; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public ChannelEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public ChannelEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
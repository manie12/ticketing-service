package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.settings
 *
 * DDL (summary):
 * settings(
 *   id uuid PK,
 *   tenant_id uuid NULL FK tenants(id),
 *   channel_id uuid NULL FK channels(id),
 *   category_id uuid NULL FK categories(id),
 *   key varchar(120) NOT NULL,
 *   value_type varchar(20) NOT NULL default 'STRING' check in (STRING,NUMBER,BOOLEAN,JSON),
 *   value text NULL,
 *   value_json jsonb NULL,
 *   is_active boolean NOT NULL default true,
 *   description varchar(400) NULL,
 *   created_at timestamptz NOT NULL default now(),
 *   updated_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "settings")
public class SettingEntity implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;      // nullable (global)

    @Column("channel_id")
    private UUID channelId;     // nullable

    @Column("category_id")
    private UUID categoryId;    // nullable

    @Column("key")
    private String key;

    @Column("value_type")
    private String valueType;   // STRING | NUMBER | BOOLEAN | JSON

    /**
     * Used when value_type in (STRING, NUMBER, BOOLEAN)
     */
    @Column("value")
    private String value;

    /**
     * jsonb - keep as String for simplicity (store JSON text).
     * If you prefer JsonNode, add converters later.
     */
    @Column("value_json")
    private String valueJson;

    @Column("is_active")
    private Boolean isActive;

    @Column("description")
    private String description;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    @Transient
    private boolean isNew = false;

    public SettingEntity() {}

    public SettingEntity(UUID id, UUID tenantId, UUID channelId, UUID categoryId,
                         String key, String valueType, String value, String valueJson,
                         Boolean isActive, String description,
                         OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.channelId = channelId;
        this.categoryId = categoryId;
        this.key = key;
        this.valueType = valueType;
        this.value = value;
        this.valueJson = valueJson;
        this.isActive = isActive;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Convenient factories
    public static SettingEntity newGlobalString(String key, String value, String description) {
        SettingEntity s = new SettingEntity();
        s.id = UUID.randomUUID();
        s.key = key;
        s.valueType = "STRING";
        s.value = value;
        s.valueJson = null;
        s.isActive = true;
        s.description = description;
        return s;
    }

    public static SettingEntity newTenantString(UUID tenantId, String key, String value, String description) {
        SettingEntity s = new SettingEntity();
        s.id = UUID.randomUUID();
        s.tenantId = tenantId;
        s.key = key;
        s.valueType = "STRING";
        s.value = value;
        s.valueJson = null;
        s.isActive = true;
        s.description = description;
        return s;
    }

    // Getters / Setters
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public SettingEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() { return id; }
    public SettingEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public SettingEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public UUID getChannelId() { return channelId; }
    public SettingEntity setChannelId(UUID channelId) { this.channelId = channelId; return this; }

    public UUID getCategoryId() { return categoryId; }
    public SettingEntity setCategoryId(UUID categoryId) { this.categoryId = categoryId; return this; }

    public String getKey() { return key; }
    public SettingEntity setKey(String key) { this.key = key; return this; }

    public String getValueType() { return valueType; }
    public SettingEntity setValueType(String valueType) { this.valueType = valueType; return this; }

    public String getValue() { return value; }
    public SettingEntity setValue(String value) { this.value = value; return this; }

    public String getValueJson() { return valueJson; }
    public SettingEntity setValueJson(String valueJson) { this.valueJson = valueJson; return this; }

    public Boolean getIsActive() { return isActive; }
    public SettingEntity setIsActive(Boolean active) { isActive = active; return this; }

    public String getDescription() { return description; }
    public SettingEntity setDescription(String description) { this.description = description; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public SettingEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public SettingEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
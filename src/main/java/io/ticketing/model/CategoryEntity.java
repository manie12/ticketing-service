package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.categories
 *
 * DDL (summary):
 * categories(
 *   id uuid PK,
 *   tenant_id uuid NOT NULL FK tenants(id),
 *   code varchar(60) NOT NULL (A-Z0-9_),
 *   name varchar(160) NOT NULL,
 *   description varchar(500) NULL,
 *   is_active boolean NOT NULL default true,
 *   allowed_channel_codes text[] NULL,
 *   created_at timestamptz NOT NULL default now(),
 *   updated_at timestamptz NOT NULL default now(),
 *   unique(tenant_id, code)
 * )
 */
@Table(schema = "ticketing", name = "categories")
public class CategoryEntity implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("is_active")
    private Boolean isActive;

    @Transient
    private boolean isNew = false;
    /**
     * Postgres text[].
     * NOTE: Depending on your R2DBC driver/version, mapping text[] to List<String>
     * may require custom converters. If you hit issues, change this field to String
     * and store comma-separated values.
     */
    @Column("allowed_channel_codes")
    private List<String> allowedChannelCodes;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;


    public CategoryEntity() {}

    public CategoryEntity(UUID id, UUID tenantId, String code, String name, String description,
                          Boolean isActive, List<String> allowedChannelCodes,
                          OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.code = code;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.allowedChannelCodes = allowedChannelCodes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CategoryEntity newCategory(UUID tenantId, String code, String name) {
        CategoryEntity c = new CategoryEntity();
        c.id = UUID.randomUUID();
        c.tenantId = tenantId;
        c.code = code;
        c.name = name;
        c.isActive = true;
        return c;
    }
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public CategoryEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() { return id; }
    public CategoryEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public CategoryEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getCode() { return code; }
    public CategoryEntity setCode(String code) { this.code = code; return this; }

    public String getName() { return name; }
    public CategoryEntity setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public CategoryEntity setDescription(String description) { this.description = description; return this; }

    public Boolean getIsActive() { return isActive; }
    public CategoryEntity setIsActive(Boolean active) { isActive = active; return this; }

    public List<String> getAllowedChannelCodes() { return allowedChannelCodes; }
    public CategoryEntity setAllowedChannelCodes(List<String> allowedChannelCodes) {
        this.allowedChannelCodes = allowedChannelCodes;
        return this;
    }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public CategoryEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public CategoryEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
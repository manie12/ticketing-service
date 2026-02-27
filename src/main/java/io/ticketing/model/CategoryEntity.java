package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: categories
 * (Top-5 addon table used by sla_policies and tickets.category_id)
 */
@Table("categories")
public class CategoryEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("scope")
    private String scope;      // GLOBAL | TENANT

    @Column("tenant_id")
    private UUID tenantId;     // nullable when scope=GLOBAL

    @Column("public_id")
    private String publicId;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("parent_id")
    private UUID parentId;

    @Column("is_active")
    private Boolean isActive;

    @Column("sort_order")
    private Integer sortOrder;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public CategoryEntity() {}

    public CategoryEntity(UUID id, String scope, UUID tenantId, String publicId, String code,
                          String name, String description, UUID parentId, Boolean isActive,
                          Integer sortOrder, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.scope = scope;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.code = code;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CategoryEntity newGlobal(String publicId, String code, String name) {
        CategoryEntity c = new CategoryEntity();
        c.id = UUID.randomUUID();
        c.scope = "GLOBAL";
        c.publicId = publicId;
        c.code = code;
        c.name = name;
        c.isActive = true;
        c.sortOrder = 0;
        return c;
    }

    public static CategoryEntity newTenant(UUID tenantId, String publicId, String code, String name) {
        CategoryEntity c = new CategoryEntity();
        c.id = UUID.randomUUID();
        c.scope = "TENANT";
        c.tenantId = tenantId;
        c.publicId = publicId;
        c.code = code;
        c.name = name;
        c.isActive = true;
        c.sortOrder = 0;
        return c;
    }

    public UUID getId() { return id; }
    public CategoryEntity setId(UUID id) { this.id = id; return this; }

    public String getScope() { return scope; }
    public CategoryEntity setScope(String scope) { this.scope = scope; return this; }

    public UUID getTenantId() { return tenantId; }
    public CategoryEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getPublicId() { return publicId; }
    public CategoryEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getCode() { return code; }
    public CategoryEntity setCode(String code) { this.code = code; return this; }

    public String getName() { return name; }
    public CategoryEntity setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public CategoryEntity setDescription(String description) { this.description = description; return this; }

    public UUID getParentId() { return parentId; }
    public CategoryEntity setParentId(UUID parentId) { this.parentId = parentId; return this; }

    public Boolean getIsActive() { return isActive; }
    public CategoryEntity setIsActive(Boolean active) { isActive = active; return this; }

    public Integer getSortOrder() { return sortOrder; }
    public CategoryEntity setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public CategoryEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public CategoryEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
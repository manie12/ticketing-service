package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: sla_policies
 *
 * DDL recap:
 *  sla_policies(
 *    id uuid PK,
 *    scope text NOT NULL,                  -- GLOBAL|TENANT
 *    tenant_id uuid NULL,                  -- required if scope=TENANT
 *    public_id text UNIQUE NOT NULL,
 *    name text NOT NULL,
 *    description text NULL,
 *    is_active boolean NOT NULL default true,
 *    channel_code text NULL,               -- WEB|EMAIL|WHATSAPP|API|PHONE|IN_APP (NULL=any)
 *    priority text NULL,                   -- LOW|MEDIUM|HIGH|URGENT (NULL=any)
 *    category_id uuid NULL,                -- FK categories(id) optional
 *    first_response_minutes int NOT NULL,
 *    resolve_minutes int NOT NULL,
 *    calendar_ref text NULL,
 *    created_at timestamptz NOT NULL default now(),
 *    updated_at timestamptz NOT NULL default now()
 *  )
 */
@Table("sla_policies")
public class SlaPolicyEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("scope")
    private String scope; // GLOBAL | TENANT

    @Column("tenant_id")
    private UUID tenantId; // nullable when scope=GLOBAL

    @Column("public_id")
    private String publicId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("is_active")
    private Boolean isActive;

    @Column("channel_code")
    private String channelCode; // nullable => any channel

    @Column("priority")
    private String priority; // nullable => any priority

    @Column("category_id")
    private UUID categoryId; // nullable

    @Column("first_response_minutes")
    private Integer firstResponseMinutes;

    @Column("resolve_minutes")
    private Integer resolveMinutes;

    @Column("calendar_ref")
    private String calendarRef;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public SlaPolicyEntity() {}

    public SlaPolicyEntity(UUID id, String scope, UUID tenantId, String publicId, String name,
                           String description, Boolean isActive, String channelCode, String priority,
                           UUID categoryId, Integer firstResponseMinutes, Integer resolveMinutes,
                           String calendarRef, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.scope = scope;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.channelCode = channelCode;
        this.priority = priority;
        this.categoryId = categoryId;
        this.firstResponseMinutes = firstResponseMinutes;
        this.resolveMinutes = resolveMinutes;
        this.calendarRef = calendarRef;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---------- Factory helpers ----------
    public static SlaPolicyEntity newGlobal(String publicId, String name,
                                           String channelCode, String priority,
                                           int firstResponseMinutes, int resolveMinutes) {
        SlaPolicyEntity p = new SlaPolicyEntity();
        p.id = UUID.randomUUID();
        p.scope = "GLOBAL";
        p.tenantId = null;
        p.publicId = publicId;
        p.name = name;
        p.isActive = true;
        p.channelCode = channelCode;
        p.priority = priority;
        p.firstResponseMinutes = firstResponseMinutes;
        p.resolveMinutes = resolveMinutes;
        return p;
    }

    public static SlaPolicyEntity newTenant(UUID tenantId, String publicId, String name,
                                           String channelCode, String priority,
                                           int firstResponseMinutes, int resolveMinutes) {
        SlaPolicyEntity p = new SlaPolicyEntity();
        p.id = UUID.randomUUID();
        p.scope = "TENANT";
        p.tenantId = tenantId;
        p.publicId = publicId;
        p.name = name;
        p.isActive = true;
        p.channelCode = channelCode;
        p.priority = priority;
        p.firstResponseMinutes = firstResponseMinutes;
        p.resolveMinutes = resolveMinutes;
        return p;
    }

    // ---------- Getters / Setters ----------
    public UUID getId() { return id; }
    public SlaPolicyEntity setId(UUID id) { this.id = id; return this; }

    public String getScope() { return scope; }
    public SlaPolicyEntity setScope(String scope) { this.scope = scope; return this; }

    public UUID getTenantId() { return tenantId; }
    public SlaPolicyEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getPublicId() { return publicId; }
    public SlaPolicyEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getName() { return name; }
    public SlaPolicyEntity setName(String name) { this.name = name; return this; }

    public String getDescription() { return description; }
    public SlaPolicyEntity setDescription(String description) { this.description = description; return this; }

    public Boolean getIsActive() { return isActive; }
    public SlaPolicyEntity setIsActive(Boolean active) { isActive = active; return this; }

    public String getChannelCode() { return channelCode; }
    public SlaPolicyEntity setChannelCode(String channelCode) { this.channelCode = channelCode; return this; }

    public String getPriority() { return priority; }
    public SlaPolicyEntity setPriority(String priority) { this.priority = priority; return this; }

    public UUID getCategoryId() { return categoryId; }
    public SlaPolicyEntity setCategoryId(UUID categoryId) { this.categoryId = categoryId; return this; }

    public Integer getFirstResponseMinutes() { return firstResponseMinutes; }
    public SlaPolicyEntity setFirstResponseMinutes(Integer firstResponseMinutes) { this.firstResponseMinutes = firstResponseMinutes; return this; }

    public Integer getResolveMinutes() { return resolveMinutes; }
    public SlaPolicyEntity setResolveMinutes(Integer resolveMinutes) { this.resolveMinutes = resolveMinutes; return this; }

    public String getCalendarRef() { return calendarRef; }
    public SlaPolicyEntity setCalendarRef(String calendarRef) { this.calendarRef = calendarRef; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public SlaPolicyEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public SlaPolicyEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
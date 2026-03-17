package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.business_rules
 *
 * DDL (summary):
 * business_rules(
 *   id uuid PK,
 *   tenant_id uuid NULL FK tenants(id),
 *   name varchar(200) NOT NULL,
 *   is_enabled boolean NOT NULL default true,
 *   priority int NOT NULL default 100 check(priority>=0),
 *   channel_codes text[] NULL,
 *   category_codes text[] NULL,
 *   match_expr jsonb NULL,
 *   actions jsonb NOT NULL,
 *   created_at timestamptz NOT NULL default now(),
 *   updated_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "business_rules")
public class BusinessRuleEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId; // nullable => global rule

    @Column("name")
    private String name;

    @Column("is_enabled")
    private Boolean isEnabled;

    @Column("priority")
    private Integer priority;

    /**
     * Postgres text[].
     * NOTE: Mapping arrays/lists may require converters depending on your R2DBC setup.
     * If you hit issues, change these to String (comma-separated) or use custom converters.
     */
    @Column("channel_codes")
    private List<String> channelCodes;

    @Column("category_codes")
    private List<String> categoryCodes;

    /**
     * jsonb stored as JSON string for simplicity.
     */
    @Column("match_expr")
    private String matchExpr;

    /**
     * jsonb stored as JSON string for simplicity.
     * This is required.
     */
    @Column("actions")
    private String actions;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public BusinessRuleEntity() {}

    public BusinessRuleEntity(UUID id, UUID tenantId, String name, Boolean isEnabled, Integer priority,
                              List<String> channelCodes, List<String> categoryCodes,
                              String matchExpr, String actions,
                              OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.isEnabled = isEnabled;
        this.priority = priority;
        this.channelCodes = channelCodes;
        this.categoryCodes = categoryCodes;
        this.matchExpr = matchExpr;
        this.actions = actions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BusinessRuleEntity newRule(UUID tenantId, String name, int priority, String actionsJson) {
        BusinessRuleEntity r = new BusinessRuleEntity();
        r.id = UUID.randomUUID();
        r.tenantId = tenantId;
        r.name = name;
        r.isEnabled = true;
        r.priority = priority;
        r.actions = actionsJson;
        return r;
    }

    public UUID getId() { return id; }
    public BusinessRuleEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public BusinessRuleEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getName() { return name; }
    public BusinessRuleEntity setName(String name) { this.name = name; return this; }

    public Boolean getIsEnabled() { return isEnabled; }
    public BusinessRuleEntity setIsEnabled(Boolean enabled) { isEnabled = enabled; return this; }

    public Integer getPriority() { return priority; }
    public BusinessRuleEntity setPriority(Integer priority) { this.priority = priority; return this; }

    public List<String> getChannelCodes() { return channelCodes; }
    public BusinessRuleEntity setChannelCodes(List<String> channelCodes) { this.channelCodes = channelCodes; return this; }

    public List<String> getCategoryCodes() { return categoryCodes; }
    public BusinessRuleEntity setCategoryCodes(List<String> categoryCodes) { this.categoryCodes = categoryCodes; return this; }

    public String getMatchExpr() { return matchExpr; }
    public BusinessRuleEntity setMatchExpr(String matchExpr) { this.matchExpr = matchExpr; return this; }

    public String getActions() { return actions; }
    public BusinessRuleEntity setActions(String actions) { this.actions = actions; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public BusinessRuleEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public BusinessRuleEntity setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
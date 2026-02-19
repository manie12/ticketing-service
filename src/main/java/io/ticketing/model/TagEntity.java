package io.ticketing.model;

// ============================================================
// TAGS
// ============================================================
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
@Table("tags")
class TagEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("name")
    private String name;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TagEntity() {}

    public TagEntity(UUID id, UUID tenantId, String name, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static TagEntity newTag(UUID tenantId, String name) {
        TagEntity t = new TagEntity();
        t.id = UUID.randomUUID();
        t.tenantId = tenantId;
        t.name = name;
        return t;
    }

    public UUID getId() { return id; }
    public TagEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public TagEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getName() { return name; }
    public TagEntity setName(String name) { this.name = name; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public TagEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}


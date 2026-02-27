package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC Entity for: tenants
 * <p>
 * Table:
 * tenants(
 * id uuid PK,
 * public_id text UNIQUE NOT NULL,
 * name text NOT NULL,
 * created_at timestamptz NOT NULL default now()
 * )
 */
@Table("tenants")
public class TenantEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("public_id")
    private String publicId;

    @Column("name")
    private String name;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public TenantEntity() {
    }

    public TenantEntity(UUID id, String publicId, String name, OffsetDateTime createdAt) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.createdAt = createdAt;
    }

    // ---------- Factory helpers ----------
    public static TenantEntity newTenant(String publicId, String name) {
        TenantEntity t = new TenantEntity();
        t.id = UUID.randomUUID();
        t.publicId = publicId;
        t.name = name;
        // createdAt is set by @CreatedDate if auditing is enabled; otherwise DB default handles it.
        return t;
    }

    // ---------- Getters / Setters ----------
    public UUID getId() {
        return id;
    }

    public TenantEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public TenantEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getName() {
        return name;
    }

    public TenantEntity setName(String name) {
        this.name = name;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TenantEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
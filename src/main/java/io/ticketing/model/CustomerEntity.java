// ============================================================
// R2DBC ENTITIES (Spring Data R2DBC)
// - Tenants (already done)
// - Customers
// - Tickets
// - TicketMessages
// - TicketAttachments
// - TicketEvents
// - Tags
// - TicketTags
// - Settings (independent scope+tenant_id)
// - Channels (independent scope+tenant_id)
// - BusinessRules (independent scope+tenant_id)
// - Categories (independent scope+tenant_id)
// - SlaPolicies (independent scope+tenant_id)
// - TicketSla
// - TicketParticipants
// - TicketLinks
//
// Notes:
// 1) R2DBC does NOT auto-handle relations like JPA; keep FKs as IDs.
// 2) Use OffsetDateTime for timestamptz.
// 3) For jsonb columns, use String (store JSON) or Json type converters.
// ============================================================

package io.ticketing.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

// ============================================================
// CUSTOMERS
// ============================================================
@Table("customers")
public class CustomerEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("public_id")
    private String publicId;

    @Column("external_customer_id")
    private String externalCustomerId;

    @Column("display_name")
    private String displayName;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    public CustomerEntity() {}

    public CustomerEntity(UUID id, UUID tenantId, String publicId, String externalCustomerId,
                          String displayName, String email, String phone, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.externalCustomerId = externalCustomerId;
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public static CustomerEntity newCustomer(UUID tenantId, String publicId, String displayName) {
        CustomerEntity c = new CustomerEntity();
        c.id = UUID.randomUUID();
        c.tenantId = tenantId;
        c.publicId = publicId;
        c.displayName = displayName;
        return c;
    }

    public UUID getId() { return id; }
    public CustomerEntity setId(UUID id) { this.id = id; return this; }

    public UUID getTenantId() { return tenantId; }
    public CustomerEntity setTenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

    public String getPublicId() { return publicId; }
    public CustomerEntity setPublicId(String publicId) { this.publicId = publicId; return this; }

    public String getExternalCustomerId() { return externalCustomerId; }
    public CustomerEntity setExternalCustomerId(String externalCustomerId) { this.externalCustomerId = externalCustomerId; return this; }

    public String getDisplayName() { return displayName; }
    public CustomerEntity setDisplayName(String displayName) { this.displayName = displayName; return this; }

    public String getEmail() { return email; }
    public CustomerEntity setEmail(String email) { this.email = email; return this; }

    public String getPhone() { return phone; }
    public CustomerEntity setPhone(String phone) { this.phone = phone; return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public CustomerEntity setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
}

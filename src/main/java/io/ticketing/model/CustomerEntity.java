package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.customers
 * <p>
 * DDL (summary):
 * customers(
 * id uuid PK,
 * tenant_id uuid NOT NULL FK tenants(id),
 * customer_code varchar(80) NULL,
 * full_name varchar(200) NOT NULL,
 * email varchar(254) NULL,
 * phone_e164 varchar(20) NULL (E.164 check),
 * preferred_channel varchar(40) NULL,
 * status varchar(20) NOT NULL default 'ACTIVE' check in (ACTIVE,SUSPENDED,DELETED),
 * created_at timestamptz NOT NULL default now(),
 * updated_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "customers")
public class CustomerEntity implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("customer_code")
    private String customerCode;

    @Column("full_name")
    private String fullName;

    @Column("email")
    private String email;

    @Column("phone_e164")
    private String phoneE164;

    @Column("preferred_channel")
    private String preferredChannel;

    @Column("status")
    private String status; // ACTIVE | SUSPENDED | DELETED

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    @Transient
    private boolean isNew = false;

    public CustomerEntity() {
    }

    public CustomerEntity(UUID id, UUID tenantId, String customerCode, String fullName, String email,
                          String phoneE164, String preferredChannel, String status,
                          OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.customerCode = customerCode;
        this.fullName = fullName;
        this.email = email;
        this.phoneE164 = phoneE164;
        this.preferredChannel = preferredChannel;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CustomerEntity newCustomer(UUID tenantId, String fullName) {
        CustomerEntity c = new CustomerEntity();
        c.id = UUID.randomUUID();
        c.tenantId = tenantId;
        c.fullName = fullName;
        c.status = "ACTIVE";
        return c;
    }
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public CustomerEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() {
        return id;
    }

    public CustomerEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public CustomerEntity setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public CustomerEntity setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public CustomerEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneE164() {
        return phoneE164;
    }

    public CustomerEntity setPhoneE164(String phoneE164) {
        this.phoneE164 = phoneE164;
        return this;
    }

    public String getPreferredChannel() {
        return preferredChannel;
    }

    public CustomerEntity setPreferredChannel(String preferredChannel) {
        this.preferredChannel = preferredChannel;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CustomerEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public CustomerEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CustomerEntity setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
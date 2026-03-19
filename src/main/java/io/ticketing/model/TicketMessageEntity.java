package io.ticketing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * R2DBC entity for ticketing.ticket_messages
 * <p>
 * DDL (summary):
 * ticket_messages(
 * id uuid PK,
 * tenant_id uuid NOT NULL FK tenants(id),
 * ticket_id uuid NOT NULL FK tickets(id) ON DELETE CASCADE,
 * sender_type varchar(20) NOT NULL check in (CUSTOMER,AGENT,SYSTEM),
 * sender_id uuid NULL,
 * message_type varchar(30) NOT NULL default TEXT check in (TEXT,DESCRIPTION,SYSTEM_NOTE),
 * body text NOT NULL,
 * channel_id uuid NULL FK channels(id),
 * created_at timestamptz NOT NULL default now()
 * )
 */
@Table(schema = "ticketing", name = "ticket_messages")
public class TicketMessageEntity implements org.springframework.data.domain.Persistable<UUID>{

    @Id
    @Column("id")
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("ticket_id")
    private UUID ticketId;

    @Column("sender_type")
    private String senderType; // CUSTOMER | AGENT | SYSTEM

    @Column("sender_email")
    private String senderEmail;     // nullable

    @Column("message_type")
    private String messageType; // TEXT | DESCRIPTION | SYSTEM_NOTE

    @Column("body")
    private String body;

    @Column("channel_code")
    private String channelCode;    // nullable

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Transient
    private boolean isNew = false;

    public TicketMessageEntity() {
    }

    public TicketMessageEntity(UUID id, UUID tenantId, UUID ticketId, String senderType, String senderEmail,
                               String messageType, String body, String channelCode, OffsetDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.senderType = senderType;
        this.senderEmail = senderEmail;
        this.messageType = messageType;
        this.body = body;
        this.channelCode = channelCode;
        this.createdAt = createdAt;
    }

    public static TicketMessageEntity newCustomerDescription(UUID tenantId, UUID ticketId, String senderEmail, String channelCode, String body) {
        TicketMessageEntity m = new TicketMessageEntity();
        m.id = UUID.randomUUID();
        m.tenantId = tenantId;
        m.ticketId = ticketId;
        m.senderType = "";
        m.senderEmail = senderEmail;
        m.messageType = "DESCRIPTION";
        m.body = body;
        m.channelCode = channelCode;
        return m;
    }

    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    public TicketMessageEntity markNew() {
        this.isNew = true;
        return this;
    }
    public UUID getId() {
        return id;
    }

    public TicketMessageEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public TicketMessageEntity setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public TicketMessageEntity setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public String getSenderType() {
        return senderType;
    }

    public TicketMessageEntity setSenderType(String senderType) {
        this.senderType = senderType;
        return this;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public TicketMessageEntity setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public String getMessageType() {
        return messageType;
    }

    public TicketMessageEntity setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getBody() {
        return body;
    }

    public TicketMessageEntity setBody(String body) {
        this.body = body;
        return this;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public TicketMessageEntity setChannelCode(String channelCode) {
        this.channelCode = channelCode;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TicketMessageEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
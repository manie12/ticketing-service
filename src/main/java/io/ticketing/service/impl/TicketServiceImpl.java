package io.ticketing.service.impl;

import io.ticketing.config.ReactiveTx;
import io.ticketing.datatype.TicketErrorType;
import io.ticketing.datatype.status.MessageType;
import io.ticketing.datatype.status.ParticipantType;
import io.ticketing.datatype.status.TicketStatus;
import io.ticketing.dto.Ticket.TicketRequest;
import io.ticketing.dto.Ticket.TicketResponse;
import io.ticketing.dto.attachments.Attachment;
import io.ticketing.dto.category.Category;
import io.ticketing.dto.channel.Channel;
import io.ticketing.dto.customer.Customer;
import io.ticketing.exception.TicketException;
import io.ticketing.model.*;
import io.ticketing.repository.*;
import io.ticketing.service.TicketService;
import io.ticketing.util.SharedUtils;
import io.ticketing.util.Validators;
import io.ticketing.web.response.HttpResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final ReactiveTx tx;
    private final SharedUtils sharedUtils;
    private final Validators validators;

    private final TicketRepository ticketRepository;
    private final TicketMessageRepository ticketMessageRepository;
    private final TicketAttachmentRepository ticketAttachmentRepository;
    private final TicketParticipantRepository ticketParticipantRepository;
    private final TicketEventRepository ticketEventRepository;
    private final OutboxEventRepository outboxEventRepository;

    public TicketServiceImpl(
            ReactiveTx tx,
            SharedUtils sharedUtils,
            Validators validators,
            TicketRepository ticketRepository,
            TicketMessageRepository ticketMessageRepository,
            TicketAttachmentRepository ticketAttachmentRepository,
            TicketParticipantRepository ticketParticipantRepository,
            TicketEventRepository ticketEventRepository,
            OutboxEventRepository outboxEventRepository,
            ) {
        this.tx = tx;
        this.sharedUtils = sharedUtils;
        this.validators = validators;
        this.ticketRepository = ticketRepository;
        this.ticketMessageRepository = ticketMessageRepository;
        this.ticketAttachmentRepository = ticketAttachmentRepository;
        this.ticketParticipantRepository = ticketParticipantRepository;
        this.ticketEventRepository = ticketEventRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    /**
     * Step 1: Create ticket + message + attachments + participants + events + outbox_event in ONE transaction.
     * <p>
     * Idempotency:
     * - tickets has unique(tenant_id, channel_id, request_id)
     * - We "insert-first". If duplicate, we fetch existing ticket and return it.
     * <p>
     * Transaction isolation:
     * - Uses READ_COMMITTED (explicit) via tx.withDefinition(...)
     */
    @Override
    public Mono<TicketResponse> createTicket(String requestId,
                                             UUID tenantId,
                                             Channel channel,
                                             Customer customer,
                                             Category category,
                                             TicketRequest request) {

        return tx.withDefinition(
                        ReactiveTx.readCommitted(),
                        () -> doCreateInTx(requestId, tenantId, customer, channel, category, request)
                )
                .onErrorResume(this::isDuplicateRequest, ex ->
                        ticketRepository.existsByTenantIdAndChannelCodeAndRequestId(tenantId, channel.getCode(), requestId)
                                .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.DUPLICATE_REQUEST)))
                                .flatMap(t -> toCreateTicketResponse(t, requestId, channel, customer, category, request))
                );
    }

    private Mono<TicketResponse> doCreateInTx(
            String requestId,
            UUID tenantId,
            Customer customer,
            Channel channel,
            Category category,
            TicketRequest request
    ) {
        final OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        Mono<String> priorityMono = validators.validatePriorityIfPresent(request.getTicket().getPriority())
                .then(Mono.justOrEmpty(request.getTicket().getPriority()))
                .map(p -> p.trim().toUpperCase())
                .switchIfEmpty(Mono.just("LOW"));

        return priorityMono
                .flatMap(priorityRes -> {

                    UUID ticketId = this.sharedUtils.generateTicketId();
                    String publicId = this.sharedUtils.generatePublicId(now);

                    // 1) tickets
                    TicketEntity ticket = new TicketEntity()
                            .setId(ticketId)
                            .setTenantId(tenantId)
                            .setPublicId(publicId)
                            .setRequestId(requestId)
                            .setCustomerEmail(customer.getEmail())
                            .setChannelCode(channel.getCode())
                            .setCategoryCode(category != null ? category.getCode() : null)
                            .setPriority(priorityRes)
                            .setSubject(request.getTicket().getSubject())
                            .setStatus(TicketStatus.CREATED.name())
                            .setOpenedAt(now)
                            .setUpdatedAt(now);
                    Mono<TicketEntity> savedTicket = ticketRepository.save(ticket);

                    // 2) ticket_messages
                    Mono<TicketMessageEntity> savedMessage = savedTicket.flatMap(t -> {
                        TicketMessageEntity m = new TicketMessageEntity()
                                .setId(UUID.randomUUID())
                                .setTenantId(tenantId)
                                .setTicketId(t.getId())
                                .setSenderType(TicketStatus.CREATED.name())
                                .setSenderEmail(customer.getEmail())
                                .setMessageType(MessageType.DESCRIPTION.name())
                                .setBody(request.getTicket().getDescription())
                                .setChannelCode(channel.getCode())
                                .setCreatedAt(now);

                        return ticketMessageRepository.save(m);
                    });

                    // 3) ticket_attachments (optional)
                    Mono<Void> savedAttachments = savedMessage.flatMap(msg -> {
                        List<Attachment> attachments = request.getAttachments();
                        if (attachments == null || attachments.isEmpty()) {
                            return Mono.empty();
                        }

                        return Flux.fromIterable(attachments)
                                .map(a -> new TicketAttachmentEntity()
                                        .setId(UUID.randomUUID())
                                        .setTenantId(tenantId)
                                        .setTicketId(ticketId)
                                        .setMessageId(msg.getId())
                                        .setFileName(a.getFileName())
                                        .setContentType(a.getContentType())
                                        .setFileSizeBytes(a.getFileSizeBytes())
                                        .setStorageProvider(a.getStorageProvider())
                                        .setStoragePath(a.getTempStoragePath()) // MVP: temp as final path
                                        .setChecksumSha256(a.getChecksumSha256())
                                        .setCreatedAt(now)
                                )
                                .flatMap(entity -> {
                                    entity.setCustomerEmail(customer.getEmail());
                                    return ticketAttachmentRepository.save(entity);
                                })
                                .then();
                    });

                    // 4) ticket_participants
                    Mono<TicketParticipantEntity> savedParticipant = savedTicket.flatMap(t -> {
                        TicketParticipantEntity p = new TicketParticipantEntity()
                                .setTenantId(tenantId)
                                .setTicketId(t.getId())
                                .setParticipantType(ParticipantType.CUSTOMER.name())
                                .setCustomerEmail(customer.getEmail())
                                .setRole("REQUESTER")
                                .setIsPrimary(true)
                                .setAddedAt(now);
                        return ticketParticipantRepository.save(p);
                    });

                    // 5) ticket_events
                    Mono<TicketEventEntity> savedEvent = savedTicket.flatMap(t -> {
                        String meta = sharedUtils.toJson(
                                java.util.Map.of(
                                        "channelCode", channel.getCode(),
                                        "categoryCode", category.getCode() != null ? category.getCode() : "",
                                        "priority", priorityRes
                                ),
                                false
                        );

                        TicketEventEntity e = new TicketEventEntity()
                                .setId(UUID.randomUUID())
                                .setTenantId(tenantId)
                                .setTicketId(t.getId())
                                .setEventType(TicketStatus.CREATED.name())
                                .setEventCategory(category.getCode())
                                .setFromStatus(null)
                                .setToStatus(TicketStatus.CREATED.name())
                                .setActorType(ParticipantType.CUSTOMER.name())
                                .setCustomerEmail(customer.getEmail())
                                .setMeta(meta)
                                .setOccurredAt(now)
                                .setCreatedAt(now);

                        return ticketEventRepository.save(e);
                    });

                    // 6) outbox_event (TicketCreated)
                    Mono<OutboxEventEntity> savedOutbox = savedTicket.flatMap(t -> {
                        String payload = sharedUtils.toJson(
                                java.util.Map.of(
                                        "tenantId", tenantId.toString(),
                                        "ticketId", t.getId().toString(),
                                        "publicId", t.getPublicId(),
                                        "customerId", customerId.toString(),
                                        "channelCode", request.getChannel().getCode(),
                                        "categoryCode", request.getTicket().getCategoryCode(),
                                        "priority", priority,
                                        "subject", request.getTicket().getSubject(),
                                        "occurredAt", now.toString()
                                ),
                                false
                        );

                        OutboxEventEntity out = new OutboxEventEntity()
                                .setId(UUID.randomUUID())
                                .setTenantId(tenantId)
                                .setAggregateType("ticket")
                                .setAggregateId(t.getId().toString())
                                .setEventType("TicketCreated")
                                .setEventVersion(1)
                                .setPayload(payload)
                                .setCorrelationId(requestId)
                                .setOccurredAt(now)
                                .setCreatedAt(now);

                        return outboxEventRepository.save(out);
                    });

                    return savedTicket
                            .flatMap(savedMessage::thenReturn)
                            .flatMap(savedAttachments::thenReturn)
                            .flatMap(savedParticipant::thenReturn)
                            .flatMap(savedEvent::thenReturn)
                            .flatMap(savedOutbox::thenReturn)
                            .map(t -> toCreateTicketResponse(t, requestId, channel, customer, category, request));
                });
    }


    private boolean isDuplicateRequest(Throwable ex) {
        return ex instanceof DataIntegrityViolationException
                || (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("duplicate"));
    }

    private TicketResponse toCreateTicketResponse(
            TicketEntity t,
            String requestId,
            Channel channel,
            Customer customer,
            Category category,
            TicketRequest request
    ) {
        TicketResponse res = new TicketResponse();

        // Identity
        res.setId(t.getId());
        res.setTenantId(t.getTenantId());
        res.setPublicId(t.getPublicId());
        res.setRequestId(requestId);

        // Parties
        res.setCustomer(customer);
        res.setChannel(channel);

        // Snapshot state
        res.setStatus(t.getStatus());
        res.setPriority(t.getPriority());

        // Classification
        res.setCategory(category); // nullable OK

        // Content
        res.setSubject(t.getSubject());
        res.setDescription(t.getDescription());

        // Assignment
        res.setAssigneeRef(t.getAssigneeRef());

        // Timestamps
        res.setOpenedAt(t.getOpenedAt());
        res.setClosedAt(t.getClosedAt());
        res.setUpdatedAt(t.getUpdatedAt());

        return res;
    }
}
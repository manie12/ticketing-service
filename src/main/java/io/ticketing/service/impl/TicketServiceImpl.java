package io.ticketing.service.impl;

import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;
import java.time.Duration;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Slf4j
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
            OutboxEventRepository outboxEventRepository
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

    @Override
    public Mono<TicketResponse> createTicket(String requestId,
                                             UUID tenantId,
                                             Channel channel,
                                             Customer customer,
                                             Category category,
                                             TicketRequest request) {

        final long startNs = System.nanoTime();

        // Safe, minimal request summary (avoid dumping full description/payload)
        final String channelCode = channel != null ? channel.getCode() : null;
        final String customerEmail = customer != null ? customer.getEmail() : null;
        final String categoryCode = category != null ? category.getCode() : null;
        final String subject = (request != null && request.getTicket() != null) ? request.getTicket().getSubject() : null;
        final int attachmentsCount = (request != null && request.getAttachments() != null) ? request.getAttachments().size() : 0;

        log.info("Ticket create request received [requestId={}, tenantId={}, channelCode={}, customerEmail={}, categoryCode={}, attachmentsCount={}, subject={}]",
                requestId, tenantId, channelCode, customerEmail, categoryCode, attachmentsCount, subject);

        return tx.withDefinition(
                        ReactiveTx.readCommitted(),
                        () -> doCreateInTx(requestId, tenantId, customer, channel, category, request)
                )
                .doOnSubscribe(s -> log.info("Ticket create TX started [requestId={}, tenantId={}, channelCode={}]",
                        requestId, tenantId, channelCode))
                .doOnSuccess(resp -> {
                    long tookMs = Duration.ofNanos(System.nanoTime() - startNs).toMillis();
                    String ticketId = (resp != null && resp.getId() != null) ? resp.getId().toString() : null;
                    String publicId = (resp != null) ? resp.getPublicId() : null;
                    log.info("Ticket create succeeded [requestId={}, tenantId={}, ticketId={}, publicId={}, status={}, tookMs={}]",
                            requestId, tenantId, ticketId, publicId, resp != null ? resp.getStatus() : null, tookMs);
                }).cache()
                .doOnError(ex -> {
                    long tookMs = Duration.ofNanos(System.nanoTime() - startNs).toMillis();
                    log.error("Ticket create failed [requestId={}, tenantId={}, channelCode={}, tookMs={}, errorClass={}, error={}]",
                            requestId, tenantId, channelCode, tookMs, ex.getClass().getName(), ex.getMessage(), ex);
                })
                .onErrorResume(this::isDuplicateRequest, ex -> {
                    log.warn("Duplicate ticket create detected, returning existing ticket [requestId={}, tenantId={}, channelCode={}, reason={}]",
                            requestId, tenantId, channelCode, ex.getMessage());

                    return ticketRepository.findByTenantIdAndChannelCodeAndRequestId(tenantId, channelCode, requestId)
                            .switchIfEmpty(Mono.error(TicketException.of(TicketErrorType.DUPLICATE_REQUEST)))
                            .doOnNext(t -> log.info("Duplicate ticket resolved to existing row [requestId={}, tenantId={}, ticketId={}, publicId={}]",
                                    requestId, tenantId, t.getId(), t.getPublicId()))
                            .map(t -> toCreateTicketResponse(t, requestId, channel, customer, category, request));
                });
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
                .doOnNext(p -> log.info("Resolved priority [requestId={}, tenantId={}, priority={}]", requestId, tenantId, p))
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
                            .setStatus(TicketStatus.OPEN.name())
                            .setOpenedAt(now)
                            .setUpdatedAt(now)
                            .markNew();
                    Mono<TicketEntity> savedTicket = ticketRepository.save(ticket)
                            .doOnSubscribe(s -> log.info("DB write: tickets insert started [requestId={}, tenantId={}, ticketId={}, publicId={}]",
                                    requestId, tenantId, ticketId, publicId))
                            .doOnSuccess(t -> log.info("DB write: tickets insert OK [requestId={}, tenantId={}, ticketId={}, publicId={}]",
                                    requestId, tenantId, t != null ? t.getId() : null, t != null ? t.getPublicId() : null)).cache();

                    // 2) ticket_messages
                    Mono<TicketMessageEntity> savedMessage = savedTicket.flatMap(t -> {
                        TicketMessageEntity m = new TicketMessageEntity()
                                .setId(UUID.randomUUID())
                                .setTenantId(tenantId)
                                .setTicketId(t.getId())
                                .setSenderType(ParticipantType.CUSTOMER.name())
                                .setSenderEmail(customer.getEmail())
                                .setMessageType(MessageType.DESCRIPTION.name())
                                .setBody(request.getTicket().getDescription())
                                .setChannelCode(channel.getCode())
                                .setCreatedAt(now)
                                .markNew();

                        return ticketMessageRepository.save(m)
                                .doOnSubscribe(s -> log.info("DB write: ticket_messages insert started [requestId={}, tenantId={}, ticketId={}, messageId={}]",
                                        requestId, tenantId, t.getId(), m.getId()))
                                .doOnSuccess(x -> log.info("DB write: ticket_messages insert OK [requestId={}, tenantId={}, ticketId={}, messageId={}]",
                                        requestId, tenantId, t.getId(), m.getId())).cache();
                    });

                    // 3) ticket_attachments (optional)
                    Mono<Void> savedAttachments = savedMessage.flatMap(msg -> {
                        List<Attachment> attachments = request.getAttachments();
                        if (attachments == null || attachments.isEmpty()) {
                            log.debug("No attachments to persist [requestId={}, tenantId={}, ticketId={}]", requestId, tenantId, msg.getTicketId());
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
                                        .setStoragePath(a.getTempStoragePath())
                                        .setChecksumSha256(a.getChecksumSha256())
                                        .setCreatedAt(now)
                                        .markNew()
                                )
                                .flatMap(entity -> {
                                    entity.setCustomerEmail(customer.getEmail());
                                    return ticketAttachmentRepository.save(entity)
                                            .doOnSubscribe(s -> log.info("DB write: ticket_attachments insert started [requestId={}, tenantId={}, ticketId={}, attachmentId={}, fileName={}, sizeBytes={}]",
                                                    requestId, tenantId, entity.getTicketId(), entity.getId(), entity.getFileName(), entity.getFileSizeBytes()))
                                            .doOnSuccess(x -> log.info("DB write: ticket_attachments insert OK [requestId={}, tenantId={}, ticketId={}, attachmentId={}]",
                                                    requestId, tenantId, entity.getTicketId(), entity.getId())).cache();
                                })
                                .then();
                    });

                    // 4) ticket_participants
//                    Mono<TicketParticipantEntity> savedParticipant = savedTicket.flatMap(t -> {
//                        TicketParticipantEntity p = new TicketParticipantEntity()
//                                .setTenantId(tenantId)
//                                .setTicketId(t.getId())
//                                .setParticipantType(ParticipantType.CUSTOMER.name())
//                                .setCustomerEmail(customer.getEmail())
//                                .setRole("REQUESTER")
//                                .setIsPrimary(true)
//                                .setAddedAt(now);
//                        return ticketParticipantRepository.save(p)
//                                .doOnSubscribe(s -> log.info("DB write: ticket_participants insert started [requestId={}, tenantId={}, ticketId={}, participantType={}, role={}]",
//                                        requestId, tenantId, t.getId(), p.getParticipantType(), p.getRole()))
//                                .doOnSuccess(x -> log.info("DB write: ticket_participants insert OK [requestId={}, tenantId={}, ticketId={}]",
//                                        requestId, tenantId, t.getId()));
//                    });

                    // 5) ticket_events
                    Mono<TicketEventEntity> savedEvent = savedTicket.flatMap(t -> {
                        String meta = sharedUtils.toJson(
                                java.util.Map.of(
                                        "channelCode", t.getChannelCode(),
                                        "categoryCode", t.getCategoryCode() != null ? t.getCategoryCode() : "",
                                        "priority", t.getPriority()
                                ),
                                false
                        );

                        TicketEventEntity e = new TicketEventEntity()
                                .setId(UUID.randomUUID())
                                .setTenantId(tenantId)
                                .setTicketId(t.getId())
                                .setEventType(TicketStatus.OPEN.name())
                                .setEventCategory(category != null ? category.getCode() : null)
                                .setFromStatus(null)
                                .setToStatus(TicketStatus.OPEN.name())
                                .setActorType(ParticipantType.CUSTOMER.name())
                                .setCustomerEmail(customer.getEmail())
                                .setMeta(meta)
                                .setOccurredAt(now)
                                .setCreatedAt(now)
                                .markNew();

                        return ticketEventRepository.save(e)
                                .doOnSubscribe(s -> log.info("DB write: ticket_events insert started [requestId={}, tenantId={}, ticketId={}, eventType={}]",
                                        requestId, tenantId, t.getId(), e.getEventType()))
                                .doOnSuccess(x -> log.info("DB write: ticket_events insert OK [requestId={}, tenantId={}, ticketId={}, eventType={}]",
                                        requestId, tenantId, t.getId(), e.getEventType())).cache();
                    });

                    // 6) outbox_event (TicketCreated) - Mapped from the persisted TicketEntity 't'
                    Mono<OutboxEvent> savedOutbox = savedTicket.flatMap(t -> {
                        String payload = sharedUtils.toJson(
                                java.util.Map.ofEntries(
                                        java.util.Map.entry("id", t.getId().toString()),
                                        java.util.Map.entry("tenantId", t.getTenantId().toString()),
                                        java.util.Map.entry("publicId", t.getPublicId()),
                                        java.util.Map.entry("requestId", t.getRequestId()),
                                        java.util.Map.entry("customerEmail", t.getCustomerEmail()),
                                        java.util.Map.entry("channelCode", t.getChannelCode()),
                                        java.util.Map.entry("categoryCode", t.getCategoryCode() != null ? t.getCategoryCode() : ""),
                                        java.util.Map.entry("priority", t.getPriority()),
                                        java.util.Map.entry("subject", t.getSubject()),
                                        java.util.Map.entry("status", t.getStatus()),
                                        java.util.Map.entry("openedAt", t.getOpenedAt().toString())
                                ),
                                false
                        );
                        OutboxEvent out = new OutboxEvent()
                                .setId(UUID.randomUUID())
                                .setTenantId(t.getTenantId())
                                .setAggregateType("TICKET")
                                .setAggregateId(t.getId().toString())
                                .setEventType(TicketStatus.OPEN.name())
                                .setEventVersion(1)
                                .setPayload(payload)
                                .setCorrelationId(t.getRequestId())
                                .setOccurredAt(t.getOpenedAt()).markNew();

                        return outboxEventRepository.save(out)
                                .doOnSubscribe(s -> log.info("DB write: outbox_event insert started [requestId={}, tenantId={}, aggregateId={}, eventType={}]",
                                        requestId, t.getTenantId(), out.getAggregateId(), out.getEventType()))
                                .doOnSuccess(x -> log.info("DB write: outbox_event insert OK [requestId={}, tenantId={}, outboxId={}, aggregateId={}, eventType={}]",
                                        requestId, t.getTenantId(), out.getId(), out.getAggregateId(), out.getEventType())).cache();
                    });

                    // Final chain execution ensures all steps complete within the TX
                    return savedTicket
                            .flatMap(t -> savedMessage
                                    .then(savedAttachments)
//                                    .then(savedParticipant)
                                    .then(savedEvent)
                                    .then(savedOutbox)
                                    .thenReturn(t)
                            )
                            .doOnSuccess(t0 -> log.info("Ticket create TX completed (all writes done) [requestId={}, tenantId={}, ticketId={}, publicId={}]",
                                    requestId, tenantId, t0 != null ? t0.getId() : null, t0 != null ? t0.getPublicId() : null))
                            .map(t0 -> toCreateTicketResponse(t0, requestId, channel, customer, category, request));
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
        res.setId(t.getId());
        res.setTenantId(t.getTenantId());
        res.setPublicId(t.getPublicId());
        res.setRequestId(requestId);
        res.setCustomer(customer);
        res.setChannel(channel);
        res.setStatus(t.getStatus());
        res.setPriority(t.getPriority());
        res.setCategory(category);
        res.setSubject(t.getSubject());
        res.setDescription(request.getTicket().getDescription());
        res.setOpenedAt(t.getOpenedAt());
        res.setUpdatedAt(t.getUpdatedAt());
        return res;
    }
}
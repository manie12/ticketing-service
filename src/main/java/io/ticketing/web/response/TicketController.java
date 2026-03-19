package io.ticketing.web.response;

import io.ticketing.datatype.TicketErrorType;
import io.ticketing.dto.Ticket.TicketRequest;
import io.ticketing.dto.Ticket.TicketResponse;
import io.ticketing.service.TicketService;
import io.ticketing.util.SharedUtils;
import io.ticketing.util.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * TicketController (WebFlux)
 * <p>
 * - Uses @Valid for Jakarta validation (handled by GlobalExceptionHandler)
 * - Uses Validators for business validations (throws TicketException)
 * - Returns HttpResponse<T> envelope (HTTP 200 style)
 * <p>
 * Endpoints:
 * - POST /api/v1/tickets  -> create ticket (Step 1)
 * <p>
 * NOTE:
 * This controller wires the flow and shows where validations happen.
 * Replace TicketService.create(...) implementation with your DB transaction logic.
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private final TicketService ticketService;
    private final Validators validators;
    private final SharedUtils sharedUtils;

    public TicketController(TicketService ticketService, Validators validators, SharedUtils sharedUtils) {
        this.ticketService = ticketService;
        this.validators = validators;
        this.sharedUtils = sharedUtils;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<HttpResponse<TicketResponse>>> createTicket(
            @RequestBody @jakarta.validation.Valid TicketRequest request
    ) {
        log.info("[createTicket] Received create ticket request: customerEmail={}, channelCode={}, categoryCode={}, priority={}",
                request.getCustomer() != null ? request.getCustomer().getEmail() : null,
                request.getChannel() != null ? request.getChannel().getCode() : null,
                request.getTicket() != null ? request.getTicket().getCategoryCode() : null,
                request.getTicket() != null ? request.getTicket().getPriority() : null);

        String requestId = (request.getRequestId() != null && !request.getRequestId().isBlank())
                ? request.getRequestId()
                : UUID.randomUUID().toString();
        log.info("[createTicket] Resolved requestId={}", requestId);

        UUID tenantId = this.sharedUtils.parseUuidOrThrow(request.getTenantId(), TicketErrorType.TENANT_NOT_FOUND);
        log.info("[createTicket] Parsed tenantId={}", tenantId);

        String customerEmail = request.getCustomer().getEmail();
        String channelCode = request.getChannel().getCode();
        String categoryCode = request.getTicket().getCategoryCode();
        String priority = request.getTicket().getPriority();
        var attachments = request.getAttachments();

        log.info("[createTicket] Starting validation pipeline: tenantId={}, requestId={}, channelCode={}, customerEmail={}, categoryCode={}, priority={}, attachmentsCount={}",
                tenantId, requestId, channelCode, customerEmail, categoryCode, priority,
                attachments != null ? attachments.size() : 0);

        return validators.validateTenant(tenantId)
                .doOnSuccess(v -> log.info("[createTicket] Tenant validated: tenantId={}", tenantId))
                .doOnError(e -> log.info("[createTicket] Tenant validation failed: tenantId={}, error={}", tenantId, e.getMessage()))
                .then(validators.validateChannel(tenantId, channelCode))
                .doOnSuccess(channel -> log.info("[createTicket] Channel validated: channelCode={}, channelId={}", channelCode, channel != null ? channel.getCode() : null))
                .doOnError(e -> log.info( "[createTicket] Channel validation failed: channelCode={}, error={}", channelCode, e.getMessage()))
                .flatMap(channel -> validators.validateRequestIdUniqueness(tenantId, channel.getCode(), requestId)
                        .doOnSuccess(v -> log.info("[createTicket] RequestId uniqueness validated: requestId={}", requestId))
                        .doOnError(e -> log.info("[createTicket] RequestId uniqueness check failed: requestId={}, error={}", requestId, e.getMessage()))
                        .then(validators.validateCustomer(tenantId, customerEmail))
                        .doOnSuccess(customer -> log.info("[createTicket] Customer validated: customerEmail={}, customerId={}", customerEmail, customer != null ? customer.getId() : null))
                        .doOnError(e -> log.info("[createTicket] Customer validation failed: customerEmail={}, error={}", customerEmail, e.getMessage()))
                        .flatMap(customer -> validators.validateCategoryIfPresent(tenantId, categoryCode, channelCode)
                                .doOnSuccess(category -> log.info("[createTicket] Category validated: categoryCode={}", categoryCode))
                                .doOnError(e -> log.info("[createTicket] Category validation failed: categoryCode={}, error={}", categoryCode, e.getMessage()))
                                .flatMap(category -> validators.validatePriorityIfPresent(priority)
                                        .doOnSuccess(v -> log.info("[createTicket] Priority validated: priority={}", priority))
                                        .doOnError(e -> log.info("[createTicket] Priority validation failed: priority={}, error={}", priority, e.getMessage()))
                                        .then(validators.validateAttachmentsIfPresent(attachments))
                                        .doOnSuccess(v -> log.info("[createTicket] Attachments validated: count={}", attachments != null ? attachments.size() : 0))
                                        .doOnError(e -> log.info("[createTicket] Attachments validation failed: error={}", e.getMessage()))
                                        .then(ticketService.createTicket(requestId, tenantId, channel, customer, category, request))
                                        .doOnSuccess(response -> log.info("[createTicket] Ticket created successfully: requestId={}, tenantId={}, publicId={}",
                                                requestId, tenantId, response != null ? response.getPublicId() : null))
                                        .doOnError(e -> log.info("[createTicket] Ticket creation failed: requestId={}, tenantId={}, error={}", requestId, tenantId, e.getMessage()))
                                )
                        )
                )
                .map(data -> {
                    log.info("[createTicket] Returning success response: requestId={}", requestId);
                    return ResponseEntity.ok(ResponseFactory.ok(requestId, data));
                });
    }
}

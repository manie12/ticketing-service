package io.ticketing.web.response;

import io.ticketing.datatype.TicketErrorType;
import io.ticketing.dto.Ticket.TicketRequest;
import io.ticketing.dto.Ticket.TicketResponse;
import io.ticketing.service.TicketService;
import io.ticketing.util.SharedUtils;
import io.ticketing.util.Validators;
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
        String requestId = (request.getRequestId() != null && !request.getRequestId().isBlank())
                ? request.getRequestId()
                : UUID.randomUUID().toString();

        UUID tenantId = this.sharedUtils.parseUuidOrThrow(request.getTenantId(), TicketErrorType.TENANT_NOT_FOUND);
        String customerEmail = request.getCustomer().getEmail();

        String channelCode = request.getChannel().getCode();
        String categoryCode = request.getTicket().getCategoryCode();
        String priority = request.getTicket().getPriority();
        var attachments = request.getAttachments();

        return validators.validateTenant(tenantId)
                .then(validators.validateChannel(tenantId, channelCode))
                .flatMap(channel -> validators.validateRequestIdUniqueness(tenantId, channel.getCode(), requestId)
                        .then(validators.validateCustomer(tenantId, customerEmail))
                        .flatMap(customer -> validators.validateCategoryIfPresent(tenantId, categoryCode, channelCode)
                                .flatMap(category -> validators.validatePriorityIfPresent(priority)
                                        .then(validators.validateAttachmentsIfPresent(attachments))
                                        .then(ticketService.createTicket(requestId, tenantId, channel, customer, category, request))
                                )
                        )
                )
                .map(data -> ResponseEntity.ok(ResponseFactory.ok(requestId, data)));
    }


}
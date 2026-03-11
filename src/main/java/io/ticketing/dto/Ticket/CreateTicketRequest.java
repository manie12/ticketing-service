package io.ticketing.dto.Ticket;

import io.ticketing.dto.attachments.Attachment;
import io.ticketing.dto.channel.Channel;
import io.ticketing.dto.clientMeta.ClientMeta;
import io.ticketing.dto.customer.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create Ticket request payload (Step 1 entry point).
 * Designed to match the JSON you provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequest {

    @NotBlank
    private String requestId;

    @NotBlank
    private String tenantId;

    @NotNull
    @Valid
    private Channel channel;

    @NotNull
    @Valid
    private Customer customer;

    @NotNull
    @Valid
    private Ticket ticket;

    @Valid
    private List<Attachment> attachments;

    @Valid
    private ClientMeta clientMeta;
}
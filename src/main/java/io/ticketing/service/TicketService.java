package io.ticketing.service;

import io.ticketing.dto.Ticket.TicketRequest;
import io.ticketing.dto.Ticket.TicketResponse;
import io.ticketing.dto.category.Category;
import io.ticketing.dto.channel.Channel;
import io.ticketing.dto.customer.Customer;
import io.ticketing.model.CategoryEntity;
import io.ticketing.model.ChannelEntity;
import io.ticketing.model.CustomerEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TicketService {

    Mono<TicketResponse> createTicket(@NotNull String requestId, @NotNull UUID tenantId, @NotNull Channel channel, @NotNull Customer customer, Category category, @Valid TicketRequest request);
}

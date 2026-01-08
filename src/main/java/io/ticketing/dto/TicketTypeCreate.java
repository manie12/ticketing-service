package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record TicketTypeCreate(
        String name,
        Money price,
        Integer minQty,
        Integer maxQty,
        Eligibility eligibility
) {}
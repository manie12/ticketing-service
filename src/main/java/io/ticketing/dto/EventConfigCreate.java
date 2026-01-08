package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Type-dependent config for EVENT-like products.
 */
@JsonInclude(NON_NULL)
public record EventConfigCreate(
        OffsetDateTime eventStartAt,
        OffsetDateTime eventEndAt,
        OffsetDateTime doorsOpenAt,

        // for general admission capacity
        Integer capacityTotal,

        // for seated events (optional)
        String seatMapId
) {}
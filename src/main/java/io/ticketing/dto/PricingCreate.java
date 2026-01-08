package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.ticketing.datatype.FeeModel;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Pricing skeleton: you can create with just currency + fee model,
 * and later add ticket types via separate endpoints.
 */
@JsonInclude(NON_NULL)
public record PricingCreate(
        Boolean priceIncludesTax,
        FeeModel feeModel,
        String taxRuleId,

        // optional quick defaults
        List<TicketTypeCreate> ticketTypes,
        List<AddonCreate> addons
) {}

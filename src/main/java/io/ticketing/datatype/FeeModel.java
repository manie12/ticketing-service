package io.ticketing.datatype;

import lombok.Getter;

/**
 * Defines how service fees are handled.
 */
@Getter
public enum FeeModel {
    /**
     * Fees are added on top of the ticket price and paid by the customer.
     */
    PASS_TO_CUSTOMER("Fees added at checkout"),

    /**
     * Fees are included in the price; the merchant pays them out of their margin.
     */
    ABSORBED_BY_MERCHANT("Fees included in price"),

    /**
     * No fees apply to this specific product type.
     */
    NO_FEES("No service fees"),
    ABSORBED("Fees absorbed"),
    PASSTHROUGH("Fees passed through");
    private final String description;

    FeeModel(String description) {
        this.description = description;
    }

}
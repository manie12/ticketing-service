package io.ticketing.datatype;

import java.util.Locale;

/**
 * Default/Built-in category codes.
 *
 * NOTE:
 * In your system, categories are stored in DB per tenant, so this enum should be used only for:
 * - seed data defaults
 * - shared system categories
 * - validation for fixed category sets (if you choose that model)
 */
public enum Category {

    BILLING,
    TECHNICAL_SUPPORT,
    ACCOUNT_ACCESS,
    PAYMENTS,
    LOANS,
    FRAUD,
    COMPLAINT,
    GENERAL_INQUIRY;

}
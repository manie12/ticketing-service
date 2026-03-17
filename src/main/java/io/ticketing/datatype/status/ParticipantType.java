package io.ticketing.datatype.status;

import java.util.Locale;

/**
 * Ticket participant type.
 * Mirrors DB constraint: CUSTOMER | AGENT | SYSTEM
 */
public enum ParticipantType {
    CUSTOMER,
    AGENT,
    SYSTEM;
}
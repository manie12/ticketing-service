package io.ticketing.datatype.status;

import java.util.Locale;

/**
 * Ticket message type.
 * Mirrors DB constraint: TEXT | DESCRIPTION | SYSTEM_NOTE
 */
public enum MessageType {
    TEXT,
    DESCRIPTION,
    SYSTEM_NOTE;
}
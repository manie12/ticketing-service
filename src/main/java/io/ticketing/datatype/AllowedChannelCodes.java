package io.ticketing.datatype;

/**
 * Allowed inbound channel codes for ticket creation and routing.
 * Mirrors DB constraint/pattern and the validator allow-list.
 */
public enum AllowedChannelCodes {
    WEB,
    EMAIL,
    WHATSAPP;

    public static boolean isAllowed(String value) {
        if (value == null) return false;
        try {
            AllowedChannelCodes.valueOf(value.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
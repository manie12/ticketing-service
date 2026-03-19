package io.ticketing.datatype;

/**
 * Allowed ticket priorities.
 * Used for validation + SLA policy matching.
 */
public enum AllowedPriorities {
    LOW,
    MEDIUM,
    HIGH,
    URGENT;

    public static boolean isAllowed(String value) {
        if (value == null) return false;
        try {
            AllowedPriorities.valueOf(value.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
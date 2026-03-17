package io.ticketing.datatype;

import java.util.Locale;
import java.util.Set;

/**
 * Allowed storage providers for attachments.
 * <p>
 * Note: you created it as a class (not enum), so this is a small utility with constants.
 */
public final class AllowedStorageProviders {

    private AllowedStorageProviders() {
    }

    public static final String S3 = "S3";
    public static final String AZURE_BLOB = "AZURE_BLOB";
    public static final String LOCAL = "LOCAL";

    private static final Set<String> ALLOWED = Set.of(S3, AZURE_BLOB, LOCAL);

    public static boolean isAllowed(String value) {
        if (value == null) return false;
        return ALLOWED.contains(value.trim().toUpperCase(Locale.ROOT));
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }
}
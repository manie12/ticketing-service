package io.ticketing.datatype;

public enum ProductBadge {
    /** Highlights high-volume or trending items. */
    POPULAR("Popular", "#FF4500"), // Orange-Red

    /** Indicates low inventory or a short time window. */
    LIMITED("Limited Availability", "#B22222"), // Firebrick Red

    /** Suitable for all ages; used for filtering. */
    FAMILY_FRIENDLY("Family Friendly", "#2E8B57"), // Sea Green

    /** For items recently added to the catalog. */
    NEW_ARRIVAL("New", "#1E90FF"), // Dodger Blue

    /** Highlighting a specific price drop or promotion. */
    BEST_VALUE("Best Value", "#8A2BE2"); // Blue Violet

    private final String label;
    private final String hexColor;

    ProductBadge(String label, String hexColor) {
        this.label = label;
        this.hexColor = hexColor;
    }

    public String getLabel() {
        return label;
    }

    public String getHexColor() {
        return hexColor;
    }
}
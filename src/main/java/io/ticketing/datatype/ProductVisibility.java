package io.ticketing.datatype;

public enum ProductVisibility {
    /**
     * Visible to everyone; appears in search results and public catalogs.
     */
    PUBLIC(true),

    /**
     * Accessible via direct link only; hidden from search and public listings.
     */
    UNLISTED(false),

    /**
     * Restrictive access; only visible to staff or specific authorized users.
     */
    PRIVATE(false);

    private final boolean discoverable;

    ProductVisibility(boolean discoverable) {
        this.discoverable = discoverable;
    }

    /**
     * @return true if the product should be shown in public search or storefronts.
     */
    public boolean isDiscoverable() {
        return discoverable;
    }
}
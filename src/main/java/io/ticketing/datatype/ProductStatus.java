package io.ticketing.datatype;

public enum ProductStatus {
    /**
     * Product is live and can be purchased.
     */
    ACTIVE(true),

    /**
     * Product is temporarily unavailable for purchase but still exists in the system.
     */
    PAUSED(false),

    /**
     * Product is no longer in use; hidden from catalogs and cannot be sold.
     */
    ARCHIVED(false);

    private final boolean sellingAllowed;

    ProductStatus(boolean sellingAllowed) {
        this.sellingAllowed = sellingAllowed;
    }

    /**
     * Helper method to verify if the product can currently be added to a cart.
     */
    public boolean isSellingAllowed() {
        return sellingAllowed;
    }
}
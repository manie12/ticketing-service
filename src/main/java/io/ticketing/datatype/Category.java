package io.ticketing.datatype;

import lombok.Getter;

/**
     * Categories to help group logic and filtering
     */
    @Getter
    public enum Category {
        TICKETING("Ticketing & Admission"),
        EVENT("Events"),
        TOUR_ACTIVITY("Tours & Activities"),
        RENTAL_RESOURCE("Rentals & Resources"),
        MEMBERSHIP_SUBSCRIPTION("Memberships & Subscriptions"),
        VOUCHER_CREDIT("Vouchers & Credits"),
        ADDON("Add-ons");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

}

package io.ticketing.datatype;

import lombok.Getter;

/**
 * Comprehensive product type enumeration for ticketing and booking systems.
 * Each product type belongs to a category and has a display name for UI purposes.
 */
@Getter
public enum ProductType {
    // --- Ticketing / Admission ---
    TIMED_ENTRY(Category.TICKETING, "Timed Entry"),
    OPEN_DATED_ENTRY(Category.TICKETING, "Open Dated Entry"),
    DAY_PASS(Category.TICKETING, "Day Pass"),
    MULTI_DAY_PASS(Category.TICKETING, "Multi-Day Pass"),
    SEASON_PASS(Category.TICKETING, "Season Pass"),
    ANNUAL_PASS(Category.TICKETING, "Annual Pass"),

    // --- Events ---
    EVENT_GENERAL_ADMISSION(Category.EVENT, "General Admission"),
    EVENT_SEATED(Category.EVENT, "Seated Event"),
    EVENT_MULTI_SESSION(Category.EVENT, "Multi-Session Event"),
    FESTIVAL_WRISTBAND(Category.EVENT, "Festival Wristband"),

    // --- Tours & Activities ---
    TOUR_GUIDED(Category.TOUR_ACTIVITY, "Guided Tour"),
    ACTIVITY_SLOT(Category.TOUR_ACTIVITY, "Activity Slot"),
    PRIVATE_BOOKING(Category.TOUR_ACTIVITY, "Private Booking"),

    // --- Rentals & Resources ---
    RENTAL_TIME_BLOCK(Category.RENTAL_RESOURCE, "Rental Time Block"),
    RENTAL_DAY(Category.RENTAL_RESOURCE, "Daily Rental"),
    RESOURCE_RESERVATION(Category.RENTAL_RESOURCE, "Resource Reservation"),

    // --- Memberships / Subscriptions ---
    MEMBERSHIP(Category.MEMBERSHIP_SUBSCRIPTION, "Membership"),
    SUBSCRIPTION_BUNDLE(Category.MEMBERSHIP_SUBSCRIPTION, "Subscription Bundle"),

    // --- Vouchers / Gift / Credit products ---
    GIFT_CARD(Category.VOUCHER_CREDIT, "Gift Card"),
    VOUCHER(Category.VOUCHER_CREDIT, "Voucher"),
    CREDIT_PACK(Category.VOUCHER_CREDIT, "Credit Pack"),

    // --- Add-on only catalog items ---
    ADDON_ONLY(Category.ADDON, "Add-on Only");


    private final Category category;
    private final String displayName;

    ProductType(Category category, String displayName) {
        this.category = category;
        this.displayName = displayName;
    }

    /**
     * Utility method to check if the item can be sold standalone
     * @return true if the product can be sold independently, false if it's addon-only
     */
    public boolean isStandalone() {
        return this != ADDON_ONLY;
    }

    /**
     * Check if this product type belongs to the ticketing category
     */
    public boolean isTicketing() {
        return category == Category.TICKETING;
    }

    /**
     * Check if this product type belongs to the event category
     */
    public boolean isEvent() {
        return category == Category.EVENT;
    }

    /**
     * Check if this product type is time-based (requires specific time slots)
     */
    public boolean isTimeBased() {
        return this == TIMED_ENTRY
                || this == RENTAL_TIME_BLOCK
                || this == ACTIVITY_SLOT
                || this == TOUR_GUIDED
                || this == EVENT_SEATED
                || this == EVENT_GENERAL_ADMISSION;
    }

    /**
     * Check if this product type is date-flexible
     */
    public boolean isDateFlexible() {
        return this == OPEN_DATED_ENTRY
                || this == GIFT_CARD
                || this == VOUCHER
                || this == CREDIT_PACK;
    }

    /**
     * Check if this product type is a recurring pass
     */
    public boolean isPass() {
        return this == DAY_PASS
                || this == MULTI_DAY_PASS
                || this == SEASON_PASS
                || this == ANNUAL_PASS;
    }

    /**
     * Get all product types for a given category
     */
    public static ProductType[] getByCategory(Category category) {
        return java.util.Arrays.stream(values())
                .filter(pt -> pt.category == category)
                .toArray(ProductType[]::new);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ticketing.datatype.ProductType;
import io.ticketing.datatype.Visibility;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record CreateProductRequest(
        String organizationId,
        ProductType type,

        // Naming
        String title,
        String internalName,
        String slug, // optional; server can generate

        // Placement
        String venueId,
        String timezone,  // e.g. "Africa/Nairobi"
        String currency,  // e.g. "KES"

        // Visibility / channels
        Visibility visibility,
        SalesChannels salesChannels,

        // Content
        String shortDescription,
        String description,
        List<String> tags,
        MediaCreate media,

        // Policies (optional at create)
        PoliciesCreate policies,

        // Pricing / inventory (optional at create, but often provided)
        PricingCreate pricing,
        InventoryCreate inventory,

        // Scheduling config (type-dependent; optional at create)
        TimedEntryConfigCreate timedEntry,
        EventConfigCreate event,

        // Idempotency (recommended)
        @JsonProperty("idempotencyKey")
        String idempotencyKey
) {
}

package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Type-dependent config for TIMED_ENTRY-like products.
 */
@JsonInclude(NON_NULL)
public record TimedEntryConfigCreate(
        // e.g. 60-min slots, capacity per slot
        Integer slotDurationMinutes,
        Integer capacityPerSlot,
        Integer salesCutoffMinutes,

        // optional operating hours template for fast setup
        WeeklyHours weeklyHours
) {}
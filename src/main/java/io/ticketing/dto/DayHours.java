package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record DayHours(
        Boolean open,
        String openTime,  // "09:00"
        String closeTime  // "17:00"
) {}
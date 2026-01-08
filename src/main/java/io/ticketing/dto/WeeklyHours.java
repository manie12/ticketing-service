package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record WeeklyHours(
        DayHours monday,
        DayHours tuesday,
        DayHours wednesday,
        DayHours thursday,
        DayHours friday,
        DayHours saturday,
        DayHours sunday
) {}
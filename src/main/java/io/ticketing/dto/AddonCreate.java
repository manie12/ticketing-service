package io.ticketing.dto;
import io.ticketing.datatype.AppliesPer;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record AddonCreate(
        String name,
        Money price,
        AppliesPer appliesPer
) {
}
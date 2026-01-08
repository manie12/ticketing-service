package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.ticketing.datatype.CapacityPolicy;
import io.ticketing.datatype.InventoryMode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record InventoryCreate(
        InventoryMode mode,
        CapacityPolicy capacityPolicy,
        Integer holdTtlSeconds,
        Boolean oversellAllowed
) {
}
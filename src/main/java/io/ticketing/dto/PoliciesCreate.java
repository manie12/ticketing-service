package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.ticketing.datatype.RefundPolicy;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record PoliciesCreate(
        RefundPolicy refundPolicy,
        Integer refundCutoffMinutes,
        Integer lateArrivalGraceMinutes,
        Boolean transferable
) {}

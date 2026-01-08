package io.ticketing.dto;

public record Money(
        String currency,
        long amountMinor
) {}

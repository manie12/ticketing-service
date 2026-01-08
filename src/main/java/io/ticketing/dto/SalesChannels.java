package io.ticketing.dto;

public record SalesChannels(
        boolean online,
        boolean pos,
        boolean agents
) {}
package io.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record MediaCreate(
        ImageRef coverImage,
        List<ImageRef> gallery
) {}

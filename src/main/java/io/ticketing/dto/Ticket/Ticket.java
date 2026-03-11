package io.ticketing.dto.Ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    // categoryCode is optional
    private String categoryCode;

    // If absent, server resolves from settings/rules
    private String priority; // LOW|MEDIUM|HIGH|URGENT

    @NotBlank
    @Size(max = 300)
    private String subject;

    @NotBlank
    @Size(max = 10000)
    private String description;
}
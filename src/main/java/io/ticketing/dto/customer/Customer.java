package io.ticketing.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @NotBlank
    private String id;

    @Email
    private String email;

    // E.164 e.g. +254712345678
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "phoneE164 must be in E.164 format e.g. +2547xxxxxxx")
    private String phoneE164;


}
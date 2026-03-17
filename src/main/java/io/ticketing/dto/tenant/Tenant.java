package io.ticketing.dto.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tenant DTOs
 *
 * - TenantCreateRequest: used when creating/updating a tenant (API input)
 * - TenantResponse: used when returning tenant data (API output)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Tenant {


        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[A-Z0-9_]+$", message = "code must be uppercase letters, numbers, or underscore")
        private String code;

        @NotBlank
        @Size(max = 200)
        private String name;

        @Size(max = 30)
        private String status; // ACTIVE | SUSPENDED | DELETED (optional; server can default ACTIVE)

        @Size(max = 64)
        private String defaultTimezone; // e.g. Africa/Nairobi

        @Size(max = 20)
        private String defaultLocale; // e.g. en-KE

}
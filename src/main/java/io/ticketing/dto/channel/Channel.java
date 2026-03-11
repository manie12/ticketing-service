package io.ticketing.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Channel {
    @NotBlank
    private String code; // WEB | EMAIL | WHATSAPP
}

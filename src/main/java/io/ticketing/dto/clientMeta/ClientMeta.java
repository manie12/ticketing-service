package io.ticketing.dto.clientMeta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientMeta {
    private String device;    // mobile-web, android, ios, desktop-web
    private String appVersion;
    private String locale;
    private String timezone;

}
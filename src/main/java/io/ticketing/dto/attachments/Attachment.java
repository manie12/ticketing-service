package io.ticketing.dto.attachments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    @NotBlank
    private String clientAttachmentId;

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;

    @NotNull
    @PositiveOrZero
    private Long fileSizeBytes;

    @NotBlank
    private String storageProvider; // S3|AZURE_BLOB|LOCAL

    @NotBlank
    private String tempStoragePath;

    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "checksumSha256 must be 64 hex chars")
    private String checksumSha256;

}
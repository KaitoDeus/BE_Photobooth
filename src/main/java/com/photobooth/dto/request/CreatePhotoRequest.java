package com.photobooth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new photo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new photo")
public class CreatePhotoRequest {

    @NotNull(message = "Session ID is required")
    @Positive(message = "Session ID must be a positive number")
    @Schema(description = "ID of the session", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sessionId;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Schema(description = "URL of the photo image", example = "https://example.com/photos/photo1.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String imageUrl;
}

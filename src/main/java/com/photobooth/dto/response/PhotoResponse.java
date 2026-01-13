package com.photobooth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for photo data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Photo response data")
public class PhotoResponse {

    @Schema(description = "Photo ID", example = "1")
    private Long id;

    @Schema(description = "Session ID this photo belongs to", example = "1")
    private Long sessionId;

    @Schema(description = "URL of the photo image", example = "https://example.com/photos/photo1.jpg")
    private String imageUrl;

    @Schema(description = "Creation timestamp", example = "2024-01-08T12:00:00")
    private LocalDateTime createdAt;
}

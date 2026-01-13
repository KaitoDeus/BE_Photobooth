package com.photobooth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for session data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Session response data")
public class SessionResponse {

    @Schema(description = "Session ID", example = "1")
    private Long id;

    @Schema(description = "User ID who owns this session", example = "1")
    private Long userId;

    @Schema(description = "Creation timestamp", example = "2024-01-08T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Photos in this session")
    private List<PhotoResponse> photos;
}

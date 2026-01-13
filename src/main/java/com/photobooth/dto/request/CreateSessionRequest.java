package com.photobooth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new session
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new session")
public class CreateSessionRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    @Schema(description = "ID of the user creating the session", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
}

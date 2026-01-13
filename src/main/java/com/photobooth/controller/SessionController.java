package com.photobooth.controller;

import com.photobooth.dto.request.CreateSessionRequest;
import com.photobooth.dto.response.SessionResponse;
import com.photobooth.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================
 * Session Controller
 * ============================================
 * REST API endpoints for Session operations.
 * 
 * Endpoints:
 * - POST /api/v1/sessions - Create a new session
 * - GET /api/v1/sessions/{id} - Get session by ID
 * ============================================
 */
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Photo booth session endpoints")
public class SessionController {

    private final SessionService sessionService;

    /**
     * Create a new session
     * POST /api/v1/sessions
     */
    @PostMapping
    @Operation(summary = "Create a new session", description = "Creates a new photo booth session for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session created successfully", content = @Content(schema = @Schema(implementation = SessionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<SessionResponse> create(@Valid @RequestBody CreateSessionRequest request) {
        SessionResponse response = sessionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get session by ID
     * GET /api/v1/sessions/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID", description = "Retrieves a specific session with user and photos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found", content = @Content)
    })
    public ResponseEntity<SessionResponse> findById(
            @Parameter(description = "Session ID", example = "1") @PathVariable Long id) {
        SessionResponse session = sessionService.findById(id);
        return ResponseEntity.ok(session);
    }

    /**
     * Delete session by ID
     * DELETE /api/v1/sessions/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete session", description = "Deletes a session and all its photos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

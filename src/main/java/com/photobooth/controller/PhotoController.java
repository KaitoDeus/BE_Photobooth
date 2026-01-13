package com.photobooth.controller;

import com.photobooth.dto.request.CreatePhotoRequest;
import com.photobooth.dto.response.PhotoResponse;
import com.photobooth.service.PhotoService;
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

import java.util.List;

/**
 * ============================================
 * Photo Controller
 * ============================================
 * REST API endpoints for Photo operations.
 * 
 * Endpoints:
 * - POST /api/v1/photos - Create a new photo
 * - GET /api/v1/photos - Get photos (optionally filtered)
 * ============================================
 */
@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@Tag(name = "Photos", description = "Photo management endpoints")
public class PhotoController {

    private final PhotoService photoService;

    /**
     * Create a new photo
     * POST /api/v1/photos
     */
    @PostMapping
    @Operation(summary = "Create a new photo", description = "Creates a new photo associated with a session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Photo created successfully", content = @Content(schema = @Schema(implementation = PhotoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Session not found", content = @Content)
    })
    public ResponseEntity<PhotoResponse> create(@Valid @RequestBody CreatePhotoRequest request) {
        PhotoResponse response = photoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get photos with optional filtering
     * GET /api/v1/photos?sessionId=
     */
    @GetMapping
    @Operation(summary = "Get photos", description = "Retrieves photos. Can be filtered by sessionId query parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of photos retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found (when filtering by sessionId)", content = @Content)
    })
    public ResponseEntity<List<PhotoResponse>> findAll(
            @Parameter(description = "Filter photos by session ID", example = "1") @RequestParam(required = false) Long sessionId) {
        List<PhotoResponse> photos = photoService.findAll(sessionId);
        return ResponseEntity.ok(photos);
    }

    /**
     * Get photo by ID
     * GET /api/v1/photos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get photo by ID", description = "Retrieves a specific photo by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Photo not found", content = @Content)
    })
    public ResponseEntity<PhotoResponse> findById(@PathVariable Long id) {
        PhotoResponse photo = photoService.findById(id);
        return ResponseEntity.ok(photo);
    }

    /**
     * Delete photo by ID
     * DELETE /api/v1/photos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete photo", description = "Deletes a photo by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Photo deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Photo not found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        photoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

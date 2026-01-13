package com.photobooth.controller;

import com.photobooth.dto.response.PhotoResponse;
import com.photobooth.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * ============================================
 * File Upload Controller
 * ============================================
 * Handles file uploads for photos.
 * ============================================
 */
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "File upload endpoints")
public class UploadController {

    private final PhotoService photoService;

    // Directory to store uploaded photos
    private static final String UPLOAD_DIR = "uploads/photos/";

    /**
     * Upload a photo as base64
     * POST /api/v1/upload/base64
     */
    @PostMapping("/base64")
    @Operation(summary = "Upload photo as base64", description = "Uploads a base64 encoded image and saves it to the server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Photo uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<UploadResponse> uploadBase64(@RequestBody Base64UploadRequest request) {
        try {
            // Create uploads directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Decode base64 and save to file
            String base64Data = request.getImageData();

            // Remove data URL prefix if present (e.g., "data:image/png;base64,")
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Generate unique filename
            String extension = request.getExtension() != null ? request.getExtension() : "png";
            String filename = UUID.randomUUID().toString() + "." + extension;
            Path filePath = uploadPath.resolve(filename);

            // Write file
            Files.write(filePath, imageBytes);

            // Generate public URL
            String imageUrl = "/uploads/photos/" + filename;

            // If sessionId is provided, create photo record in database
            PhotoResponse savedPhoto = null;
            if (request.getSessionId() != null) {
                savedPhoto = photoService.createWithUrl(request.getSessionId(), imageUrl);
            }

            UploadResponse response = new UploadResponse();
            response.setSuccess(true);
            response.setImageUrl(imageUrl);
            response.setFilename(filename);
            response.setPhotoId(savedPhoto != null ? savedPhoto.getId() : null);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("Failed to save file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (IllegalArgumentException e) {
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("Invalid base64 data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Upload a photo as multipart file
     * POST /api/v1/upload/file
     */
    @PostMapping("/file")
    @Operation(summary = "Upload photo as file", description = "Uploads a multipart file and saves it to the server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Photo uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file", content = @Content)
    })
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        try {
            if (file.isEmpty()) {
                UploadResponse errorResponse = new UploadResponse();
                errorResponse.setSuccess(false);
                errorResponse.setError("File is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Create uploads directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "png";
            String filename = UUID.randomUUID().toString() + "." + extension;
            Path filePath = uploadPath.resolve(filename);

            // Write file
            Files.write(filePath, file.getBytes());

            // Generate public URL
            String imageUrl = "/uploads/photos/" + filename;

            // If sessionId is provided, create photo record in database
            PhotoResponse savedPhoto = null;
            if (sessionId != null) {
                savedPhoto = photoService.createWithUrl(sessionId, imageUrl);
            }

            UploadResponse response = new UploadResponse();
            response.setSuccess(true);
            response.setImageUrl(imageUrl);
            response.setFilename(filename);
            response.setPhotoId(savedPhoto != null ? savedPhoto.getId() : null);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("Failed to save file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ============================================
    // Request/Response DTOs
    // ============================================

    @lombok.Data
    public static class Base64UploadRequest {
        private String imageData; // Base64 encoded image
        private Long sessionId; // Optional: link to session
        private String extension; // Optional: file extension (default: png)
    }

    @lombok.Data
    public static class UploadResponse {
        private boolean success;
        private String imageUrl; // Public URL to access the image
        private String filename; // Saved filename
        private Long photoId; // ID of the photo in database (if saved)
        private String error; // Error message if failed
    }
}

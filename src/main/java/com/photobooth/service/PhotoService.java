package com.photobooth.service;

import com.photobooth.dto.request.CreatePhotoRequest;
import com.photobooth.dto.response.PhotoResponse;
import com.photobooth.entity.Photo;
import com.photobooth.entity.Session;
import com.photobooth.exception.ResourceNotFoundException;
import com.photobooth.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================
 * Photo Service
 * ============================================
 * Business logic layer for Photo operations.
 * ============================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final SessionService sessionService;

    /**
     * Create a new photo
     * 
     * @param request - Photo creation data
     * @return Created photo response
     */
    public PhotoResponse create(CreatePhotoRequest request) {
        // Verify session exists
        Session session = sessionService.findEntityById(request.getSessionId());

        Photo photo = Photo.builder()
                .session(session)
                .imageUrl(request.getImageUrl())
                .build();

        Photo savedPhoto = photoRepository.save(photo);
        return mapToResponse(savedPhoto);
    }

    /**
     * Create a new photo with direct URL (used by upload controller)
     * 
     * @param sessionId - Session ID
     * @param imageUrl  - Image URL
     * @return Created photo response
     */
    public PhotoResponse createWithUrl(Long sessionId, String imageUrl) {
        // Verify session exists
        Session session = sessionService.findEntityById(sessionId);

        Photo photo = Photo.builder()
                .session(session)
                .imageUrl(imageUrl)
                .build();

        Photo savedPhoto = photoRepository.save(photo);
        return mapToResponse(savedPhoto);
    }

    /**
     * Get all photos (optionally filtered by sessionId)
     * 
     * @param sessionId - Optional session ID filter
     * @return List of photos
     */
    @Transactional(readOnly = true)
    public List<PhotoResponse> findAll(Long sessionId) {
        List<Photo> photos;

        if (sessionId != null) {
            // Verify session exists
            sessionService.findEntityById(sessionId);
            photos = photoRepository.findBySession_IdOrderByCreatedAtDesc(sessionId);
        } else {
            photos = photoRepository.findAllByOrderByCreatedAtDesc();
        }

        return photos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a photo by ID
     * 
     * @param id - Photo ID
     * @return Photo if found
     */
    @Transactional(readOnly = true)
    public PhotoResponse findById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "id", id));
        return mapToResponse(photo);
    }

    /**
     * Delete a photo
     * 
     * @param id - Photo ID
     */
    public void delete(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "id", id));
        photoRepository.delete(photo);
    }

    /**
     * Map Photo entity to PhotoResponse DTO
     */
    private PhotoResponse mapToResponse(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .sessionId(photo.getSessionId())
                .imageUrl(photo.getImageUrl())
                .createdAt(photo.getCreatedAt())
                .build();
    }
}

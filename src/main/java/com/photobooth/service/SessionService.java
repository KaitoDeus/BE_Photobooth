package com.photobooth.service;

import com.photobooth.dto.request.CreateSessionRequest;
import com.photobooth.dto.response.PhotoResponse;
import com.photobooth.dto.response.SessionResponse;
import com.photobooth.entity.Session;
import com.photobooth.entity.User;
import com.photobooth.exception.ResourceNotFoundException;
import com.photobooth.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================
 * Session Service
 * ============================================
 * Business logic layer for Session operations.
 * ============================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    /**
     * Create a new session
     * 
     * @param request - Session creation data
     * @return Created session response
     */
    public SessionResponse create(CreateSessionRequest request) {
        // Verify user exists
        User user = userService.findEntityById(request.getUserId());

        Session session = Session.builder()
                .user(user)
                .build();

        Session savedSession = sessionRepository.save(session);
        return mapToResponse(savedSession);
    }

    /**
     * Get a session by ID with details
     * 
     * @param id - Session ID
     * @return Session with user and photos
     * @throws ResourceNotFoundException if session not found
     */
    @Transactional(readOnly = true)
    public SessionResponse findById(Long id) {
        Session session = sessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));
        return mapToResponseWithDetails(session);
    }

    /**
     * Get session entity by ID (for internal use)
     */
    @Transactional(readOnly = true)
    public Session findEntityById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));
    }

    /**
     * Get all sessions for a user
     * 
     * @param userId - User ID
     * @return List of sessions
     */
    @Transactional(readOnly = true)
    public List<SessionResponse> findByUserId(Long userId) {
        // Verify user exists
        userService.findEntityById(userId);

        return sessionRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete a session
     * 
     * @param id - Session ID
     */
    public void delete(Long id) {
        Session session = findEntityById(id);
        sessionRepository.delete(session);
    }

    /**
     * Map Session entity to SessionResponse DTO (basic)
     */
    private SessionResponse mapToResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .createdAt(session.getCreatedAt())
                .build();
    }

    /**
     * Map Session entity to SessionResponse DTO (with details)
     */
    private SessionResponse mapToResponseWithDetails(Session session) {
        List<PhotoResponse> photos = session.getPhotos().stream()
                .map(photo -> PhotoResponse.builder()
                        .id(photo.getId())
                        .sessionId(photo.getSessionId())
                        .imageUrl(photo.getImageUrl())
                        .createdAt(photo.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return SessionResponse.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .createdAt(session.getCreatedAt())
                .photos(photos)
                .build();
    }
}

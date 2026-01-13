package com.photobooth.service;

import com.photobooth.dto.request.CreateUserRequest;
import com.photobooth.dto.response.UserResponse;
import com.photobooth.entity.User;
import com.photobooth.exception.ResourceNotFoundException;
import com.photobooth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================
 * User Service
 * ============================================
 * Business logic layer for User operations.
 * ============================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Create a new user
     * 
     * @param request - User creation data
     * @return Created user response
     */
    public UserResponse create(CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    /**
     * Get all users
     * 
     * @return List of all users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a user by ID
     * 
     * @param id - User ID
     * @return User if found
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToResponse(user);
    }

    /**
     * Get user entity by ID (for internal use)
     */
    @Transactional(readOnly = true)
    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Delete a user
     * 
     * @param id - User ID
     */
    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    /**
     * Map User entity to UserResponse DTO
     */
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

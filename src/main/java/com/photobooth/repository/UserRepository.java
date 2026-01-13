package com.photobooth.repository;

import com.photobooth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================
 * User Repository
 * ============================================
 * Data access layer for User entity.
 * ============================================
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find all users ordered by creation date descending
     */
    List<User> findAllByOrderByCreatedAtDesc();

    /**
     * Check if a user exists by name
     */
    boolean existsByName(String name);
}

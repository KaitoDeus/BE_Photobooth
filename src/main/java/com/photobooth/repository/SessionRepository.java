package com.photobooth.repository;

import com.photobooth.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================
 * Session Repository
 * ============================================
 * Data access layer for Session entity.
 * ============================================
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Find all sessions ordered by creation date descending
     */
    List<Session> findAllByOrderByCreatedAtDesc();

    /**
     * Find all sessions by user ID
     */
    List<Session> findByUser_IdOrderByCreatedAtDesc(Long userId);

    /**
     * Find session by ID with user and photos eagerly loaded
     */
    @Query("SELECT s FROM Session s " +
            "LEFT JOIN FETCH s.user " +
            "LEFT JOIN FETCH s.photos " +
            "WHERE s.id = :id")
    Optional<Session> findByIdWithDetails(@Param("id") Long id);
}

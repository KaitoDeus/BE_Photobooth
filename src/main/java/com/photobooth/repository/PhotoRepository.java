package com.photobooth.repository;

import com.photobooth.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================
 * Photo Repository
 * ============================================
 * Data access layer for Photo entity.
 * ============================================
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /**
     * Find all photos ordered by creation date descending
     */
    List<Photo> findAllByOrderByCreatedAtDesc();

    /**
     * Find all photos by session ID
     */
    List<Photo> findBySession_IdOrderByCreatedAtDesc(Long sessionId);

    /**
     * Count photos in a session
     */
    long countBySession_Id(Long sessionId);
}

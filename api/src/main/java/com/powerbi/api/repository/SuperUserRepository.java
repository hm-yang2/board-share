package com.powerbi.api.repository;

import com.powerbi.api.model.SuperUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing SuperUser entities.
 * Provides methods for querying SuperUser data by user ID and checking for existence by user ID.
 */
public interface SuperUserRepository extends JpaRepository<SuperUser, Long> {
    Optional<SuperUser> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

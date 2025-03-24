package com.powerbi.api.repository;

import com.powerbi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Provides methods for querying User data by email and checking for existence by email.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    List<User> findByEmailContaining(String email);
    Optional<User> findByEmail(String email);
}


package com.powerbi.api.repository;

import com.powerbi.api.model.SuperUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperUserRepository extends JpaRepository<SuperUser, Long> {
    Optional<SuperUser> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

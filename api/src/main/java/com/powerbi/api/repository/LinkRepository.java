package com.powerbi.api.repository;

import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByUser(User user);
    List<Link> findByUserAndTitleContainingIgnoreCase(User user, String title);
    Optional<Link> findByUserAndId(User user, Long linkId);
}


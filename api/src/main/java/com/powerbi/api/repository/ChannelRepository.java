package com.powerbi.api.repository;

import com.powerbi.api.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Channel entities.
 * Provides methods for querying Channel data by name and visibility,
 * and checking for the existence of channels by name.
 */
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);
    List<Channel> findByVisibility(Channel.Visibility visibility);
    boolean existsByName(String name);
}

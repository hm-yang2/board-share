package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChannelOwner entities.
 * Provides methods for querying ChannelOwner data by channel ID, user ID,
 * and combinations of user and channel IDs, as well as counting owners by channel ID.
 */
public interface ChannelOwnerRepository extends JpaRepository<ChannelOwner, Long> {
    List<ChannelOwner> findByChannelId(Long channelId);
    List<ChannelOwner> findByUserId(Long id);
    Optional<ChannelOwner> findByIdAndChannelId(Long userId, Long channelId);
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);
    long countByChannelId(Long channelId);
}

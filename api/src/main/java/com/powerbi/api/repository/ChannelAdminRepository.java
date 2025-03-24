package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChannelAdmin entities.
 * Provides methods for querying ChannelAdmin data by channel ID, user ID,
 * and combinations of user and channel IDs.
 */
public interface ChannelAdminRepository extends JpaRepository<ChannelAdmin, Long> {
    List<ChannelAdmin> findByChannelId(Long channelId);
    List<ChannelAdmin> findByUserId(Long id);
    Optional<ChannelAdmin> findByUserIdAndChannelId(Long userId, Long channelId);
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);
}
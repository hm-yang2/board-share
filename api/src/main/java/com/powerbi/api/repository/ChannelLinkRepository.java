package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChannelLink entities.
 * Provides methods for querying ChannelLink data by channel ID and specific link IDs.
 */
public interface ChannelLinkRepository extends JpaRepository<ChannelLink, Long> {
    List<ChannelLink> findByChannelId(Long channelId);
    Optional<ChannelLink> findByChannelIdAndId(Long channelId, Long channelLinkId);
}


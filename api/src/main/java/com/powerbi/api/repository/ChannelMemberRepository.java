package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChannelMember entities.
 * Provides methods for querying ChannelMember data by channel ID, user ID,
 * and combinations of user and channel IDs.
 */
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    List<ChannelMember> findByChannelId(Long channelId);
    List<ChannelMember> findByUserId(Long id);
    Optional<ChannelMember> findByUserIdAndChannelId(Long userId, Long channelId);
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);
}


package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelOwnerRepository extends JpaRepository<ChannelOwner, Long> {
    List<ChannelOwner> findByChannelId(Long channelId);
    List<ChannelOwner> findByUserId(Long id);
    Optional<ChannelOwner> findByUserIdAndChannelId(Long userId, Long channelId);
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);
    long countByChannelId(Long channelId);
}

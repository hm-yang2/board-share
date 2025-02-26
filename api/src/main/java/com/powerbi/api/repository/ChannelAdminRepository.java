package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelAdminRepository extends JpaRepository<ChannelAdmin, Long> {
    List<ChannelAdmin> findByChannelId(Long channelId);
    List<ChannelAdmin> findByUserId(Long id);
    Optional<ChannelAdmin> findByUserIdAndChannelId(Long userId, Long channelId);
    boolean existsByUserIdAndChannelId(Long userId, Long channelId);
}
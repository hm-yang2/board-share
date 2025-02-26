package com.powerbi.api.repository;

import com.powerbi.api.model.ChannelLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelLinkRepository extends JpaRepository<ChannelLink, Long> {
    List<ChannelLink> findByChannelId(Long channelId);
}


package com.powerbi.api.repository;

import com.powerbi.api.model.ClusterLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClusterLinkRepository extends JpaRepository<ClusterLink, Long> {
    List<ClusterLink> findByClusterId(Long groupId);
    List<ClusterLink> findByChannelLinkId(Long channelLinkId);
}

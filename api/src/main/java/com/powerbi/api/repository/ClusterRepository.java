package com.powerbi.api.repository;

import com.powerbi.api.model.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    List<Cluster> findByChannelId(Long channelId);
}
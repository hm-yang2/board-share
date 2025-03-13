package com.powerbi.api.repository;

import com.powerbi.api.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);
    List<Channel> findByVisibility(Channel.Visibility visibility);
    boolean existsByName(String name);
}

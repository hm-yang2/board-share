package com.powerbi.api.repository;

import com.powerbi.api.model.GroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupLinkRepository extends JpaRepository<GroupLink, Long> {
    List<GroupLink> findByGroupId(Long groupId);
    List<GroupLink> findByChannelLinkId(Long channelLinkId);
}

package com.powerbi.api.controller;

import com.powerbi.api.dto.ChannelLinkDTO;
import com.powerbi.api.model.ChannelLink;
import com.powerbi.api.service.ChannelLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
public class ChannelLinkController {
    @Autowired
    private ChannelLinkService channelLinkService;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelLink>> getChannelLinks(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelLink> links = channelLinkService.getChannelLinks(user.getUsername(), channelId);
        return ResponseEntity.ok(links);
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelLink> createChannelLink(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelLinkDTO channelLinkData
    ) {
        ChannelLink createdLink = channelLinkService.createChannelLink(user.getUsername(), channelLinkData);
        return ResponseEntity.ok(createdLink);
    }

    @PostMapping("/{channelId}")
    public ResponseEntity<ChannelLink> editChannelLink(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelLinkDTO channelLinkData
    ) {
        ChannelLink updatedLink = channelLinkService.updateChannelLink(user.getUsername(), channelLinkData);
        return ResponseEntity.ok(updatedLink);
    }

    @DeleteMapping("/{channelId}/{channelLinkId}")
    public ResponseEntity<Void> deleteChannelLink(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @PathVariable Long channelLinkId
    ) {
        channelLinkService.deleteChannelLink(user.getUsername(), channelLinkId);
        return ResponseEntity.noContent().build();
    }
}

package com.powerbi.api.controller;

import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.service.ChannelOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/channelowner")
public class ChannelOwnerController {
    private ChannelOwnerService channelOwnerService;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelOwner>> getChannelOwners(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelOwner> owners = channelOwnerService.getChannelOwners(user.getUsername(), channelId);
        return ResponseEntity.ok(owners);
    }

    @PostMapping("/{channelId}")
    public ResponseEntity<Void> createChannelOwner(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @RequestParam Long newOwnerId
    ) {
        channelOwnerService.addChannelOwner(user.getUsername(), channelId, newOwnerId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{channelId}/{ownerId}")
    public ResponseEntity<Void> deleteChannelOwner(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @PathVariable Long ownerId
    ) {
        channelOwnerService.removeChannelOwner(user.getUsername(), channelId, ownerId);
        return ResponseEntity.noContent().build();
    }
}

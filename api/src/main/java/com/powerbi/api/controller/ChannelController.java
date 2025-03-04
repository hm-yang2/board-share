package com.powerbi.api.controller;

import com.powerbi.api.dto.ChannelDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<Channel>> getChannels(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(channelService.getChannels(user.getUsername(), search));
    }

    @PutMapping
    public ResponseEntity<Channel> createChannel(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelDTO channelDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channelService.createChannel(user.getUsername(), channelDTO));
    }

    @PostMapping
    public ResponseEntity<Channel> editChannel(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelDTO channelDTO
    ) {
        return ResponseEntity.ok(channelService.updateChannel(user.getUsername(), channelDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(
        @AuthenticationPrincipal User user,
        @PathVariable Long id
    ) {
        channelService.deleteChannel(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

}

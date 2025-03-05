package com.powerbi.api.controller;

import com.powerbi.api.model.ChannelMember;
import com.powerbi.api.service.ChannelAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/channelmember")
public class ChannelMemberController {
    @Autowired
    private ChannelAdminService channelAdminService;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelMember>> getChannelMembers(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelMember> members = channelAdminService.getChannelMembers(user.getUsername(), channelId);
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelMember> createChannelMember(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long newUserId = requestBody.get("id");
        ChannelMember newMember = channelAdminService.addChannelMember(user.getUsername(), channelId, newUserId);
        return ResponseEntity.status(201).body(newMember);
    }

    @DeleteMapping("/{channelId}/{memberId}")
    public ResponseEntity<Void> deleteChannelMember(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @PathVariable Long memberId
    ) {
        channelAdminService.removeChannelMember(user.getUsername(), channelId, memberId);
        return ResponseEntity.noContent().build();
    }
}

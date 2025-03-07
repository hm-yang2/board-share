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

/**
 * Controller that handles operations related to channel members.
 * Includes methods for retrieving, creating, and deleting channel members.
 */
@RestController
@RequestMapping("/api/channelmember")
public class ChannelMemberController {
    @Autowired
    private ChannelAdminService channelAdminService;

    /**
     * Retrieves a list of members associated with a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel for which members are being fetched.
     * @return A ResponseEntity containing a list of ChannelMember objects.
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelMember>> getChannelMembers(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelMember> members = channelAdminService.getChannelMembers(user.getUsername(), channelId);
        return ResponseEntity.ok(members);
    }

    /**
     * Creates a new member for a given channel.
     * The member details are provided in the request body as a map with user ID.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel to which the member will be added.
     * @param requestBody The request body containing the new member's user ID.
     * @return A ResponseEntity containing the created ChannelMember object.
     */
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

    /**
     * Deletes a member from a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel from which the member will be deleted.
     * @param memberId The ID of the channel member to be deleted.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
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

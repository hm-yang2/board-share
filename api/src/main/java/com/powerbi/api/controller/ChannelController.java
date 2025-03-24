package com.powerbi.api.controller;

import com.powerbi.api.dto.ChannelDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelRole;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller that handles operations related to channels.
 * Includes methods for retrieving, creating, updating, and deleting channels.
 */
@RestController
@RequestMapping("/api/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    /**
     * Retrieves a list of channels for the authenticated user.
     * Optionally, the user can provide a search term to filter the channels.
     *
     * @param user The currently authenticated user.
     * @param search An optional search term to filter channels.
     * @return A ResponseEntity containing a list of Channel objects.
     */
    @GetMapping
    public ResponseEntity<List<Channel>> getChannels(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(channelService.getChannels(user.getUsername(), search));
    }

    /**
     * Retrieves a specific channel by its ID.
     * Ensures that the requesting user has the necessary permissions to access the channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel to retrieve.
     * @return A ResponseEntity containing the requested Channel object.
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        return ResponseEntity.ok(channelService.getChannel(user.getUsername(), channelId));
    }

    /**
     * Retrieves the role of the authenticated user for a specific channel.
     * If no channel ID is provided, the method returns the default role of the user.
     *
     * @param user The currently authenticated user.
     * @param channelId An optional ID of the channel to retrieve the role for.
     * @return A ResponseEntity containing a map with the user's role for the specified channel.
     */
    @GetMapping("/role")
    public ResponseEntity<Map<String, ChannelRole>> getChannelRole(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long channelId
    ) {
        ChannelRole role = channelService.getChannelRole(user.getUsername(), channelId);
        return ResponseEntity.ok(Collections.singletonMap("role", role));
    }

    /**
     * Creates a new channel for the authenticated user.
     * The channel details are provided in the request body as a ChannelDTO object.
     *
     * @param user The currently authenticated user.
     * @param channelDTO The details of the new channel to be created.
     * @return A ResponseEntity containing the created Channel object.
     */
    @PutMapping
    public ResponseEntity<Channel> createChannel(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelDTO channelDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channelService.createChannel(user.getUsername(), channelDTO));
    }

    /**
     * Updates an existing channel for the authenticated user.
     * The updated channel details are provided in the request body as a ChannelDTO object.
     *
     * @param user The currently authenticated user.
     * @param channelDTO The details of the channel to be updated.
     * @return A ResponseEntity containing the updated Channel object.
     */
    @PostMapping
    public ResponseEntity<Channel> editChannel(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelDTO channelDTO
    ) {
        return ResponseEntity.ok(channelService.updateChannel(user.getUsername(), channelDTO));
    }

    /**
     * Deletes a channel for the authenticated user.
     *
     * @param user The currently authenticated user.
     * @param id The ID of the channel to be deleted.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(
        @AuthenticationPrincipal User user,
        @PathVariable Long id
    ) {
        channelService.deleteChannel(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

}

package com.powerbi.api.controller;

import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.service.ChannelOwnerService;
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
 * Controller that handles operations related to channel owners.
 * Includes methods for retrieving, creating, and deleting channel owners.
 */
@RestController
@RequestMapping("/api/channelowner")
public class ChannelOwnerController {
    @Autowired
    private ChannelOwnerService channelOwnerService;

    /**
     * Retrieves a list of owners associated with a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel for which owners are being fetched.
     * @return A ResponseEntity containing a list of ChannelOwner objects.
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelOwner>> getChannelOwners(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelOwner> owners = channelOwnerService.getChannelOwners(user.getUsername(), channelId);
        return ResponseEntity.ok(owners);
    }

    /**
     * Creates a new owner for a given channel.
     * The owner details are provided in the request body as a map with user ID.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel to which the owner will be added.
     * @param requestBody The request body containing the new owner's user ID.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 201 Created).
     */
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelOwner> createChannelOwner(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long newOwnerId = requestBody.get("id");
        ChannelOwner newCHannelOwner = channelOwnerService.addChannelOwner(user.getUsername(), channelId, newOwnerId);
        return ResponseEntity.status(201).body(newCHannelOwner);
    }

    /**
     * Deletes an owner from a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel from which the owner will be deleted.
     * @param ownerId The ID of the channel owner to be deleted.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
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

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
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/channellink")
public class ChannelLinkController {
    @Autowired
    private ChannelLinkService channelLinkService;

    /**
     * Retrieves a list of links associated with a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel for which links are being fetched.
     * @return A ResponseEntity containing a list of ChannelLink objects.
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelLink>> getChannelLinks(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelLink> links = channelLinkService.getChannelLinks(user.getUsername(), channelId);
        return ResponseEntity.ok(links);
    }

    /**
     * Retrieves a {@link ChannelLink} for the specified channel and channel link IDs.
     * 
     * This endpoint requires authentication, and the user must have the necessary permissions to access the channel's link.
     * If the channel is private, the user must have the appropriate permissions to view links in that channel.
     *
     * @param user The authenticated user making the request.
     * @param channelId The ID of the channel from which the link will be fetched.
     * @param channelLinkId The ID of the channel link to be retrieved.
     * @return A {@link ResponseEntity} containing the {@link ChannelLink} if found, or an appropriate error response.
     * @throws NoSuchElementException If the channel or channel link with the specified IDs does not exist.
     */
    @GetMapping("/{channelId}/{channelLinkId}")
    public ResponseEntity<ChannelLink> getChannelLink(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @PathVariable Long channelLinkId
    ) {
        ChannelLink link = channelLinkService.getChannelLink(user.getUsername(), channelId, channelLinkId);
        return ResponseEntity.ok(link);
    }

    /**
     * Creates a new link for a given channel.
     * The channel link details are provided in the request body as a ChannelLinkDTO object.
     *
     * @param user The currently authenticated user.
     * @param channelLinkData The details of the new channel link to be created.
     * @return A ResponseEntity containing the created ChannelLink object.
     */
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelLink> createChannelLink(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelLinkDTO channelLinkData
    ) {
        ChannelLink createdLink = channelLinkService.createChannelLink(user.getUsername(), channelLinkData);
        return ResponseEntity.ok(createdLink);
    }

    /**
     * Updates an existing link for a given channel.
     * The updated channel link details are provided in the request body as a ChannelLinkDTO object.
     *
     * @param user The currently authenticated user.
     * @param channelLinkData The details of the channel link to be updated.
     * @return A ResponseEntity containing the updated ChannelLink object.
     */
    @PostMapping("/{channelId}")
    public ResponseEntity<ChannelLink> editChannelLink(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelLinkDTO channelLinkData
    ) {
        ChannelLink updatedLink = channelLinkService.updateChannelLink(user.getUsername(), channelLinkData);
        return ResponseEntity.ok(updatedLink);
    }

    /**
     * Deletes a channel link for a given channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel from which the link will be deleted.
     * @param channelLinkId The ID of the channel link to be deleted.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
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

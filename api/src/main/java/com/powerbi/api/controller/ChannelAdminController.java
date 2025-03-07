package com.powerbi.api.controller;

import com.powerbi.api.model.ChannelAdmin;
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
 * Controller that handles operations related to channel administrators.
 * Includes methods for retrieving, adding, and removing channel administrators.
 */
@RestController
@RequestMapping("/api/channeladmin")
public class ChannelAdminController {
    @Autowired
    private ChannelAdminService channelAdminService;

    /**
     * Retrieves a list of administrators for a given channel.
     * The method fetches the administrators based on the logged-in user's username and the channel ID.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel for which administrators are being fetched.
     * @return A ResponseEntity containing a list of ChannelAdmin objects.
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelAdmin>> getChannelAdmins(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelAdmin> admins = channelAdminService.getChannelAdmins(user.getUsername(), channelId);
        return ResponseEntity.ok(admins);
    }

    /**
     * Adds a new administrator to a given channel.
     * The method accepts a request containing the ID of the new administrator and associates it with the specified channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel to which the new administrator is being added.
     * @param requestBody A map containing the ID of the new administrator to be added.
     * @return A ResponseEntity containing the newly created ChannelAdmin object.
     */
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelAdmin> createChannelAdmin(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long newAdminId = requestBody.get("id");
        ChannelAdmin newAdmin = channelAdminService.addChannelAdmin(user.getUsername(), channelId, newAdminId);
        return ResponseEntity.status(201).body(newAdmin);
    }

    /**
     * Removes an administrator from a given channel.
     * The method deletes the specified administrator from the channel.
     *
     * @param user The currently authenticated user.
     * @param channelId The ID of the channel from which the administrator is being removed.
     * @param adminId The ID of the administrator to be removed from the channel.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
    @DeleteMapping("/{channelId}/{adminId}")
    public ResponseEntity<Void> deleteChannelAdmin(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId,
            @PathVariable Long adminId
    ) {
        channelAdminService.removeChannelAdmin(user.getUsername(), channelId, adminId);
        return ResponseEntity.noContent().build();
    }
}

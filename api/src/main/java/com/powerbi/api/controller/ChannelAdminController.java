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

@RestController
@RequestMapping("/api/channeladmin")
public class ChannelAdminController {
    @Autowired
    private ChannelAdminService channelAdminService;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<ChannelAdmin>> getChannelAdmins(
            @AuthenticationPrincipal User user,
            @PathVariable Long channelId
    ) {
        List<ChannelAdmin> admins = channelAdminService.getChannelAdmins(user.getUsername(), channelId);
        return ResponseEntity.ok(admins);
    }

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

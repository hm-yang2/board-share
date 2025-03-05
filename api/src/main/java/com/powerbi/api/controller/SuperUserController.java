package com.powerbi.api.controller;

import com.powerbi.api.model.SuperUser;
import com.powerbi.api.service.SuperUserService;
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
@RequestMapping("/api/superuser")
public class SuperUserController {
    @Autowired
    private SuperUserService superUserService;

    @GetMapping
    public ResponseEntity<List<SuperUser>> getAllSuperUsers(
            @AuthenticationPrincipal User user
    ) {
        List<SuperUser> superUsers = superUserService.getAllSuperUsers(user.getUsername());
        return ResponseEntity.ok(superUsers);
    }

    @PutMapping
    public ResponseEntity<Void> createSuperUser(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long newUserId = requestBody.get("id");
        superUserService.addSuperUser(user.getUsername(), newUserId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{deleteSuperId}")
    public ResponseEntity<Void> deleteSuperUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long deleteSuperId
    ) {
        superUserService.removeSuperUser(user.getUsername(), deleteSuperId);
        return ResponseEntity.noContent().build();
    }
}

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

/**
 * Controller that handles operations related to super users.
 * Includes methods for retrieving, creating, and deleting super users.
 */
@RestController
@RequestMapping("/api/superuser")
public class SuperUserController {
    @Autowired
    private SuperUserService superUserService;

    /**
     * Retrieves a list of all super users associated with the currently authenticated user.
     *
     * @param user The currently authenticated user.
     * @return A ResponseEntity containing a list of SuperUser objects.
     */
    @GetMapping
    public ResponseEntity<List<SuperUser>> getSuperUsers(
            @AuthenticationPrincipal User user
    ) {
        List<SuperUser> superUsers = superUserService.getAllSuperUsers(user.getUsername());
        return ResponseEntity.ok(superUsers);
    }

    /**
     * Creates a new super user for the currently authenticated user.
     * The user to be added as a super user is provided in the request body as a map with the user ID.
     *
     * @param user The currently authenticated user.
     * @param requestBody A map containing the ID of the user to be added as a super user.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 201 Created).
     */
    @PutMapping
    public ResponseEntity<SuperUser> createSuperUser(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long newUserId = requestBody.get("id");
        SuperUser newSuper = superUserService.addSuperUser(user.getUsername(), newUserId);
        return ResponseEntity.status(201).body(newSuper);
    }

    /**
     * Deletes a super user by its ID.
     *
     * @param user The currently authenticated user.
     * @param deleteSuperId The ID of the super user to be deleted.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
    @DeleteMapping("/{deleteSuperId}")
    public ResponseEntity<Void> deleteSuperUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long deleteSuperId
    ) {
        superUserService.removeSuperUser(user.getUsername(), deleteSuperId);
        return ResponseEntity.noContent().build();
    }
}

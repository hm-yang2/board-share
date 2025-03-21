package com.powerbi.api.controller;

import com.powerbi.api.model.User;
import com.powerbi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that handles operations related to users.
 * Includes methods for retrieving users and deleting a specific user.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of all users.
     * Optionally, a search parameter can be provided to filter the list.
     *
     * @param search An optional search query for filtering users by name or other criteria.
     * @return A ResponseEntity containing a list of User objects.
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    /**
     * Retrieves the details of a user by their user ID.
     *
     * @param userId the ID of the user to be retrieved
     * @return a ResponseEntity containing the User object
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/self")
    public ResponseEntity<User> getSelf(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        return ResponseEntity.ok(userService.getUser(user.getUsername()));
    }

    /**
     * Deletes a user by its ID.
     *
     * @param userDetails The currently authenticated user performing the deletion.
     * @param id The ID of the user to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long id
    ) {
        userService.deleteUser(userDetails.getUsername(), id);
    }
}

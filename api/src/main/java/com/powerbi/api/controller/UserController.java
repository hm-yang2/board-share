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

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long id
    ) {
        userService.deleteUser(userDetails.getUsername(), id);
    }
}

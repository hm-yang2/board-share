package com.powerbi.api.controller;

import com.powerbi.api.dto.LinkDTO;
import com.powerbi.api.model.Link;
import com.powerbi.api.service.LinkService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that handles operations related to links associated with users.
 * Includes methods for retrieving, creating, editing, and deleting user-specific links.
 */
@RestController
@RequestMapping("/api/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * Retrieves a list of links associated with the currently authenticated user.
     * Optionally, filters links based on the provided search query.
     *
     * @param user The currently authenticated user.
     * @param search An optional search query to filter links.
     * @return A ResponseEntity containing a list of Link objects.
     */
    @GetMapping
    public ResponseEntity<List<Link>> getAllLinks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String search
    ) {
        List<Link> links = linkService.getUserLinks(user.getUsername(), search);
        return ResponseEntity.ok(links);
    }

    /**
     * Retrieves a single link associated with the currently authenticated user by its ID.
     *
     * @param user The currently authenticated user.
     * @param id The ID of the link to retrieve.
     * @return A ResponseEntity containing the requested Link object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Link> getLinkById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Link link = linkService.getUserLink(user.getUsername(), id);
        return ResponseEntity.ok(link);
    }

    /**
     * Creates a new link for the currently authenticated user.
     * The link data is provided in the request body as a LinkDTO.
     *
     * @param user The currently authenticated user.
     * @param linkDTO The data transfer object containing link details.
     * @return A ResponseEntity containing the created Link object.
     */
    @PutMapping
    public ResponseEntity<Link> createLink(@AuthenticationPrincipal User user, @RequestBody LinkDTO linkDTO) {
        Link createdLink = linkService.createUserLink(user.getUsername(), linkDTO);
        return ResponseEntity.ok(createdLink);
    }

    /**
     * Updates an existing link for the currently authenticated user.
     * The updated link data is provided in the request body as a LinkDTO.
     *
     * @param user The currently authenticated user.
     * @param linkDTO The data transfer object containing updated link details.
     * @return A ResponseEntity containing the updated Link object.
     */
    @PostMapping
    public ResponseEntity<Link> editLink(@AuthenticationPrincipal User user, @RequestBody LinkDTO linkDTO) {
        return ResponseEntity.ok(linkService.updateUserLink(user.getUsername(), linkDTO));
    }

    /**
     * Deletes a link associated with the currently authenticated user by its ID.
     *
     * @param user The currently authenticated user.
     * @param id The ID of the link to delete.
     * @return A ResponseEntity indicating that the operation was successful (HTTP 204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@AuthenticationPrincipal User user, @PathVariable Long id) {
        linkService.deleteUserLink(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}

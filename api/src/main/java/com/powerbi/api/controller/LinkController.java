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

@RestController
@RequestMapping("/api/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<List<Link>> getAllLinks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String search
    ) {
        List<Link> links = linkService.getUserLinks(user.getUsername(), search);
        return ResponseEntity.ok(links);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> getLinkById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Link link = linkService.getUserLink(user.getUsername(), id);
        return ResponseEntity.ok(link);
    }

    @PutMapping
    public ResponseEntity<Link> createLink(@AuthenticationPrincipal User user, @RequestBody LinkDTO linkDTO) {
        Link createdLink = linkService.createUserLink(user.getUsername(), linkDTO);
        return ResponseEntity.ok(createdLink);
    }

    @PostMapping
    public ResponseEntity<Link> editLink(@AuthenticationPrincipal User user, @RequestBody LinkDTO linkDTO) {
        return ResponseEntity.ok(linkService.updateUserLink(user.getUsername(), linkDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@AuthenticationPrincipal User user, @PathVariable Long id) {
        linkService.deleteUserLink(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}

package com.powerbi.api.service;

import com.powerbi.api.dto.LinkDTO;
import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.LinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing Link entities.
 * Handles operations such as retrieving, creating, updating, and deleting links for a specific user.
 */
@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private UserService userService;

    /**
     * Retrieves all links associated with the given user.
     *
     * @param username the user whose links are to be retrieved
     * @return a list of Link entities belonging to the user
     */
    @Transactional
    public List<Link> getUserLinks(String username, String search) {
        User user = userService.getUser(username);
        if (search != null && !search.isEmpty()) {
            return linkRepository.findByUserAndTitleContainingIgnoreCase(user, search);
        } else {
            return linkRepository.findByUser(user);
        }
    }

    /**
     * Retrieves a specific link for the user by its ID.
     * Ensures that the link belongs to the user.
     *
     * @param username   the user who owns the link
     * @param linkId the ID of the link to be retrieved
     * @return the Link entity if found and belongs to the user
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public Link getUserLink(String username, Long linkId) {
        User user = userService.getUser(username);
        Optional<Link> link = linkRepository.findByUserAndId(user, linkId);
        if (link.isEmpty()) {
            throw new ResourceNotFoundException("Link not found/Does not belong to user. LinkId: " + linkId);
        }
        return link.get();
    }

    /**
     * Creates a new link for the user.
     *
     * @param username    the user creating the link
     * @param linkDTO the data transfer object containing link details
     * @return the created Link entity
     */
    @Transactional
    public Link createUserLink(String username, LinkDTO linkDTO) {
        User user = userService.getUser(username);
        Link link = new Link();
        link.setUser(user);
        link.setLink(linkDTO.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    /**
     * Updates an existing link for the user.
     * Ensures that the link belongs to the user before updating.
     *
     * @param username    the user updating the link
     * @param linkDTO the data transfer object containing updated link details
     * @return the updated Link entity
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public Link updateUserLink(String username, LinkDTO linkDTO) {
        Link link = getUserLink(username, linkDTO.getId());
        link.setLink(linkDTO.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    /**
     * Deletes a link for the user.
     * Ensures that the link belongs to the user before deleting.
     *
     * @param username   the user requesting the deletion
     * @param linkId the ID of the link to be deleted
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public void deleteUserLink(String username, Long linkId) {
        Link link = getUserLink(username, linkId);
        linkRepository.delete(link);
    }
}

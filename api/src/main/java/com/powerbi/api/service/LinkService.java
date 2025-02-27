package com.powerbi.api.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.powerbi.api.dto.LinkDTO;
import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.LinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing Link entities.
 * Handles operations such as retrieving, creating, updating, and deleting links for a specific user.
 */
@Service
public class LinkService {
    private final LinkRepository linkRepository;

    /**
     * Constructs a LinkService with the necessary dependencies.
     *
     * @param linkRepository the repository for Link entities
     */
    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    /**
     * Retrieves all links associated with the given user.
     *
     * @param user the user whose links are to be retrieved
     * @return a list of Link entities belonging to the user
     */
    public List<Link> getUserLinks(User user) {
        return linkRepository.findByUser(user);
    }

    /**
     * Retrieves a specific link for the user by its ID.
     * Ensures that the link belongs to the user.
     *
     * @param user   the user who owns the link
     * @param linkId the ID of the link to be retrieved
     * @return the Link entity if found and belongs to the user
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public Link getUserLink(User user, Long linkId) {
        Optional<Link> link = linkRepository.findByUserAndId(user, linkId);
        if (link.isEmpty()) {
            throw new ResourceNotFoundException("Link not found/Does not belong to user. LinkId: " + linkId);
        }
        return link.get();
    }

    /**
     * Creates a new link for the user.
     *
     * @param user    the user creating the link
     * @param linkDTO the data transfer object containing link details
     * @return the created Link entity
     */
    @Transactional
    public Link createUserLink(User user, LinkDTO linkDTO) {
        Link link = new Link();
        link.setUser(user);
        link.setLink(link.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    /**
     * Updates an existing link for the user.
     * Ensures that the link belongs to the user before updating.
     *
     * @param user    the user updating the link
     * @param linkDTO the data transfer object containing updated link details
     * @return the updated Link entity
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public Link updateUserLink(User user, LinkDTO linkDTO) {
        Link link = getUserLink(user, linkDTO.getId());
        link.setLink(linkDTO.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    /**
     * Deletes a link for the user.
     * Ensures that the link belongs to the user before deleting.
     *
     * @param user   the user requesting the deletion
     * @param linkId the ID of the link to be deleted
     * @throws ResourceNotFoundException if the link is not found or does not belong to the user
     */
    @Transactional
    public void deleteUserLink(User user, Long linkId) {
        Link link = getUserLink(user, linkId);
        linkRepository.delete(link);
    }
}

package com.powerbi.api.service;

import com.powerbi.api.dto.ChannelLinkDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelLink;
import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelLinkRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.repository.LinkRepository;
import com.powerbi.api.model.ChannelRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing ChannelLink entities.
 * Handles CRUD operations and authorization checks for creating, updating,
 * retrieving, and deleting channel links.
 */
@Service
public class ChannelLinkService {
    @Autowired
    private UserService userService;
    private final ChannelLinkRepository channelLinkRepository;
    private final ChannelRepository channelRepository;
    private final LinkRepository linkRepository;
    private final PermissionService permissionService;

    /**
     * Constructs a ChannelLinkService with the necessary dependencies.
     *
     * @param channelLinkRepository the repository for ChannelLink entities
     * @param channelRepository     the repository for Channel entities
     * @param linkRepository        the repository for Link entities
     * @param permissionService     the service for checking user permissions
     */
    @Autowired
    public ChannelLinkService(
            ChannelLinkRepository channelLinkRepository,
            ChannelRepository channelRepository,
            LinkRepository linkRepository,
            PermissionService permissionService
    ) {
        this.channelLinkRepository = channelLinkRepository;
        this.channelRepository = channelRepository;
        this.linkRepository = linkRepository;
        this.permissionService = permissionService;
    }

    /**
     * Retrieves all ChannelLink entities associated with a given channel.
     *
     * @param username  the user requesting the links
     * @param channelId the ID of the channel
     * @return a list of ChannelLink entities
     * @throws AccessDeniedException if the user does not have permission to view links in the channel
     */
    @Transactional
    public List<ChannelLink> getChannelLinks(String username, Long channelId) {
        User user = userService.getUser(username);
        if (permissionService.hasChannelPermission(user, channelId, ChannelRole.NOT_ALLOWED)) {
            throw new AccessDeniedException("User does not have permission to view links in this channel.");
        }
        return channelLinkRepository.findByChannelId(channelId);
    }

    /**
     * Creates a new ChannelLink entity and associates it with a channel and a link.
     *
     * @param username         the user creating the channel link
     * @param channelLinkData  the data for the new channel link
     * @return the newly created ChannelLink entity
     * @throws AccessDeniedException if the user does not have permission to create links in the channel
     */
    @Transactional
    public ChannelLink createChannelLink(String username, ChannelLinkDTO channelLinkData) {
        User user = userService.getUser(username);
        if (permissionService.hasChannelPermission(
                user, channelLinkData.getChannelId(), ChannelRole.NOT_ALLOWED
        )) {
            throw new AccessDeniedException("User does not have permission to create links in this channel.");
        }

        Channel channel = channelRepository.findById(channelLinkData.getChannelId()).orElseThrow();
        Link link = linkRepository.findById(channelLinkData.getLinkId()).orElseThrow();

        // Create the new ChannelLink
        ChannelLink channelLink = new ChannelLink();
        channelLink.setTitle(channelLinkData.getTitle());
        channelLink.setChannel(channel);
        channelLink.setLink(link);

        return channelLinkRepository.save(channelLink);
    }

    /**
     * Updates an existing ChannelLink entity.
     *
     * @param username         the user updating the channel link
     * @param channelLinkId    the ID of the channel link to update
     * @param channelLinkData  the new data for the channel link
     * @return the updated ChannelLink entity
     * @throws AccessDeniedException if the user does not have permission to update the channel link
     */
    @Transactional
    public ChannelLink updateChannelLink(String username, Long channelLinkId, ChannelLinkDTO channelLinkData) {
        User user = userService.getUser(username);
        ChannelLink channelLink = channelLinkRepository.findById(channelLinkId).orElseThrow();

        // Check if the user is the link owner, channel owner, or super user
        if (!isAdminOrAbove(user, channelLink.getChannel().getId(), channelLink.getLink().getUser().getId())) {
            throw new AccessDeniedException("User does not have permission to update this channel link.");
        }

        channelLink.setTitle(channelLinkData.getTitle());
        channelLink.setLink(linkRepository.getReferenceById(channelLinkData.getLinkId()));

        return channelLinkRepository.save(channelLink);
    }

    /**
     * Deletes an existing ChannelLink entity.
     *
     * @param username      the user deleting the channel link
     * @param channelLinkId the ID of the channel link to delete
     * @throws AccessDeniedException if the user does not have permission to delete the channel link
     */
    public void deleteChannelLink(String username, Long channelLinkId) {
        User user = userService.getUser(username);
        ChannelLink channelLink = channelLinkRepository.findById(channelLinkId).orElseThrow();

        // Check if the user is the link owner, channel owner, or super user
        if (!isAdminOrAbove(user, channelLink.getChannel().getId(), channelLink.getLink().getUser().getId())) {
            throw new AccessDeniedException("User does not have permission to delete this channel link.");
        }

        channelLinkRepository.delete(channelLink);
    }

    /**
     * Checks if a user has administrative privileges or higher for a given channel.
     *
     * @param user         the user to check
     * @param channelId    the ID of the channel
     * @param linkOwnerId  the ID of the link owner
     * @return true if the user is an admin, channel owner, super user, or the link owner, false otherwise
     */
    private boolean isAdminOrAbove(User user, Long channelId, Long linkOwnerId) {
        return linkOwnerId.equals(user.getId()) ||
                permissionService.hasSuperUserPermission(user) ||
                permissionService.hasChannelPermission(user, channelId, ChannelRole.OWNER) ||
                permissionService.hasChannelPermission(user, channelId, ChannelRole.ADMIN);
    }
}

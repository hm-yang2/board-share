package com.powerbi.api.service;

import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.repository.UserRepository;
import com.powerbi.api.model.ChannelRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing ChannelOwner entities.
 * Handles operations related to retrieving, adding, and removing channel owners,
 * along with authorization checks to ensure proper access control.
 */
@Service
public class ChannelOwnerService {
    @Autowired
    private UserService userService;

    private final PermissionService permissionService;

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelOwnerRepository channelOwnerRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a ChannelOwnerService with the necessary dependencies.
     *
     * @param permissionService      the service for checking user permissions
     * @param channelRepository      the repository for Channel entities
     * @param channelMemberRepository the repository for ChannelMember entities
     * @param channelOwnerRepository the repository for ChannelOwner entities
     * @param userRepository         the repository for User entities
     */
    @Autowired
    public ChannelOwnerService(
            PermissionService permissionService,
            ChannelRepository channelRepository,
            ChannelMemberRepository channelMemberRepository,
            ChannelOwnerRepository channelOwnerRepository,
            UserRepository userRepository
    ) {
        this.permissionService = permissionService;
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.channelOwnerRepository = channelOwnerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of ChannelOwner entities associated with a specific channel.
     *
     * @param username  the user requesting the list of channel owners
     * @param channelId the ID of the channel
     * @return a list of ChannelOwner entities
     * @throws AccessDeniedException if the user is not an owner or super user of the channel
     */
    @Transactional
    public List<ChannelOwner> getChannelOwners(String username, Long channelId) {
        User user = userService.getUser(username);
        if (!isOwnerOrAbove(user, channelId)) {
            throw new AccessDeniedException("You do not have permission to view channel owners.");
        }

        return channelOwnerRepository.findByChannelId(channelId);
    }

    /**
     * Adds a new owner to a channel.
     *
     * @param username    the user performing the addition
     * @param channelId   the ID of the channel to add an owner to
     * @param newOwnerId  the ID of the user to be added as a channel owner
     * @throws AccessDeniedException          if the user is not an owner or super user of the channel
     * @throws DataIntegrityViolationException if the user is already an owner of the channel
     */
    @Transactional
    public ChannelOwner addChannelOwner(String username, Long channelId, Long newOwnerId) {
        User user = userService.getUser(username);
        if (!isOwnerOrAbove(user, channelId)) {
            throw new AccessDeniedException("You do not have permission to add a channel owner.");
        }

        if (channelOwnerRepository.existsByUserIdAndChannelId(newOwnerId, channelId)) {
            throw new DataIntegrityViolationException("User is already an owner of this channel.");
        }

        User newOwner = userRepository.findById(newOwnerId).orElseThrow();
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        ChannelOwner channelOwner = new ChannelOwner();
        channelOwner.setChannel(channel);
        channelOwner.setUser(newOwner);

        return channelOwnerRepository.save(channelOwner);
    }

    /**
     * Removes an owner from a channel.
     *
     * @param username  the user performing the removal
     * @param channelId the ID of the channel
     * @param ownerId   the ID of the owner to be removed
     * @throws AccessDeniedException          if the user is not an owner or super user of the channel
     * @throws DataIntegrityViolationException if the channel would be left without an owner
     */
    @Transactional
    public void removeChannelOwner(String username, Long channelId, Long ownerId) {
        User user = userService.getUser(username);
        if (!isOwnerOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to remove channel owners.");
        }

        ChannelOwner owner = channelOwnerRepository.findById(ownerId).orElseThrow();

        long ownerCount = channelOwnerRepository.countByChannelId(channelId);
        if (ownerCount <= 1) {
            throw new DataIntegrityViolationException("At least one owner must remain in the channel.");
        }

        channelOwnerRepository.delete(owner);
    }

    /**
     * Checks if a user has owner-level permissions or higher for a given channel.
     *
     * @param user      the user to check
     * @param channelId the ID of the channel
     * @return true if the user is an owner or super user, false otherwise
     */
    private boolean isOwnerOrAbove(User user, Long channelId) {
        ChannelRole role = permissionService.getUserRoleInChannel(user, channelId);
        return role == ChannelRole.OWNER || role == ChannelRole.SUPER_USER;
    }
}


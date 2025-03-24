package com.powerbi.api.service;

import com.powerbi.api.dto.ChannelDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelAdmin;
import com.powerbi.api.model.ChannelMember;
import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.model.ChannelRole;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.ChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing Channel entities.
 * Handles operations related to retrieving, creating, updating, and deleting channels,
 * as well as verifying user permissions for channel access and modifications.
 */
@Service
public class ChannelService {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private ChannelAdminRepository channelAdminRepository;
    @Autowired
    private ChannelOwnerRepository channelOwnerRepository;
    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of channels accessible to the user.
     * Includes public channels and private channels where the user is a member, admin, or owner.
     *
     * @param username the user requesting the list of channels
     * @return a list of Channel entities the user has access to
     */
    @Transactional
    public List<Channel> getChannels(String username, String search) {
        User user = userService.getUser(username);
        if (permissionService.hasSuperUserPermission(user)) {
            return channelRepository.findAll()
                    .stream()
                    .filter(channel -> search == null || search.trim().isEmpty() ||
                            channel.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        // Fetch public channels
        List<Channel> publicChannels = channelRepository.findByVisibility(Channel.Visibility.PUBLIC);

        // Fetch private channels the user has access to (member, admin, owner)
        List<Channel> privateChannels = new ArrayList<>();
        List<ChannelMember> members = channelMemberRepository.findByUserId(user.getId());
        List<ChannelAdmin> admins = channelAdminRepository.findByUserId(user.getId());
        List<ChannelOwner> owners = channelOwnerRepository.findByUserId(user.getId());

        // Add private channels to the list
        privateChannels.addAll(owners.stream().map(ChannelOwner::getChannel).toList());
        privateChannels.addAll(admins.stream().map(ChannelAdmin::getChannel).toList());
        privateChannels.addAll(members.stream().map(ChannelMember::getChannel).toList());

        // Combine public and private channels and return
        List<Channel> accessibleChannels = new ArrayList<>();
        accessibleChannels.addAll(privateChannels);
        accessibleChannels.addAll(publicChannels);

        //Remove duplicates
        Set<Channel> channels = new LinkedHashSet<>(accessibleChannels);

        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            channels = channels.stream()
                    .filter(channel -> channel.getName().toLowerCase().contains(searchLower))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return new ArrayList<>(channels);
    }

    /**
     * Retrieves a specific channel based on the given channel ID.
     * The method ensures that the requesting user has permission to access the channel.
     *
     * @param username The username of the currently authenticated user.
     * @param channelId The ID of the channel to retrieve.
     * @return The Channel object if the user has access.
     * @throws NoSuchElementException If the channel does not exist.
     * @throws AccessDeniedException If the user does not have permission to access the channel.
     */
    @Transactional
    public Channel getChannel(String username, Long channelId) {
        User user = userService.getUser(username);
        Channel channel = channelRepository.findById(channelId).orElseThrow();
        if (
            channel.getVisibility() == Channel.Visibility.PRIVATE &&
            permissionService.getUserRoleInChannel(user, channelId) == ChannelRole.NOT_ALLOWED
        ) {
            throw new AccessDeniedException("User not authorized to access this channel");
        }
        return channel;
    }

    /**
     * Retrieves the role of a user in a specific channel.
     * If no channel ID is provided, the method checks if the user is a superuser.
     *
     * @param username  the username of the user
     * @param channelId the ID of the channel (optional)
     * @return the role of the user in the specified channel
     */    
    @Transactional
    public ChannelRole getChannelRole(String username, @Nullable Long channelId) {
        User user = userService.getUser(username);

        if (channelId == null) { // Check if channelId is missing
            boolean isSuperUser = permissionService.hasSuperUserPermission(user);
            return isSuperUser ? ChannelRole.SUPER_USER : ChannelRole.NOT_ALLOWED;
        }

        Channel channel = channelRepository.findById(channelId).orElseThrow();
        return permissionService.getUserRoleInChannel(user, channelId);
    }

    /**
     * Creates a new channel and assigns the creating user as the channel owner.
     *
     * @param username       the user creating the channel
     * @param channelDTO the data transfer object containing channel details
     * @return the created Channel entity
     */
    @Transactional
    public Channel createChannel(String username, ChannelDTO channelDTO) {
        User user = userService.getUser(username);

        //Create channel
        Channel channel = new Channel();
        channel.setName(channelDTO.getName());
        channel.setDescription(channelDTO.getDescription());
        channel.setVisibility(channelDTO.getVisibility());
        channel = channelRepository.save(channel);

        //Create channel owner
        ChannelOwner channelOwner = new ChannelOwner();
        channelOwner.setUser(user);
        channelOwner.setChannel(channel);
        channelOwnerRepository.save(channelOwner);

        return channel;
    }

    /**
     * Updates an existing channel's details.
     * Only the owner of the channel is authorized to update it.
     *
     * @param username       the user requesting the update
     * @param channelDTO the data transfer object containing updated channel details
     * @return the updated Channel entity
     * @throws AccessDeniedException if the user is not the owner of the channel
     * @throws ResourceNotFoundException if the channel does not exist
     */
    @Transactional
    public Channel updateChannel(String username, ChannelDTO channelDTO) {
        User user = userService.getUser(username);
        if (channelDTO.getId() == null) {
            throw new ResourceNotFoundException("No Channel Id");
        }
        Channel channel = channelRepository.findById(channelDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found"));

        // Check if user is the owner of the channel
        if (!(permissionService.hasChannelPermission(user, channel.getId(), ChannelRole.OWNER) ||
                permissionService.hasSuperUserPermission(user))) {
            throw new AccessDeniedException("User is not authorized to update this channel");
        }

        // Update channel details
        channel.setName(channelDTO.getName());
        channel.setDescription(channelDTO.getDescription());
        channel.setVisibility(channelDTO.getVisibility());

        return channelRepository.save(channel);
    }

    /**
     * Deletes a channel if the user is the owner.
     *
     * @param username      the user requesting the deletion
     * @param channelId the ID of the channel to be deleted
     * @throws AccessDeniedException if the user is not the owner of the channel
     */
    @Transactional
    public void deleteChannel(String username, Long channelId) {
        User user = userService.getUser(username);

        if (!(permissionService.hasChannelPermission(user, channelId, ChannelRole.OWNER) ||
                permissionService.hasSuperUserPermission(user))) {
            throw new AccessDeniedException("User is not authorized to delete this channel");
        }

        channelRepository.deleteById(channelId);
    }
}

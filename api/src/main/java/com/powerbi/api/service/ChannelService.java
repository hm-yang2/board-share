package com.powerbi.api.service;

import com.powerbi.api.dto.ChannelDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelAdmin;
import com.powerbi.api.model.ChannelMember;
import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.model.ChannelRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class responsible for managing Channel entities.
 * Handles operations related to retrieving, creating, updating, and deleting channels,
 * as well as verifying user permissions for channel access and modifications.
 */
@Service
public class ChannelService {
    private final PermissionService permissionService;

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelAdminRepository channelAdminRepository;
    private final ChannelOwnerRepository channelOwnerRepository;

    /**
     * Constructs a ChannelService with the necessary dependencies.
     *
     * @param permissionService       the service for checking user permissions
     * @param channelRepository       the repository for Channel entities
     * @param channelMemberRepository the repository for ChannelMember entities
     * @param channelAdminRepository  the repository for ChannelAdmin entities
     * @param channelOwnerRepository  the repository for ChannelOwner entities
     */
    @Autowired
    public ChannelService(
        PermissionService permissionService,
        ChannelRepository channelRepository,
        ChannelMemberRepository channelMemberRepository,
        ChannelAdminRepository channelAdminRepository,
        ChannelOwnerRepository channelOwnerRepository
    ) {
        this.permissionService = permissionService;
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.channelAdminRepository = channelAdminRepository;
        this.channelOwnerRepository = channelOwnerRepository;
    }

    /**
     * Retrieves a list of channels accessible to the user.
     * Includes public channels and private channels where the user is a member, admin, or owner.
     *
     * @param user the user requesting the list of channels
     * @return a list of Channel entities the user has access to
     */
    @Transactional
    public List<Channel> getChannels(User user) {
        // Fetch public channels
        List<Channel> publicChannels = channelRepository.findByVisibility(Channel.Visibility.PUBLIC);

        // Fetch private channels the user has access to (member, admin, owner)
        List<Channel> privateChannels = new ArrayList<>();
        List<ChannelMember> members = channelMemberRepository.findByUserId(user.getId());
        List<ChannelAdmin> admins = channelAdminRepository.findByUserId(user.getId());
        List<ChannelOwner> owners = channelOwnerRepository.findByUserId(user.getId());

        // Add private channels to the list
        privateChannels.addAll(members.stream().map(ChannelMember::getChannel).toList());
        privateChannels.addAll(admins.stream().map(ChannelAdmin::getChannel).toList());
        privateChannels.addAll(owners.stream().map(ChannelOwner::getChannel).toList());

        // Combine public and private channels and return
        List<Channel> accessibleChannels = new ArrayList<>();
        accessibleChannels.addAll(publicChannels);
        accessibleChannels.addAll(privateChannels);

        //Remove duplicates
        Set<Channel> channels = new LinkedHashSet<>(accessibleChannels);
        accessibleChannels.clear();
        accessibleChannels.addAll(channels);

        return accessibleChannels;
    }

    /**
     * Creates a new channel and assigns the creating user as the channel owner.
     *
     * @param user       the user creating the channel
     * @param channelDTO the data transfer object containing channel details
     * @return the created Channel entity
     */
    @Transactional
    public Channel createChannel(User user, ChannelDTO channelDTO) {
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
     * @param user       the user requesting the update
     * @param channelDTO the data transfer object containing updated channel details
     * @return the updated Channel entity
     * @throws AccessDeniedException if the user is not the owner of the channel
     * @throws ResourceNotFoundException if the channel does not exist
     */
    @Transactional
    public Channel updateChannel(User user, ChannelDTO channelDTO) {
        Channel channel = channelRepository.findById(channelDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found"));

        // Check if user is the owner of the channel
        if (!permissionService.hasChannelPermission(user, channel.getId(), ChannelRole.OWNER)) {
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
     * @param user      the user requesting the deletion
     * @param channelId the ID of the channel to be deleted
     * @throws AccessDeniedException if the user is not the owner of the channel
     */
    @Transactional
    public void deleteChannel(User user, Long channelId) {
        if (!permissionService.hasChannelPermission(user, channelId, ChannelRole.OWNER)) {
            throw new AccessDeniedException("User is not authorized to delete this channel");
        }

        channelRepository.deleteById(channelId);
    }
}

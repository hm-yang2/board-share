package com.powerbi.api.service;

import com.powerbi.api.dto.ChannelDTO;
import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.model.ChannelRole;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({ChannelService.class, UserService.class, PermissionService.class})
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelOwnerRepository channelOwnerRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private ChannelAdminRepository channelAdminRepository;

    private User ownerUser;
    private User regularUser;
    private Channel channel;
    private ChannelOwner channelOwner;

    @BeforeEach
    void setUp() {
        // Create a channel owner
        ownerUser = new User();
        ownerUser.setEmail("ownerUser");
        userRepository.save(ownerUser);

        // Create a regular user
        regularUser = new User();
        regularUser.setEmail("regularUser");
        userRepository.save(regularUser);

        // Create a channel
        channel = new Channel();
        channel.setName("Test Channel");
        channel.setVisibility(Channel.Visibility.PUBLIC);
        channelRepository.save(channel);

        // Assign the owner to the channel
        channelOwner = new ChannelOwner();
        channelOwner.setUser(ownerUser);
        channelOwner.setChannel(channel);
        channelOwnerRepository.save(channelOwner);
    }

    @Test
    void testGetChannels_OwnerUser_ReturnsChannels() {
        List<Channel> channels = channelService.getChannels(ownerUser.getEmail(), null);
        assertNotNull(channels);
        assertFalse(channels.isEmpty());
        assertEquals(1, channels.size());
        assertEquals("Test Channel", channels.get(0).getName());
    }

    @Test
    void testGetChannel_OwnerUser_ReturnsChannel() {
        Channel retrievedChannel = channelService.getChannel(ownerUser.getEmail(), channel.getId());
        assertNotNull(retrievedChannel);
        assertEquals(channel.getId(), retrievedChannel.getId());
    }

    @Test
    void testGetChannel_RegularUser_ThrowsAccessDeniedException() {
        channel.setVisibility(Channel.Visibility.PRIVATE);
        channelRepository.save(channel);

        assertThrows(AccessDeniedException.class, () ->
                channelService.getChannel(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testCreateChannel_OwnerUser_CreatesChannel() {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setName("New Channel");
        channelDTO.setDescription("New Channel Description");
        channelDTO.setVisibility(Channel.Visibility.PUBLIC);

        Channel createdChannel = channelService.createChannel(ownerUser.getEmail(), channelDTO);
        assertNotNull(createdChannel);
        assertEquals("New Channel", createdChannel.getName());
        assertEquals("New Channel Description", createdChannel.getDescription());
    }

    @Test
    void testUpdateChannel_OwnerUser_UpdatesChannel() {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(channel.getId());
        channelDTO.setName("Updated Channel");
        channelDTO.setDescription("Updated Description");
        channelDTO.setVisibility(Channel.Visibility.PRIVATE);

        Channel updatedChannel = channelService.updateChannel(ownerUser.getEmail(), channelDTO);
        assertNotNull(updatedChannel);
        assertEquals("Updated Channel", updatedChannel.getName());
        assertEquals("Updated Description", updatedChannel.getDescription());
        assertEquals(Channel.Visibility.PRIVATE, updatedChannel.getVisibility());
    }

    @Test
    void testUpdateChannel_RegularUser_ThrowsAccessDeniedException() {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(channel.getId());
        channelDTO.setName("Updated Channel");

        assertThrows(AccessDeniedException.class, () ->
                channelService.updateChannel(regularUser.getEmail(), channelDTO));
    }

    @Test
    void testDeleteChannel_OwnerUser_DeletesChannel() {
        channelService.deleteChannel(ownerUser.getEmail(), channel.getId());
        assertFalse(channelRepository.findById(channel.getId()).isPresent());
    }

    @Test
    void testDeleteChannel_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelService.deleteChannel(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testGetChannels_WithSearchFilter_ReturnsFilteredChannels() {
        List<Channel> channels = channelService.getChannels(ownerUser.getEmail(), "Test");
        assertNotNull(channels);
        assertFalse(channels.isEmpty());
        assertEquals(1, channels.size());
        assertEquals("Test Channel", channels.get(0).getName());
    }

    @Test
    void testGetChannels_NoPrivateAccess_ReturnsPublicChannelsOnly() {
        List<Channel> channels = channelService.getChannels(regularUser.getEmail(), null);
        assertNotNull(channels);
        assertFalse(channels.isEmpty());
        assertEquals(1, channels.size());
        assertEquals("Test Channel", channels.get(0).getName());
    }

    @Test
    void testGetChannelRole_UserRoleInChannel_ReturnsRole() {
        ChannelRole role = channelService.getChannelRole(ownerUser.getEmail(), channel.getId());
        assertEquals(ChannelRole.OWNER, role);
    }


    @Test
    void testCreateChannel_WithAllFields_CreatesChannel() {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setName("New Channel");
        channelDTO.setDescription("New Channel Description");
        channelDTO.setVisibility(Channel.Visibility.PRIVATE);

        Channel createdChannel = channelService.createChannel(ownerUser.getEmail(), channelDTO);
        assertNotNull(createdChannel);
        assertEquals("New Channel", createdChannel.getName());
        assertEquals("New Channel Description", createdChannel.getDescription());
        assertEquals(Channel.Visibility.PRIVATE, createdChannel.getVisibility());
    }

    @Test
    void testUpdateChannel_NullId_ThrowsResourceNotFoundException() {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setName("Updated Channel");

        assertThrows(ResourceNotFoundException.class, () ->
                channelService.updateChannel(ownerUser.getEmail(), channelDTO));
    }

    @Test
    void testDeleteChannel_NonExistentChannel_ThrowsException() {
        assertThrows(AccessDeniedException.class, () ->
                channelService.deleteChannel(ownerUser.getEmail(), 999L));
    }
}

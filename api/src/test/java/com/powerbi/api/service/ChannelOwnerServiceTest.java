package com.powerbi.api.service;

import com.powerbi.api.model.*;
import com.powerbi.api.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({ChannelOwnerService.class, UserService.class, PermissionService.class})
class ChannelOwnerServiceTest {

    @Autowired
    private ChannelOwnerService channelOwnerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelOwnerRepository channelOwnerRepository;

    private User ownerUser;
    private User regularUser;
    private Channel channel;

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
        channelRepository.save(channel);

        // Assign the owner to the channel
        ChannelOwner channelOwner = new ChannelOwner();
        channelOwner.setUser(ownerUser);
        channelOwner.setChannel(channel);
        channelOwnerRepository.save(channelOwner);
    }

    @Test
    void testGetChannelOwners_OwnerUser_ReturnsOwners() {
        List<ChannelOwner> owners = channelOwnerService.getChannelOwners(ownerUser.getEmail(), channel.getId());
        assertNotNull(owners);
        assertEquals(1, owners.size());
        assertEquals(ownerUser.getId(), owners.get(0).getUser().getId());
    }

    @Test
    void testGetChannelOwners_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelOwnerService.getChannelOwners(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testAddChannelOwner_OwnerUser_AddsOwner() {
        ChannelOwner newOwner = channelOwnerService.addChannelOwner(ownerUser.getEmail(), channel.getId(), regularUser.getId());
        assertNotNull(newOwner);
        assertEquals(regularUser.getId(), newOwner.getUser().getId());
        assertEquals(channel.getId(), newOwner.getChannel().getId());
    }

    @Test
    void testAddChannelOwner_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelOwnerService.addChannelOwner(regularUser.getEmail(), channel.getId(), ownerUser.getId()));
    }

    @Test
    void testAddChannelOwner_UserAlreadyOwner_ThrowsDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () ->
                channelOwnerService.addChannelOwner(ownerUser.getEmail(), channel.getId(), ownerUser.getId()));
    }

    @Test
    void testRemoveChannelOwner_OwnerUser_RemovesOwner() {
        // Add a second owner
        ChannelOwner newOwner = channelOwnerService.addChannelOwner(ownerUser.getEmail(), channel.getId(), regularUser.getId());

        // Remove the second owner
        channelOwnerService.removeChannelOwner(ownerUser.getEmail(), channel.getId(), newOwner.getId());

        assertFalse(channelOwnerRepository.existsById(newOwner.getId()));
    }

    @Test
    void testRemoveChannelOwner_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelOwnerService.removeChannelOwner(regularUser.getEmail(), channel.getId(), ownerUser.getId()));
    }

    @Test
    void testRemoveChannelOwner_LastOwner_ThrowsDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () ->
                channelOwnerService.removeChannelOwner(ownerUser.getEmail(), channel.getId(), channelOwnerRepository.findAll().get(0).getId()));
    }
}
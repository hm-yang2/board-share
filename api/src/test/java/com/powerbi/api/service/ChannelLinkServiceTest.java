package com.powerbi.api.service;

import com.powerbi.api.dto.ChannelLinkDTO;
import com.powerbi.api.model.*;
import com.powerbi.api.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({ChannelLinkService.class, UserService.class, PermissionService.class})
class ChannelLinkServiceTest {

    @Autowired
    private ChannelLinkService channelLinkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ChannelLinkRepository channelLinkRepository;

    @Autowired
    private ChannelAdminRepository channelAdminRepository;

    private User ownerUser;
    private User regularUser;
    private Channel channel;
    private Link link;

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

        // Create a link
        link = new Link();
        link.setTitle("Test Link");
        link.setLink("https://example.com");
        link.setUser(ownerUser);
        linkRepository.save(link);
    }

    @Test
    void testGetChannelLinks_OwnerUser_ReturnsLinks() {
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Test Link");
        channelLinkRepository.save(channelLink);

        List<ChannelLink> links = channelLinkService.getChannelLinks(ownerUser.getEmail(), channel.getId());
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals("Test Link", links.get(0).getTitle());
    }

    @Test
    void testGetChannelLinks_RegularUser_ThrowsAccessDeniedException() {
        channel.setVisibility(Channel.Visibility.PRIVATE);
        channelRepository.save(channel);

        assertThrows(AccessDeniedException.class, () ->
                channelLinkService.getChannelLinks(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testCreateChannelLink_OwnerUser_CreatesLink() {
        ChannelLinkDTO channelLinkDTO = new ChannelLinkDTO();
        channelLinkDTO.setChannelId(channel.getId());
        channelLinkDTO.setLinkId(link.getId());
        channelLinkDTO.setTitle("New Link");

        ChannelLink createdLink = channelLinkService.createChannelLink(ownerUser.getEmail(), channelLinkDTO);
        assertNotNull(createdLink);
        assertEquals("New Link", createdLink.getTitle());
        assertEquals(channel.getId(), createdLink.getChannel().getId());
        assertEquals(link.getId(), createdLink.getLink().getId());
    }

    @Test
    void testUpdateChannelLink_OwnerUser_UpdatesLink() {
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Old Link");
        channelLink = channelLinkRepository.save(channelLink);

        ChannelLinkDTO channelLinkDTO = new ChannelLinkDTO();
        channelLinkDTO.setId(channelLink.getId());
        channelLinkDTO.setChannelId(channel.getId());
        channelLinkDTO.setLinkId(link.getId());
        channelLinkDTO.setTitle("Updated Link");

        ChannelLink updatedLink = channelLinkService.updateChannelLink(ownerUser.getEmail(), channelLinkDTO);
        assertNotNull(updatedLink);
        assertEquals("Updated Link", updatedLink.getTitle());
    }

    @Test
    void testUpdateChannelLink_AdminUser_UpdatesLink() {
        // Assign admin role to regularUser
        ChannelAdmin channelAdmin = new ChannelAdmin();
        channelAdmin.setUser(regularUser);
        channelAdmin.setChannel(channel);
        channelAdminRepository.save(channelAdmin);

        // Create a channel link
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Old Link");
        channelLink = channelLinkRepository.save(channelLink);

        // Prepare updated data
        ChannelLinkDTO channelLinkDTO = new ChannelLinkDTO();
        channelLinkDTO.setId(channelLink.getId());
        channelLinkDTO.setChannelId(channel.getId());
        channelLinkDTO.setLinkId(link.getId());
        channelLinkDTO.setTitle("Updated Link");

        // Perform the update
        ChannelLink updatedLink = channelLinkService.updateChannelLink(regularUser.getEmail(), channelLinkDTO);
        assertNotNull(updatedLink);
        assertEquals("Updated Link", updatedLink.getTitle());
    }

    @Test
    void testUpdateChannelLink_RegularUser_ThrowsAccessDeniedException() {
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Old Link");
        channelLink = channelLinkRepository.save(channelLink);

        ChannelLinkDTO channelLinkDTO = new ChannelLinkDTO();
        channelLinkDTO.setId(channelLink.getId());
        channelLinkDTO.setChannelId(channel.getId());
        channelLinkDTO.setLinkId(link.getId());
        channelLinkDTO.setTitle("Updated Link");

        assertThrows(AccessDeniedException.class, () ->
                channelLinkService.updateChannelLink(regularUser.getEmail(), channelLinkDTO));
    }

    @Test
    void testDeleteChannelLink_OwnerUser_DeletesLink() {
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Test Link");
        channelLink = channelLinkRepository.save(channelLink);

        channelLinkService.deleteChannelLink(ownerUser.getEmail(), channelLink.getId());
        assertFalse(channelLinkRepository.existsById(channelLink.getId()));
    }

    @Test
    void testDeleteChannelLink_AdminUser_DeletesLink() {
        // Assign admin role to regularUser
        ChannelAdmin channelAdmin = new ChannelAdmin();
        channelAdmin.setUser(regularUser);
        channelAdmin.setChannel(channel);
        channelAdminRepository.save(channelAdmin);

        // Create a channel link
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Test Link");
        channelLink = channelLinkRepository.save(channelLink);

        // Perform the delete
        channelLinkService.deleteChannelLink(regularUser.getEmail(), channelLink.getId());
        assertFalse(channelLinkRepository.existsById(channelLink.getId()));
    }

    @Test
    void testDeleteChannelLink_RegularUser_ThrowsAccessDeniedException() {
        ChannelLink channelLink = new ChannelLink();
        channelLink.setChannel(channel);
        channelLink.setLink(link);
        channelLink.setTitle("Test Link");
        channelLink = channelLinkRepository.save(channelLink);

        ChannelLink finalChannelLink = channelLink;
        assertThrows(AccessDeniedException.class, () ->
                channelLinkService.deleteChannelLink(regularUser.getEmail(), finalChannelLink.getId()));
    }
}
package com.powerbi.api.service;

import com.powerbi.api.dto.LinkDTO;
import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.LinkRepository;
import com.powerbi.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({LinkService.class, UserService.class, PermissionService.class})
class LinkServiceTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkRepository linkRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // Create a test user
        user = new User();
        user.setEmail("testUser");
        userRepository.save(user);

        // Create some links for the user
        Link link1 = new Link();
        link1.setUser(user);
        link1.setTitle("Link 1");
        link1.setLink("https://example.com/1");
        link1.setDescription("Description 1");
        linkRepository.save(link1);

        Link link2 = new Link();
        link2.setUser(user);
        link2.setTitle("Link 2");
        link2.setLink("https://example.com/2");
        link2.setDescription("Description 2");
        linkRepository.save(link2);
    }

    @Test
    void testGetUserLinks_ReturnsAllLinks() {
        List<Link> links = linkService.getUserLinks(user.getEmail(), null);
        assertNotNull(links);
        assertEquals(2, links.size());
    }

    @Test
    void testGetUserLinks_WithSearch_ReturnsFilteredLinks() {
        List<Link> links = linkService.getUserLinks(user.getEmail(), "Link 1");
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals("Link 1", links.getFirst().getTitle());
    }

    @Test
    void testGetUserLink_ReturnsLink() {
        Link link = linkRepository.findAll().getFirst();
        Link retrievedLink = linkService.getUserLink(user.getEmail(), link.getId());
        assertNotNull(retrievedLink);
        assertEquals(link.getId(), retrievedLink.getId());
    }

    @Test
    void testGetUserLink_NotFound_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () ->
                linkService.getUserLink(user.getEmail(), 999L));
    }

    @Test
    void testCreateUserLink_CreatesLink() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTitle("New Link");
        linkDTO.setLink("https://example.com/new");
        linkDTO.setDescription("New Description");

        Link createdLink = linkService.createUserLink(user.getEmail(), linkDTO);
        assertNotNull(createdLink);
        assertEquals("New Link", createdLink.getTitle());
        assertEquals("https://example.com/new", createdLink.getLink());
        assertEquals("New Description", createdLink.getDescription());
    }

    @Test
    void testUpdateUserLink_UpdatesLink() {
        Link link = linkRepository.findAll().getFirst();

        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(link.getId());
        linkDTO.setTitle("Updated Link");
        linkDTO.setLink("https://example.com/updated");
        linkDTO.setDescription("Updated Description");

        Link updatedLink = linkService.updateUserLink(user.getEmail(), linkDTO);
        assertNotNull(updatedLink);
        assertEquals("Updated Link", updatedLink.getTitle());
        assertEquals("https://example.com/updated", updatedLink.getLink());
        assertEquals("Updated Description", updatedLink.getDescription());
    }

    @Test
    void testUpdateUserLink_NotFound_ThrowsException() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(999L);
        linkDTO.setTitle("Nonexistent Link");

        assertThrows(ResourceNotFoundException.class, () ->
                linkService.updateUserLink(user.getEmail(), linkDTO));
    }

    @Test
    void testDeleteUserLink_DeletesLink() {
        Link link = linkRepository.findAll().getFirst();
        linkService.deleteUserLink(user.getEmail(), link.getId());
        assertFalse(linkRepository.existsById(link.getId()));
    }

    @Test
    void testDeleteUserLink_NotFound_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () ->
                linkService.deleteUserLink(user.getEmail(), 999L));
    }
}
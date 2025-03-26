package com.powerbi.api.service;

import com.powerbi.api.model.SuperUser;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.SuperUserRepository;
import com.powerbi.api.repository.UserRepository;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({UserService.class, PermissionService.class})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuperUserRepository superUserRepository;

    private User superUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        // Create a super user
        superUser = new User();
        superUser.setEmail("superuser@example.com");
        userRepository.save(superUser);

        SuperUser superUserEntity = new SuperUser();
        superUserEntity.setUser(superUser);
        superUserRepository.save(superUserEntity);

        // Create a regular user
        regularUser = new User();
        regularUser.setEmail("regularuser@example.com");
        userRepository.save(regularUser);
    }

    @Test
    void testGetAllUsers_ReturnsAllUsers() {
        List<User> users = userService.getAllUsers(null);
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testGetAllUsers_WithSearchQuery_ReturnsFilteredUsers() {
        List<User> users = userService.getAllUsers("superuser");
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("superuser@example.com", users.get(0).getEmail());
    }

    @Test
    void testGetUserByEmail_ReturnsUser() {
        User user = userService.getUser("superuser@example.com");
        assertNotNull(user);
        assertEquals("superuser@example.com", user.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound_ThrowsException() {
        assertThrows(NoSuchElementException.class, () ->
                userService.getUser("nonexistent@example.com"));
    }

    @Test
    void testGetUserById_ReturnsUser() {
        User user = userService.getUser(superUser.getId());
        assertNotNull(user);
        assertEquals(superUser.getId(), user.getId());
    }

    @Test
    void testGetUserById_NotFound_ThrowsException() {
        assertThrows(NoSuchElementException.class, () ->
                userService.getUser(999L));
    }

    @Test
    void testCreateUser_CreatesNewUser() {
        User newUser = userService.createUser("newuser@example.com");
        assertNotNull(newUser);
        assertEquals("newuser@example.com", newUser.getEmail());
    }

    @Test
    void testCreateUser_AlreadyExists_ThrowsException() {
        assertThrows(DataIntegrityViolationException.class, () ->
                userService.createUser("superuser@example.com"));
    }

    @Test
    void testCreateUser_FirstUserBecomesSuperUser() {
        // Remove all existing super users
        superUserRepository.deleteAll();

        User newUser = userService.createUser("firstsuperuser@example.com");
        assertNotNull(newUser);
        assertEquals("firstsuperuser@example.com", newUser.getEmail());

        // Verify the new user is a super user
        assertTrue(superUserRepository.existsByUserId(newUser.getId()));
    }

    @Test
    void testDeleteUser_SuperUserDeletesUser() {
        userService.deleteUser(superUser.getEmail(), regularUser.getId());
        assertFalse(userRepository.existsById(regularUser.getId()));
    }

    @Test
    void testDeleteUser_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                userService.deleteUser(regularUser.getEmail(), superUser.getId()));
    }
}
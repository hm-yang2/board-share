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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({SuperUserService.class, UserService.class, PermissionService.class})
class SuperUserServiceTest {

    @Autowired
    private SuperUserService superUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuperUserRepository superUserRepository;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        // Create a super user
        adminUser = new User();
        adminUser.setEmail("adminUser");
        userRepository.save(adminUser);

        SuperUser superUser = new SuperUser();
        superUser.setUser(adminUser);
        superUserRepository.save(superUser);

        // Create a regular user
        regularUser = new User();
        regularUser.setEmail("regularUser");
        userRepository.save(regularUser);
    }

    @Test
    void testGetAllSuperUsers_AdminUser_ReturnsSuperUsers() {
        List<SuperUser> superUsers = superUserService.getAllSuperUsers(adminUser.getEmail());
        assertNotNull(superUsers);
        assertEquals(1, superUsers.size());
        assertEquals(adminUser.getId(), superUsers.get(0).getUser().getId());
    }

    @Test
    void testGetAllSuperUsers_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                superUserService.getAllSuperUsers(regularUser.getEmail()));
    }

    @Test
    void testAddSuperUser_AdminUser_AddsSuperUser() {
        SuperUser newSuperUser = superUserService.addSuperUser(adminUser.getEmail(), regularUser.getId());
        assertNotNull(newSuperUser);
        assertEquals(regularUser.getId(), newSuperUser.getUser().getId());
    }

    @Test
    void testAddSuperUser_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                superUserService.addSuperUser(regularUser.getEmail(), adminUser.getId()));
    }

    @Test
    void testAddSuperUser_UserAlreadySuperUser_ThrowsDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () ->
                superUserService.addSuperUser(adminUser.getEmail(), adminUser.getId()));
    }

    @Test
    void testRemoveSuperUser_AdminUser_RemovesSuperUser() {
        // Add a second super user
        SuperUser newSuperUser = superUserService.addSuperUser(adminUser.getEmail(), regularUser.getId());

        // Remove the second super user
        superUserService.removeSuperUser(adminUser.getEmail(), newSuperUser.getId());

        assertFalse(superUserRepository.existsById(newSuperUser.getId()));
    }

    @Test
    void testRemoveSuperUser_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                superUserService.removeSuperUser(regularUser.getEmail(), adminUser.getId()));
    }

    @Test
    void testRemoveSuperUser_LastSuperUser_ThrowsDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () ->
                superUserService.removeSuperUser(adminUser.getEmail(), superUserRepository.findAll().get(0).getId()));
    }
}
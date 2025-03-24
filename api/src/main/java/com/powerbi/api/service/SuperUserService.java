package com.powerbi.api.service;

import com.powerbi.api.model.SuperUser;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.SuperUserRepository;
import com.powerbi.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class responsible for managing SuperUser entities.
 * Handles operations such as retrieving, adding, and removing SuperUsers.
 */
@Service
public class SuperUserService {
    @Autowired
    private SuperUserRepository superUserRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all SuperUsers in the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param username the user requesting the list of SuperUsers
     * @return a list of SuperUser entities
     * @throws AccessDeniedException if the user does not have SuperUser permissions
     */
    @Transactional
    public List<SuperUser> getAllSuperUsers(String username) {
        User user = userService.getUser(username);
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        return superUserRepository.findAll();
    }

    /**
     * Adds a new SuperUser to the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param username      the user requesting to add a new SuperUser
     * @param newUserId the ID of the user to be promoted to SuperUser
     * @throws AccessDeniedException           if the requesting user does not have SuperUser permissions
     * @throws DataIntegrityViolationException if the user is already a SuperUser
     * @throws NoSuchElementException          if the user to be promoted does not exist
     */
    @Transactional
    public SuperUser addSuperUser(String username, Long newUserId) {
        User user = userService.getUser(username);
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }

        // Check if the user already exists as a super user
        if (superUserRepository.existsByUserId(newUserId)) {
            throw new DataIntegrityViolationException("User is already a super user.");
        }

        // Check if the user exists
        User newSuperUser = userRepository.findById(newUserId).orElseThrow();

        SuperUser superUser = new SuperUser();
        superUser.setUser(newSuperUser);

        return superUserRepository.save(superUser);
    }

    /**
     * Removes an existing SuperUser from the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param username          the user requesting the removal of a SuperUser
     * @param deleteSuperId the ID of the SuperUser to be removed
     * @throws AccessDeniedException  if the requesting user does not have SuperUser permissions
     * @throws NoSuchElementException if the SuperUser to be removed does not exist
     */
    @Transactional
    public void removeSuperUser(String username, Long deleteSuperId) {
        User user = userService.getUser(username);
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        SuperUser superUser = superUserRepository.findById(deleteSuperId).orElseThrow();

        long superUserCount = superUserRepository.count();
        if (superUserCount <= 1) {
            throw new DataIntegrityViolationException("At least one super user must remain in the app.");
        }
        superUserRepository.delete(superUser);
    }
}

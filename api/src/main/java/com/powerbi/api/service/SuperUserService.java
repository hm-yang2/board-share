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
    private final SuperUserRepository superUserRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;

    /**
     * Constructs a SuperUserService with the necessary dependencies.
     *
     * @param superUserRepository the repository for SuperUser entities
     * @param userRepository      the repository for User entities
     * @param permissionService   the service responsible for permission checks
     */
    @Autowired
    public SuperUserService(
            SuperUserRepository superUserRepository,
            UserRepository userRepository,
            PermissionService permissionService
    ) {
        this.superUserRepository = superUserRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    /**
     * Retrieves all SuperUsers in the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param user the user requesting the list of SuperUsers
     * @return a list of SuperUser entities
     * @throws AccessDeniedException if the user does not have SuperUser permissions
     */
    @Transactional
    public List<SuperUser> getAllSuperUsers(User user) {
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        return superUserRepository.findAll();
    }

    /**
     * Adds a new SuperUser to the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param user      the user requesting to add a new SuperUser
     * @param newUserId the ID of the user to be promoted to SuperUser
     * @throws AccessDeniedException           if the requesting user does not have SuperUser permissions
     * @throws DataIntegrityViolationException if the user is already a SuperUser
     * @throws NoSuchElementException          if the user to be promoted does not exist
     */
    @Transactional
    public void addSuperUser(User user, Long newUserId) {
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

        superUserRepository.save(superUser);
    }

    /**
     * Removes an existing SuperUser from the system.
     * Requires the requesting user to have SuperUser permissions.
     *
     * @param user          the user requesting the removal of a SuperUser
     * @param deleteSuperId the ID of the SuperUser to be removed
     * @throws AccessDeniedException  if the requesting user does not have SuperUser permissions
     * @throws NoSuchElementException if the SuperUser to be removed does not exist
     */
    @Transactional
    public void removeSuperUser(User user, Long deleteSuperId) {
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have access to view SuperUsers");
        }
        SuperUser superUser = superUserRepository.findById(deleteSuperId).orElseThrow();

        superUserRepository.delete(superUser);
    }
}

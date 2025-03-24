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
 * Service class responsible for managing User entities.
 * Handles operations such as retrieving, creating, and deleting users.
 */
@Service
public class UserService {
    @Autowired
    private SuperUserRepository superUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionService permissionService;

    /**
     * Retrieves all users or users matching a search query.
     * If a search query is provided, it filters users by email containing the query.
     * If no search query is provided, all users are returned.
     *
     * @param searchQuery the search query to filter users by email (can be null or empty)
     * @return a list of User entities
     */
    @Transactional
    public List<User> getAllUsers(String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            return userRepository.findByEmailContaining(searchQuery);
        }
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the User entity
     * @throws NoSuchElementException if the user with the given email does not exist
     */
    @Transactional
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User entity
     * @throws NoSuchElementException if the user with the given ID does not exist
     */
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    /**
     * Creates a new User with the specified email.
     * Checks if a user with the same email already exists.
     *
     * @param email the email of the new user
     * @return the created User entity
     * @throws DataIntegrityViolationException if a user with the given email already exists
     */
    @Transactional
    public User createUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user = userRepository.save(user);

        //Create new superuser if no initial superuser
        if (superUserRepository.count() < 1) {
            SuperUser initialSuper = new SuperUser();
            initialSuper.setUser(user);
            superUserRepository.save(initialSuper);
        }

        return user;
    }

    /**
     * Deletes a user with the specified ID.
     * Only users with SuperUser permissions are allowed to delete other users.
     *
     * @param username            the user requesting the deletion
     * @param toDeleteUserId  the ID of the user to be deleted
     * @throws AccessDeniedException  if the requesting user does not have SuperUser permissions
     * @throws NoSuchElementException if the user to be deleted does not exist
     */
    @Transactional
    public void deleteUser(String username, Long toDeleteUserId) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!permissionService.hasSuperUserPermission(user)) {
            throw new AccessDeniedException("You do not have permission to delete users.");
        }
        User userToDelete = userRepository.findById(toDeleteUserId).orElseThrow();
        long ownerCount = userRepository.count();
        if (ownerCount <= 1) {
            throw new DataIntegrityViolationException("At least one user must remain in the app.");
        }
        userRepository.delete(userToDelete);
    }
}

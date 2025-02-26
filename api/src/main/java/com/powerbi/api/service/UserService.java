package com.powerbi.api.service;

import com.powerbi.api.model.User;
import com.powerbi.api.repository.SuperUserRepository;
import com.powerbi.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO write javadoc comments
/**
 *
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final SuperUserRepository superUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, SuperUserRepository superUserRepository) {
        this.userRepository = userRepository;
        this.superUserRepository = superUserRepository;
    }

    @Transactional
    public List<User> getAllUsers(String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            return userRepository.findByEmailContaining(searchQuery);
        }
        return userRepository.findAll();
    }

    /**
     * Checks that user does not already exists and creates a User
     * @param email String
     * @return User
     * @throws DataIntegrityViolationException if User already exists
     */
    @Transactional
    public User createUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId, Long toDeleteUserId) {
        if (!isSuperUser(userId)) {
            throw new AccessDeniedException("You do not have permission to delete users.");
        }
        Optional<User> userToDelete = userRepository.findById(toDeleteUserId);
        if (userToDelete.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + toDeleteUserId);
        }
        userRepository.deleteById(userId);
    }

    private boolean isSuperUser(Long userId) {
        return superUserRepository.existsByUserId(userId);
    }
}

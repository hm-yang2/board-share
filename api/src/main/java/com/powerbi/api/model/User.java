package com.powerbi.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Represents a User entity in the system.
 * A user is identified by a unique email and has a creation date.
 * 
 * Includes fields for the user's ID, email, and creation date.
 * This entity is mapped to a database table using JPA annotations.
 */
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
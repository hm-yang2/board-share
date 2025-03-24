package com.powerbi.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Represents a Channel entity in the system.
 * A channel is a container for content with attributes such as name, description,
 * visibility, and creation date.
 * 
 * The visibility of a channel can be either PUBLIC or PRIVATE.
 * This entity is mapped to a database table using JPA annotations.
 */
@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    /**
     * Enum representing the visibility of a channel.
     * 
     * Visibility levels include:
     * - PUBLIC: The channel is visible to all users.
     * - PRIVATE: The channel is restricted to specific users.
     */
    public enum Visibility {
        /** The channel is visible to all users. */
        PUBLIC,

        /** The channel is restricted to specific users. */
        PRIVATE
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }
}
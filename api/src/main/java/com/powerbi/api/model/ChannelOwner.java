package com.powerbi.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * Represents a ChannelOwner entity in the system.
 * Associates a user with ownership privileges for a specific channel.
 * 
 * Includes references to the user and channel, as well as the creation date of the association.
 * This entity is mapped to a database table using JPA annotations.
 */
@Entity
public class ChannelOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }
}
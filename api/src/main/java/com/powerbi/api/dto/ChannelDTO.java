package com.powerbi.api.dto;

import com.powerbi.api.model.Channel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Data Transfer Object (DTO) for the Channel entity.
 * Represents the data required to create or update a channel, as well as the data
 * returned when retrieving channel information.
 * 
 * Includes fields for the channel's ID, name, description, visibility, and creation date.
 * Validation constraints are applied to ensure data integrity.
 */
public class ChannelDTO {
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Channel.Visibility visibility;

    private Instant dateCreated;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Channel.Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Channel.Visibility visibility) {
        this.visibility = visibility;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }
}


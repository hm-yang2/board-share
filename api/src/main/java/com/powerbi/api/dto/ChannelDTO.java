package com.powerbi.api.dto;

import com.powerbi.api.model.Channel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

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


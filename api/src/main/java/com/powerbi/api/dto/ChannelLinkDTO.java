package com.powerbi.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Data Transfer Object (DTO) for the ChannelLink entity.
 * Represents the data required to create or update a channel link, as well as the data
 * returned when retrieving channel link information.
 * 
 * Includes fields for the link's ID, title, associated link ID, channel ID, and creation date.
 * Validation constraints are applied to ensure data integrity.
 */
public class ChannelLinkDTO {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    private Long linkId;

    @NotNull
    private Long channelId;

    private Instant dateCreated;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }
}


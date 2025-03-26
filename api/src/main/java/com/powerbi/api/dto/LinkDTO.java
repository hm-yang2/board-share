package com.powerbi.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

/**
 * Data Transfer Object (DTO) for the Link entity.
 * Represents the data required to create or update a link, as well as the data
 * returned when retrieving link information.
 * 
 * Includes fields for the link's ID, title, description, URL, and creation date.
 * Validation constraints are applied to ensure data integrity.
 */
public class LinkDTO {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    @URL
    private String link;

    private Instant dateCreated;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }
}


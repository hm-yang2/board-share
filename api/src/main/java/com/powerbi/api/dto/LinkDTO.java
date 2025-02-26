package com.powerbi.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

public class LinkDTO {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    @URL(message = "Invalid URL")
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
}


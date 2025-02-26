package com.powerbi.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

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
}


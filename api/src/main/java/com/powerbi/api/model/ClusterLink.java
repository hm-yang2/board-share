package com.powerbi.api.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
public class ClusterLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_link_id", nullable = false)
    private ChannelLink channelLink;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;

    private int idx;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    // Getters and Setters
}

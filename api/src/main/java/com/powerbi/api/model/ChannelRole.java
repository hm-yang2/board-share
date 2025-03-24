package com.powerbi.api.model;

/**
 * Enum representing the roles a user can have in a channel.
 * 
 * Roles include:
 * - MEMBER: A regular member of the channel.
 * - ADMIN: A user with administrative privileges in the channel.
 * - OWNER: The owner of the channel.
 * - SUPER_USER: A user with elevated privileges across channels.
 * - NOT_ALLOWED: A user without access to the channel.
 */
public enum ChannelRole {
    /** A regular member of the channel. */
    MEMBER,

    /** A user with administrative privileges in the channel. */
    ADMIN,

    /** The owner of the channel. */
    OWNER,

    /** A user with elevated privileges across channels. */
    SUPER_USER,

    /** A user without access to the channel. */
    NOT_ALLOWED
}
/**
 * Represents a channel in the application.
 */
export interface Channel {
  /** Unique identifier for the channel. */
  id: number;

  /** Name of the channel. */
  name: string;

  /** Description of the channel. */
  description: string;

  /** Visibility of the channel, either PUBLIC or PRIVATE. */
  visibility: "PUBLIC" | "PRIVATE";

  /** Date when the channel was created. */
  dateCreated: Date;
}

/**
 * Represents the role of a user in a channel.
 */
export interface ChannelRole {
  /** Role of the user in the channel. */
  role: "SUPER_USER" | "OWNER" | "ADMIN" | "MEMBER" | "NOT_ALLOWED";
}

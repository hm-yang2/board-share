/**
 * Data Transfer Object for creating or updating a channel.
 */
export interface ChannelDTO {
  /** Unique identifier for the channel (optional). */
  id?: number;

  /** Name of the channel. */
  name: string;

  /** Description of the channel (optional). */
  description?: string;

  /** Visibility of the channel, either PUBLIC or PRIVATE. */
  visibility: "PUBLIC" | "PRIVATE";

  /** Date when the channel was created (optional). */
  dateCreated?: Date;
}
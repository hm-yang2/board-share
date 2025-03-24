/**
 * Data Transfer Object for creating or updating a channel link.
 */
export interface ChannelLinkDTO {
  /** Unique identifier for the channel link (optional). */
  id?: number;

  /** Title of the channel link. */
  title: string;

  /** ID of the associated link. */
  linkId: number;

  /** ID of the associated channel. */
  channelId: number;

  /** Date when the channel link was created (optional). */
  dateCreated?: Date;
}

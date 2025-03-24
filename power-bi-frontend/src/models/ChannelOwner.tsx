import { Channel } from "./Channel";
import { User } from "./User";

/**
 * Represents an owner of a channel.
 */
export interface ChannelOwner {
  /** Unique identifier for the channel owner. */
  id: number;

  /** The user who is the owner of the channel. */
  user: User;

  /** The channel where the user is the owner. */
  channel: Channel;

  /** Date when the user was assigned as the owner. */
  dateCreated: Date;
}
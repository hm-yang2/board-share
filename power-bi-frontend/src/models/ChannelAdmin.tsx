import { Channel } from "./Channel";
import { User } from "./User";

/**
 * Represents an admin of a channel.
 */
export interface ChannelAdmin {
  /** Unique identifier for the channel admin. */
  id: number;

  /** The user who is the admin. */
  user: User;

  /** The channel where the user is an admin. */
  channel: Channel;

  /** Date when the user was assigned as an admin. */
  dateCreated: Date;
}

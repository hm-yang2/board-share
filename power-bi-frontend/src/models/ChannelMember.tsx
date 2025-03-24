import { Channel } from "./Channel";
import { User } from "./User";

/**
 * Represents a member of a channel.
 */
export interface ChannelMember {
  /** Unique identifier for the channel member. */
  id: number;

  /** The user who is a member of the channel. */
  user: User;

  /** The channel where the user is a member. */
  channel: Channel;

  /** Date when the user joined the channel. */
  dateCreated: Date;
}

import { Channel } from "./Channel";
import { Link } from "./Link";

/**
 * Represents a link associated with a channel.
 */
export interface ChannelLink {
  /** Unique identifier for the channel link. */
  id: number;

  /** Title of the channel link. */
  title: string;

  /** The link object associated with the channel. */
  link: Link;

  /** The channel where the link is associated. */
  channel: Channel;

  /** Date when the channel link was created. */
  dateCreated: Date;
}

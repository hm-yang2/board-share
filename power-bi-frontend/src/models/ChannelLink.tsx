import { Channel } from "./Channel";
import { Link } from "./Link";

export interface ChannelLink {
  id: number;
  title: string;
  link: Link; // Foreign key
  channel: Channel; // Foreign key
  dateCreated: Date;
}

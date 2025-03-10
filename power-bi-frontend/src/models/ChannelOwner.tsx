import { Channel } from "./Channel";
import { User } from "./User";

export interface ChannelOwner {
  user: User; // Foreign key
  channel: Channel; // Foreign key
  dateCreated: Date;
}

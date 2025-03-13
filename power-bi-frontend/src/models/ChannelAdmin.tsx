import { Channel } from "./Channel";
import { User } from "./User";

export interface ChannelAdmin {
  user: User; // Foreign key
  channel: Channel; // Foreign key
  dateCreated: Date;
}

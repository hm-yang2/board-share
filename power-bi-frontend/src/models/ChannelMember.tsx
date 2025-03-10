import { Channel } from "./Channel";
import { User } from "./User";

export interface ChannelMembers {
  user: User; // Foreign key
  channel: Channel; // Foreign key
  dateCreated: Date;
}
import { Channel } from "./Channel";
import { User } from "./User";

export interface ChannelMember {
  user: User;
  channel: Channel;
  dateCreated: Date;
}

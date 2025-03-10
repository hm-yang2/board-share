import { User } from "./User";

export interface SuperUser {
  user: User; // Foreign key
  dateCreated: Date;
}

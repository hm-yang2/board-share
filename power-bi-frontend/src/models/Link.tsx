import { User } from "./User";

export interface Link {
  id: number;
  user: User;
  title: string;
  description: string;
  link: string;
  dateCreated: Date;
}

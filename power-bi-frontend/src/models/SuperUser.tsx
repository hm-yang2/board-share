import { User } from "./User";

/**
 * Represents a super user in the application.
 */
export interface SuperUser {
  /** Unique identifier for the super user. */
  id: number;

  /** The user who is a super user. */
  user: User;

  /** Date when the user was assigned as a super user. */
  dateCreated: Date;
}

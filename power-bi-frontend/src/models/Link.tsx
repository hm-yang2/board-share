import { User } from "./User";

/**
 * Represents a link created by a user.
 */
export interface Link {
  /** Unique identifier for the link. */
  id: number;

  /** The user who created the link. */
  user: User;

  /** Title of the link. */
  title: string;

  /** Description of the link. */
  description: string;

  /** URL of the link. */
  link: string;

  /** Date when the link was created. */
  dateCreated: Date;
}

/**
 * Represents a user in the application.
 */
export interface User {
  /** Unique identifier for the user. */
  id: number;

  /** Email address of the user. */
  email: string;

  /** Date when the user account was created. */
  dateCreated: Date;
}

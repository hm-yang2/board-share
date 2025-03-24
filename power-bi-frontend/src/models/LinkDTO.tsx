/**
 * Data Transfer Object for creating or updating a link.
 */
export interface LinkDTO {
  /** Unique identifier for the link (optional). */
  id?: number;

  /** Title of the link. */
  title: string;

  /** Description of the link (optional). */
  description?: string;

  /** URL of the link. */
  link: string;

  /** Date when the link was created (optional). */
  dateCreated?: Date;
}

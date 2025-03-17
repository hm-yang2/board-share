export interface ChannelDTO {
  id?: number;
  name: string;
  description?: string;
  visibility: "PUBLIC" | "PRIVATE";
  dateCreated?: Date;
}

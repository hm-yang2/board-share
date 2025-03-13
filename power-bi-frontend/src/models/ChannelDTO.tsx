export interface ChannelDTO {
  id?: number;
  name: string;
  description?: string;
  visibility: "public" | "private";
  dateCreated?: Date;
}

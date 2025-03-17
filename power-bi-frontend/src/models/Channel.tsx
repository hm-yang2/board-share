export interface Channel {
  id: number;
  name: string;
  description: string;
  visibility: "PUBLIC" | "PRIVATE";
  dateCreated: Date;
}

export interface Channel {
  id: number;
  name: string;
  description: string;
  visibility: "public" | "private";
  dateCreated: Date;
}

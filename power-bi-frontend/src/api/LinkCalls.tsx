import { Link } from "../models/Link";
import { LinkDTO } from "../models/LinkDTO";
import { axiosInstance } from "./common";

export async function GetLinks() {
  try {
    const response = await axiosInstance.get("/api/link");
    const links: Link[] = response.data;
    return links;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function GetLink(linkId: number) {
  try {
    const response = await axiosInstance.get(`/api/link/${linkId}`);
    const link = response.data;
    return link;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function AddLink(linkDTO: LinkDTO) {
  try {
    const response = await axiosInstance.put("/api/link", linkDTO);
    const newLink: Link = response.data;
    return newLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export async function UpdateLink(linkDTO: LinkDTO) {
  try {
    const response = await axiosInstance.post("/api/link", linkDTO);
    const editedLink: Link = response.data;
    return editedLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export async function DeleteLink(linkId: number) {
  try {
    await axiosInstance.delete(`/api/link/${linkId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

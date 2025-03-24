import { Link } from "../models/Link";
import { LinkDTO } from "../models/LinkDTO";
import { axiosInstance } from "./common";

/**
 * Fetches all links from the server.
 * @returns A promise that resolves to an array of Link objects.
 */
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

/**
 * Fetches a specific link by its ID.
 * @param linkId The ID of the link to fetch.
 * @returns A promise that resolves to a Link object or null if not found.
 */
export async function GetLink(linkId: number) {
  try {
    const response = await axiosInstance.get(`/api/link/${linkId}`);
    const link = response.data;
    return link;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Creates a new link.
 * @param linkDTO The data transfer object containing link details.
 * @returns A promise that resolves to the newly created Link object or null if the operation fails.
 */
export async function CreateLink(linkDTO: LinkDTO) {
  try {
    const response = await axiosInstance.put("/api/link", linkDTO);
    const newLink: Link = response.data;
    return newLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Updates an existing link.
 * @param linkDTO The data transfer object containing updated link details.
 * @returns A promise that resolves to the updated Link object or null if the operation fails.
 */
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

/**
 * Deletes a link by its ID.
 * @param linkId The ID of the link to delete.
 * @returns A promise that resolves to false if the deletion fails.
 */
export async function DeleteLink(linkId: number) {
  try {
    await axiosInstance.delete(`/api/link/${linkId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

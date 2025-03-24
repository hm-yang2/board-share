import { ChannelLink } from "../models/ChannelLink";
import { ChannelLinkDTO } from "../models/ChannelLinkDTO";
import { axiosInstance } from "./common";

/**
 * Fetches a list of all links in a specific channel.
 * @param channelId The ID of the channel.
 * @returns A promise that resolves to an array of ChannelLink objects.
 */
export async function GetChannelLinks(channelId: number) {
  try {
    const response = await axiosInstance.get(`/api/channellink/${channelId}`);
    const channelLinks: ChannelLink[] = response.data;
    return channelLinks;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Fetches a specific channel link by its ID.
 * @param channelId The ID of the channel.
 * @param channelLinkId The ID of the channel link to fetch.
 * @returns A promise that resolves to a ChannelLink object or null if not found.
 */
export async function GetChannelLink(channelId: number, channelLinkId: number) {
  try {
    const response = await axiosInstance.get(
      `/api/channellink/${channelId}/${channelLinkId}`,
    );
    const channeLink: ChannelLink = response.data;
    return channeLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Creates a new channel link.
 * @param channelId The ID of the channel.
 * @param channelLinkDTO The data transfer object containing channel link details.
 * @returns A promise that resolves to the newly created ChannelLink object or null if the operation fails.
 */
export async function CreateChannelLink(
  channelId: number,
  channelLinkDTO: ChannelLinkDTO,
) {
  try {
    const response = await axiosInstance.put(
      `/api/channellink/${channelId}`,
      channelLinkDTO,
    );
    const newChannelLink: ChannelLink = response.data;
    return newChannelLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Updates an existing channel link.
 * @param channelId The ID of the channel.
 * @param channelLinkDTO The data transfer object containing updated channel link details.
 * @returns A promise that resolves to the updated ChannelLink object or null if the operation fails.
 */
export async function UpdateChannelLink(
  channelId: number,
  channelLinkDTO: ChannelLinkDTO,
) {
  try {
    const response = await axiosInstance.post(
      `/api/channellink/${channelId}`,
      channelLinkDTO,
    );
    const updatedChannelLink: ChannelLink = response.data;
    return updatedChannelLink;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Deletes a channel link by its ID.
 * @param channelId The ID of the channel.
 * @param channelLinkId The ID of the channel link to delete.
 * @returns A promise that resolves to false if the deletion fails.
 */
export async function DeleteChannelLink(
  channelId: number,
  channelLinkId: number,
) {
  try {
    await axiosInstance.delete(
      `/api/channellink/${channelId}/${channelLinkId}`,
    );
  } catch (error) {
    console.error(error);
    return false;
  }
}

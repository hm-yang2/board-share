import { Channel, ChannelRole } from "../models/Channel";
import { ChannelDTO } from "../models/ChannelDTO";
import { axiosInstance } from "./common";

/**
 * Fetches a list of channels from the server.
 * @param search Optional search query to filter channels.
 * @returns A promise that resolves to an array of Channel objects.
 */
export async function GetChannels(search?: string) {
  try {
    const response = await axiosInstance.get("/api/channel", {
      params: search ? { search } : {},
    });
    const channels: Channel[] = response.data;
    return channels;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Fetches a specific channel by its ID.
 * @param channelId The ID of the channel to fetch.
 * @returns A promise that resolves to a Channel object or null if not found.
 */
export async function GetChannel(channelId: number) {
  try {
    const response = await axiosInstance.get(`/api/channel/${channelId}`);
    const channel: Channel = response.data;
    return channel;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Fetches the role of the current user in a specific channel.
 * @param channelId Optional ID of the channel to fetch the role for.
 * @returns A promise that resolves to the user's role in the channel or null if not found.
 */
export async function GetChannelRole(channelId?: number) {
  try {
    const response = await axiosInstance.get(`/api/channel/role`, {
      params: channelId !== undefined ? { channelId } : {},
    });
    const role: ChannelRole = response.data;
    return role["role"];
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Creates a new channel.
 * @param channelDTO The data transfer object containing channel details.
 * @returns A promise that resolves to the newly created Channel object or null if the operation fails.
 */
export async function CreateChannel(channelDTO: ChannelDTO) {
  try {
    const response = await axiosInstance.put("/api/channel", channelDTO);
    const channel: Channel = response.data;
    return channel;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Updates an existing channel.
 * @param channelDTO The data transfer object containing updated channel details.
 * @returns A promise that resolves to the updated Channel object or null if the operation fails.
 */
export async function UpdateChannel(channelDTO: ChannelDTO) {
  try {
    const response = await axiosInstance.post(`/api/channel`, channelDTO);
    const channel: Channel = response.data;
    return channel;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Deletes a channel by its ID.
 * @param channelId The ID of the channel to delete.
 * @returns A promise that resolves to true if the deletion succeeds or false if it fails.
 */
export async function DeleteChannel(channelId: number) {
  try {
    await axiosInstance.delete(`/api/channel/${channelId}`);
    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
}

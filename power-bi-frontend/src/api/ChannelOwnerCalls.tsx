import { ChannelOwner } from "../models/ChannelOwner";
import { axiosInstance } from "./common";

/**
 * Fetches a list of all owners in a specific channel.
 * @param channelId The ID of the channel.
 * @returns A promise that resolves to an array of ChannelOwner objects.
 */
export async function GetChannelOwners(channelId: number) {
  try {
    const response = await axiosInstance.get(`/api/channelowner/${channelId}`);
    const owners: ChannelOwner[] = response.data;
    return owners;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Adds a user as an owner to a specific channel.
 * @param channelId The ID of the channel.
 * @param userId The ID of the user to add as an owner.
 * @returns A promise that resolves to the newly created ChannelOwner object or false if the operation fails.
 */
export async function AddChannelOwner(channelId: number, userId: number) {
  try {
    const response = await axiosInstance.put(`/api/channelowner/${channelId}`, {
      id: userId,
    });
    const newOwner: ChannelOwner = response.data;
    return newOwner;
  } catch (error) {
    console.error(error);
    return false;
  }
}

/**
 * Removes an owner from a specific channel.
 * @param channelId The ID of the channel.
 * @param ownerId The ID of the owner to remove.
 * @returns A promise that resolves to false if the operation fails.
 */
export async function RemoveChannelOwner(channelId: number, ownerId: number) {
  try {
    await axiosInstance.delete(`/api/channelowner/${channelId}/${ownerId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

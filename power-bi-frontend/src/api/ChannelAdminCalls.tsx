import { ChannelAdmin } from "../models/ChannelAdmin";
import { axiosInstance } from "./common";

/**
 * Fetches a list of all admins in a specific channel.
 * @param channelId The ID of the channel.
 * @returns A promise that resolves to an array of ChannelAdmin objects.
 */
export async function GetChannelAdmins(channelId: number) {
  try {
    const response = await axiosInstance.get(`/api/channeladmin/${channelId}`);
    const admins: ChannelAdmin[] = response.data;
    return admins;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Adds a user as an admin to a specific channel.
 * @param channelId The ID of the channel.
 * @param userId The ID of the user to add as an admin.
 * @returns A promise that resolves to the newly created ChannelAdmin object or false if the operation fails.
 */
export async function AddChannelAdmin(channelId: number, userId: number) {
  try {
    const response = await axiosInstance.put(`/api/channeladmin/${channelId}`, {
      id: userId,
    });
    const newAdmin: ChannelAdmin = response.data;
    return newAdmin;
  } catch (error) {
    console.error(error);
    return false;
  }
}

/**
 * Removes an admin from a specific channel.
 * @param channelId The ID of the channel.
 * @param userId The ID of the admin to remove.
 * @returns A promise that resolves to false if the operation fails.
 */
export async function RemoveChannelAdmin(channelId: number, userId: number) {
  try {
    await axiosInstance.delete(`/api/channeladmin/${channelId}/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

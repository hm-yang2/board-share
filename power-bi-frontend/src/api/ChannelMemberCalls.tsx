import { axiosInstance } from "./common";
import { ChannelMember } from "../models/ChannelMember";

/**
 * Fetches a list of all members in a specific channel.
 * @param channelId The ID of the channel.
 * @returns A promise that resolves to an array of ChannelMember objects.
 */
export async function GetChannelMembers(channelId: number) {
  try {
    const response = await axiosInstance.get(`/api/channelmember/${channelId}`);
    const members: ChannelMember[] = response.data;
    return members;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Adds a user as a member to a specific channel.
 * @param channelId The ID of the channel.
 * @param userId The ID of the user to add as a member.
 * @returns A promise that resolves to the newly created ChannelMember object or false if the operation fails.
 */
export async function AddChannelMember(channelId: number, userId: number) {
  try {
    const response = await axiosInstance.put(
      `/api/channelmember/${channelId}`,
      { id: userId },
    );
    const newMember: ChannelMember = response.data;
    return newMember;
  } catch (error) {
    console.error(error);
    return false;
  }
}

/**
 * Removes a member from a specific channel.
 * @param channelId The ID of the channel.
 * @param userId The ID of the member to remove.
 * @returns A promise that resolves to false if the operation fails.
 */
export async function RemoveChannelMember(channelId: number, userId: number) {
  try {
    await axiosInstance.delete(`/api/channelmember/${channelId}/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

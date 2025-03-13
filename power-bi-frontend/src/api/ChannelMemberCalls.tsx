import { axiosInstance } from "./common";
import { ChannelMember } from "../models/ChannelMember";

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

export async function RemoveChannelMember(channelId: number, userId: number) {
  try {
    await axiosInstance.delete(`/api/channelmember/${channelId}/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

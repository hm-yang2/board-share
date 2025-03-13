import { ChannelOwner } from "../models/ChannelOwner";
import { axiosInstance } from "./common";

export async function GetChannelOwners(channelId: string) {
  try {
    const response = await axiosInstance.get(`/api/channelowner/${channelId}`);
    const owners: ChannelOwner[] = response.data;
    return owners;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function AddChannelOwner(channelId: string, userId: string) {
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

export async function RemoveChannelOwner(channelId: string, userId: string) {
  try {
    await axiosInstance.delete(`/api/channelowner/${channelId}/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

import { ChannelAdmin } from "../models/ChannelAdmin";
import { axiosInstance } from "./common";

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

export async function RemoveChannelAdmin(channelId: number, userId: number) {
  try {
    await axiosInstance.delete(`/api/channeladmin/${channelId}/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

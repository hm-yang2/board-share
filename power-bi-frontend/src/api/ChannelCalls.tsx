import { Channel } from "../models/Channel";
import { ChannelDTO } from "../models/ChannelDTO";
import { axiosInstance } from "./common";

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

export async function DeleteChannel(channelId: number) {
  try {
    await axiosInstance.delete(`/api/channel/${channelId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

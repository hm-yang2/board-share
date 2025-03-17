import { ChannelLink } from "../models/ChannelLink";
import { ChannelLinkDTO } from "../models/ChannelLinkDTO";
import { axiosInstance } from "./common";

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

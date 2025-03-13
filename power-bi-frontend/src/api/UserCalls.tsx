import { User } from "../models/User";
import { axiosInstance } from "./common";

export async function GetUsers(search?: string) {
  try {
    const response = await axiosInstance.get("/api/user", {
      params: search ? { search } : {},
    });
    const users: User[] = response.data;
    return users;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function GetUser(userId: number) {
  try {
    const response = await axiosInstance.get(`/api/user/${userId}`);
    const user: User = response.data;
    return user;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export async function DeleteUser(userId: number) {
  try {
    await axiosInstance.delete(`/api/user/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

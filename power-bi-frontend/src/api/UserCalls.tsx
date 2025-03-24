import { User } from "../models/User";
import { axiosInstance } from "./common";

/**
 * Fetches a list of users from the server.
 * @param search Optional search query to filter users.
 * @returns A promise that resolves to an array of User objects.
 */
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

/**
 * Fetches a specific user by their ID.
 * @param userId The ID of the user to fetch.
 * @returns A promise that resolves to a User object or null if not found.
 */
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

/**
 * Fetches the currently authenticated user's details.
 * @returns A promise that resolves to a User object or null if not authenticated.
 */
export async function GetSelf() {
  try {
    const response = await axiosInstance.get(`/api/user/self`);
    console.log(response.data);
    const user: User = response.data;
    return user;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Deletes a user by their ID.
 * @param userId The ID of the user to delete.
 * @returns A promise that resolves to false if the deletion fails.
 */
export async function DeleteUser(userId: number) {
  try {
    await axiosInstance.delete(`/api/user/${userId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

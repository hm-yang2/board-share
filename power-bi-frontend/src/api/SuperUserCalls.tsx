import { SuperUser } from "../models/SuperUser";
import { axiosInstance } from "./common";

/**
 * Fetches a list of all super users.
 * @returns A promise that resolves to an array of SuperUser objects.
 */
export async function GetSuperUsers() {
  try {
    const response = await axiosInstance.get("/api/superuser");
    const superUsers: SuperUser[] = response.data;
    return superUsers;
  } catch (error) {
    console.error(error);
    return [];
  }
}

/**
 * Adds a user as a super user.
 * @param userId The ID of the user to add as a super user.
 * @returns A promise that resolves to the newly created SuperUser object or null if the operation fails.
 */
export async function AddSuperUser(userId: number) {
  try {
    const response = await axiosInstance.put("/api/superuser", { id: userId });
    const newSuperUser: SuperUser = response.data;
    return newSuperUser;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Deletes a super user by their ID.
 * @param superId The ID of the super user to delete.
 * @returns A promise that resolves to false if the deletion fails.
 */
export async function DeleteSuperUser(superId: number) {
  try {
    await axiosInstance.delete(`/api/superuser/${superId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

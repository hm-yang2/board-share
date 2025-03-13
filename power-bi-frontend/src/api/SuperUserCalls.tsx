import { SuperUser } from "../models/SuperUser";
import { axiosInstance } from "./common";

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

export async function DeleteSuperUser(superId: number) {
  try {
    await axiosInstance.delete(`/api/superuser/${superId}`);
  } catch (error) {
    console.error(error);
    return false;
  }
}

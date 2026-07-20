import apiClient from "../../api/apiClient";

export function getAllUsers() {
  return apiClient.get("/users").then((response) => response.data);
}

export function getUserById(id) {
  return apiClient.get(`/users/${id}`).then((response) => response.data);
}

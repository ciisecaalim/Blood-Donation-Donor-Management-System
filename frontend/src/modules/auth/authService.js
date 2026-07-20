import apiClient from "../../api/apiClient";

export function registerUser(data) {
  return apiClient.post("/auth/register", data).then((response) => response.data);
}

export function loginUser(data) {
  return apiClient.post("/auth/login", data).then((response) => response.data);
}

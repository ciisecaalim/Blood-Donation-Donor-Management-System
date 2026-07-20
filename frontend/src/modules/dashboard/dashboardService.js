import apiClient from "../../api/apiClient";

export function getDashboard() {
  return apiClient.get("/dashboard").then((response) => response.data);
}

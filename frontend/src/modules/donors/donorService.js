import apiClient from "../../api/apiClient";

export function getAllDonors() {
  return apiClient.get("/donors").then((response) => response.data);
}

export function getDonorById(id) {
  return apiClient.get(`/donors/${id}`).then((response) => response.data);
}

export function createDonor(data) {
  return apiClient.post("/donors", data).then((response) => response.data);
}

export function updateDonor(id, data) {
  return apiClient.put(`/donors/${id}`, data).then((response) => response.data);
}

export function deleteDonor(id) {
  return apiClient.delete(`/donors/${id}`).then((response) => response.data);
}

import apiClient from "../../api/apiClient";

export function getAllDonations() {
  return apiClient.get("/donations").then((response) => response.data);
}

export function getDonationById(id) {
  return apiClient.get(`/donations/${id}`).then((response) => response.data);
}

export function createDonation(data) {
  return apiClient.post("/donations", data).then((response) => response.data);
}

export function updateDonation(id, data) {
  return apiClient.put(`/donations/${id}`, data).then((response) => response.data);
}

export function deleteDonation(id) {
  return apiClient.delete(`/donations/${id}`).then((response) => response.data);
}

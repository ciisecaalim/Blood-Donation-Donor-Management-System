import apiClient from "../../api/apiClient";

export function getAllAnnouncements() {
  return apiClient.get("/announcements").then((response) => response.data);
}

export function getActiveAnnouncements() {
  return apiClient.get("/announcements/active").then((response) => response.data);
}

export function getAnnouncementById(id) {
  return apiClient.get(`/announcements/${id}`).then((response) => response.data);
}

export function createAnnouncement(data) {
  return apiClient.post("/announcements", data).then((response) => response.data);
}

export function updateAnnouncement(id, data) {
  return apiClient.put(`/announcements/${id}`, data).then((response) => response.data);
}

export function updateAnnouncementStatus(id, status) {
  return apiClient.patch(`/announcements/${id}/status`, { status }).then((response) => response.data);
}

export function deleteAnnouncement(id) {
  return apiClient.delete(`/announcements/${id}`).then((response) => response.data);
}

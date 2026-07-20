import { useEffect, useReducer, useState } from "react";
import StandardModulePage from "../../shared/StandardModulePage";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesValue, formatDate } from "../../utils/filterHelpers";
import { getRole } from "../../utils/storage";
import { getActiveCategories } from "../categories/categoryService";
import { createAnnouncement, deleteAnnouncement, getActiveAnnouncements, getAllAnnouncements, updateAnnouncement, updateAnnouncementStatus } from "./announcementService";

const emptyFilters = { title: "", location: "", bloodGroup: "", status: "", urgency: "" };
const emptyForm = { title: "", categoryId: "", quantityNeeded: "", urgency: "NORMAL", location: "", description: "", status: "ACTIVE" };
const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

function AnnouncementsPage() {
  const role = getRole();
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState(emptyFilters);
  const [formData, setFormData] = useState(emptyForm);
  const [categories, setCategories] = useState([]);
  const [selected, setSelected] = useState(null);
  const [deleteRecord, setDeleteRecord] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [formError, setFormError] = useState("");

  async function loadAnnouncements() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = role === "ROLE_ADMIN" ? await getAllAnnouncements() : await getActiveAnnouncements();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  // Load announcements and dropdown categories when the page opens.
  useEffect(() => {
    loadAnnouncements();
    getActiveCategories().then(setCategories).catch(() => setCategories([]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function openCreate() { setSelected(null); setFormData(emptyForm); setFormError(""); setModalOpen(true); }
  function openEdit(record) { setSelected(record); setFormData({ title: record.title || "", categoryId: "", quantityNeeded: record.quantityNeeded || "", urgency: record.urgency || "NORMAL", location: record.location || "", description: record.description || "", status: record.status || "ACTIVE" }); setFormError(""); setModalOpen(true); }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setFormError("");
    try {
      if (selected) {
        await updateAnnouncement(selected.id, { title: formData.title, quantityNeeded: Number(formData.quantityNeeded), urgency: formData.urgency, location: formData.location, description: formData.description });
      } else {
        await createAnnouncement({ title: formData.title, categoryId: Number(formData.categoryId), quantityNeeded: Number(formData.quantityNeeded), urgency: formData.urgency, location: formData.location, description: formData.description });
      }
      setModalOpen(false);
      loadAnnouncements();
    } catch (error) { setFormError(error.message); } finally { setSaving(false); }
  }

  async function handleStatus(record) {
    const nextStatus = record.status === "ACTIVE" ? "FULFILLED" : "ACTIVE";
    try { await updateAnnouncementStatus(record.id, nextStatus); loadAnnouncements(); } catch (error) { dispatch({ type: "FETCH_ERROR", payload: error.message }); }
  }

  async function handleDelete() {
    setDeleting(true);
    try { await deleteAnnouncement(deleteRecord.id); setDeleteRecord(null); loadAnnouncements(); } catch (error) { dispatch({ type: "FETCH_ERROR", payload: error.message }); } finally { setDeleting(false); }
  }

  const filteredAnnouncements = state.data.filter((item) => includesText(item.title, filters.title) && includesText(item.location, filters.location) && matchesValue(item.bloodGroup, filters.bloodGroup) && matchesValue(item.status, filters.status) && matchesValue(item.urgency, filters.urgency));
  const summaryItems = [{ title: "Total Announcements", value: state.data.length }, { title: "Active Announcements", value: state.data.filter((item) => item.status === "ACTIVE").length }, { title: "Fulfilled Announcements", value: state.data.filter((item) => item.status === "FULFILLED").length }, { title: "Cancelled Announcements", value: state.data.filter((item) => item.status === "CANCELLED").length }];

  return <StandardModulePage title="Announcements" description="View and manage blood need announcements." summaryItems={summaryItems} filters={[{ name: "title", label: "Search by title" }, { name: "location", label: "Search by location" }, { name: "bloodGroup", label: "Blood group", type: "select", options: bloodGroups }, { name: "status", label: "Announcement status", type: "select", options: ["ACTIVE", "FULFILLED", "CANCELLED"] }, { name: "urgency", label: "Urgency level", type: "select", options: ["NORMAL", "URGENT", "EMERGENCY"] }]} filterValues={filters} onFilterChange={(name, value) => setFilters({ ...filters, [name]: value })} onClearFilters={() => setFilters(emptyFilters)} columns={[{ key: "title", label: "Title" }, { key: "bloodGroup", label: "Group" }, { key: "quantityNeeded", label: "Units Needed" }, { key: "urgency", label: "Urgency", type: "status" }, { key: "location", label: "Location" }, { key: "status", label: "Status", type: "status" }, { key: "createdAt", label: "Created", render: (row) => formatDate(row.createdAt) }]} records={filteredAnnouncements} loading={state.loading} error={state.error} primaryAction={role === "ROLE_ADMIN" ? { label: "Add Announcement", onClick: openCreate } : null} rowActions={role === "ROLE_ADMIN" ? [{ label: "Edit", onClick: openEdit }, { label: "Change Status", onClick: handleStatus }, { label: "Delete", onClick: setDeleteRecord }] : []} emptyMessage="No announcements match the selected filters." modal={{ open: modalOpen, title: selected ? "Edit Announcement" : "Add Announcement", error: formError, saving, submitLabel: selected ? "Update Announcement" : "Create Announcement", onSubmit: handleSubmit, onClose: () => setModalOpen(false), children: <AnnouncementForm formData={formData} setFormData={setFormData} categories={categories} selected={selected} /> }} deleteModal={{ open: Boolean(deleteRecord), title: "Delete Announcement", message: `Delete ${deleteRecord?.title || "this announcement"}?`, loading: deleting, onCancel: () => setDeleteRecord(null), onConfirm: handleDelete }} />;
}

function AnnouncementForm({ formData, setFormData, categories, selected }) {
  return <><label className="block text-sm font-medium text-gray-600">Title<input className="form-input mt-1" value={formData.title} onChange={(event) => setFormData({ ...formData, title: event.target.value })} required /></label>{!selected && <label className="block text-sm font-medium text-gray-600">Blood category<select className="form-input mt-1" value={formData.categoryId} onChange={(event) => setFormData({ ...formData, categoryId: event.target.value })} required><option value="">Select group</option>{categories.map((item) => <option key={item.id} value={item.id}>{item.bloodGroup}</option>)}</select></label>}<div className="grid grid-cols-1 gap-4 sm:grid-cols-2"><label className="block text-sm font-medium text-gray-600">Quantity needed<input className="form-input mt-1" type="number" value={formData.quantityNeeded} onChange={(event) => setFormData({ ...formData, quantityNeeded: event.target.value })} required /></label><label className="block text-sm font-medium text-gray-600">Urgency<select className="form-input mt-1" value={formData.urgency} onChange={(event) => setFormData({ ...formData, urgency: event.target.value })} required><option value="NORMAL">NORMAL</option><option value="URGENT">URGENT</option><option value="EMERGENCY">EMERGENCY</option></select></label></div><label className="block text-sm font-medium text-gray-600">Location<input className="form-input mt-1" value={formData.location} onChange={(event) => setFormData({ ...formData, location: event.target.value })} required /></label><label className="block text-sm font-medium text-gray-600">Description<textarea className="form-input mt-1" rows="3" value={formData.description} onChange={(event) => setFormData({ ...formData, description: event.target.value })} /></label></>;
}

export default AnnouncementsPage;




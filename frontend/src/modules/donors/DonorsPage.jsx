import { useEffect, useReducer, useState } from "react";
import StandardModulePage from "../../shared/StandardModulePage";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesValue, formatDate } from "../../utils/filterHelpers";
import { getRole } from "../../utils/storage";
import { getAllUsers } from "../users/userService";
import { getActiveCategories } from "../categories/categoryService";
import { createDonor, deleteDonor, getAllDonors, updateDonor } from "./donorService";

const emptyFilters = { fullName: "", email: "", phone: "", bloodGroup: "", gender: "", status: "" };
const emptyForm = { userId: "", categoryId: "", phone: "", address: "", age: "", gender: "", weight: "" };
const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

function DonorsPage() {
  const role = getRole();
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState(emptyFilters);
  const [formData, setFormData] = useState(emptyForm);
  const [users, setUsers] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selected, setSelected] = useState(null);
  const [deleteRecord, setDeleteRecord] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [formError, setFormError] = useState("");

  async function loadDonors() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getAllDonors();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadDonors();
    getAllUsers().then(setUsers).catch(() => setUsers([]));
    getActiveCategories().then(setCategories).catch(() => setCategories([]));
  }, []);

  function openCreate() {
    setSelected(null);
    setFormData(emptyForm);
    setFormError("");
    setModalOpen(true);
  }

  function openEdit(record) {
    setSelected(record);
    setFormData({ userId: "", categoryId: "", phone: record.phone || "", address: record.address || "", age: record.age || "", gender: record.gender || "", weight: record.weight || "" });
    setFormError("");
    setModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setFormError("");

    const payload = { ...formData, age: Number(formData.age), weight: Number(formData.weight) };

    try {
      if (selected) {
        await updateDonor(selected.id, { phone: payload.phone, address: payload.address, age: payload.age, gender: payload.gender, weight: payload.weight });
      } else {
        await createDonor({ ...payload, userId: Number(payload.userId), categoryId: Number(payload.categoryId) });
      }
      setModalOpen(false);
      loadDonors();
    } catch (error) {
      setFormError(error.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    setDeleting(true);
    try {
      await deleteDonor(deleteRecord.id);
      setDeleteRecord(null);
      loadDonors();
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    } finally {
      setDeleting(false);
    }
  }

  const filteredDonors = state.data.filter((item) => includesText(item.fullName, filters.fullName) && includesText(item.email, filters.email) && includesText(item.phone, filters.phone) && matchesValue(item.bloodGroup, filters.bloodGroup) && matchesValue(item.gender, filters.gender) && matchesValue(item.status, filters.status));
  const summaryItems = [
    { title: "Total Donors", value: state.data.length },
    { title: "Active Donors", value: state.data.filter((item) => item.status === "ACTIVE").length },
    { title: "Temporarily Ineligible Donors", value: state.data.filter((item) => item.status === "TEMPORARILY_INELIGIBLE").length },
    { title: "Inactive Donors", value: state.data.filter((item) => item.status === "INACTIVE").length },
  ];

  return (
    <StandardModulePage
      title="Donors"
      description="Manage donor profiles and eligibility information."
      summaryItems={summaryItems}
      filters={[{ name: "fullName", label: "Search by donor name" }, { name: "email", label: "Search by email" }, { name: "phone", label: "Search by phone" }, { name: "bloodGroup", label: "Blood group", type: "select", options: bloodGroups }, { name: "gender", label: "Gender", type: "select", options: ["MALE", "FEMALE"] }, { name: "status", label: "Donor status", type: "select", options: ["ACTIVE", "TEMPORARILY_INELIGIBLE", "INACTIVE"] }]}
      filterValues={filters}
      onFilterChange={(name, value) => setFilters({ ...filters, [name]: value })}
      onClearFilters={() => setFilters(emptyFilters)}
      columns={[{ key: "fullName", label: "Donor" }, { key: "bloodGroup", label: "Group" }, { key: "phone", label: "Phone" }, { key: "gender", label: "Gender" }, { key: "age", label: "Age" }, { key: "weight", label: "Weight" }, { key: "status", label: "Status", type: "status" }, { key: "createdAt", label: "Created", render: (row) => formatDate(row.createdAt) }]}
      records={filteredDonors}
      loading={state.loading}
      error={state.error}
      primaryAction={role !== "ROLE_USER" ? { label: "Add Donor", onClick: openCreate } : null}
      rowActions={role !== "ROLE_USER" ? [{ label: "Edit", onClick: openEdit }, { label: "Delete", onClick: setDeleteRecord }] : []}
      emptyMessage="No donors match the selected filters."
      modal={{ open: modalOpen, title: selected ? "Edit Donor" : "Add Donor", error: formError, saving, submitLabel: selected ? "Update Donor" : "Create Donor", onSubmit: handleSubmit, onClose: () => setModalOpen(false), children: <DonorForm formData={formData} setFormData={setFormData} users={users} categories={categories} selected={selected} /> }}
      deleteModal={{ open: Boolean(deleteRecord), title: "Delete Donor", message: `Delete ${deleteRecord?.fullName || "this donor"}?`, loading: deleting, onCancel: () => setDeleteRecord(null), onConfirm: handleDelete }}
    />
  );
}

function DonorForm({ formData, setFormData, users, categories, selected }) {
  return (
    <>
      {!selected && <label className="block text-sm font-medium text-gray-600">User<select className="form-input mt-1" value={formData.userId} onChange={(event) => setFormData({ ...formData, userId: event.target.value })} required><option value="">Select user</option>{users.map((user) => <option key={user.id} value={user.id}>{user.fullName} - {user.email}</option>)}</select></label>}
      {!selected && <label className="block text-sm font-medium text-gray-600">Blood category<select className="form-input mt-1" value={formData.categoryId} onChange={(event) => setFormData({ ...formData, categoryId: event.target.value })} required><option value="">Select group</option>{categories.map((item) => <option key={item.id} value={item.id}>{item.bloodGroup}</option>)}</select></label>}
      <label className="block text-sm font-medium text-gray-600">Phone<input className="form-input mt-1" value={formData.phone} onChange={(event) => setFormData({ ...formData, phone: event.target.value })} required /></label>
      <label className="block text-sm font-medium text-gray-600">Address<input className="form-input mt-1" value={formData.address} onChange={(event) => setFormData({ ...formData, address: event.target.value })} required /></label>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3"><label className="block text-sm font-medium text-gray-600">Age<input className="form-input mt-1" type="number" value={formData.age} onChange={(event) => setFormData({ ...formData, age: event.target.value })} required /></label><label className="block text-sm font-medium text-gray-600">Gender<select className="form-input mt-1" value={formData.gender} onChange={(event) => setFormData({ ...formData, gender: event.target.value })} required><option value="">Select</option><option value="MALE">MALE</option><option value="FEMALE">FEMALE</option></select></label><label className="block text-sm font-medium text-gray-600">Weight<input className="form-input mt-1" type="number" value={formData.weight} onChange={(event) => setFormData({ ...formData, weight: event.target.value })} required /></label></div>
    </>
  );
}

export default DonorsPage;

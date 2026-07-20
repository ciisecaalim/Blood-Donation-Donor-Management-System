import { useEffect, useReducer, useState } from "react";
import StandardModulePage from "../../shared/StandardModulePage";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesNumberRange, matchesValue, formatDate } from "../../utils/filterHelpers";
import { getRole } from "../../utils/storage";
import { getActiveCategories } from "../categories/categoryService";
import { getAllDonors } from "../donors/donorService";
import { createDonation, deleteDonation, getAllDonations, updateDonation } from "./donationService";

const emptyFilters = { donorName: "", bloodGroup: "", status: "", minQuantity: "", maxQuantity: "" };
const emptyForm = { donorId: "", categoryId: "", quantity: "", donationDate: "", location: "", notes: "" };
const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

function DonationsPage() {
  const role = getRole();
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState(emptyFilters);
  const [formData, setFormData] = useState(emptyForm);
  const [donors, setDonors] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selected, setSelected] = useState(null);
  const [deleteRecord, setDeleteRecord] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [formError, setFormError] = useState("");

  async function loadDonations() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getAllDonations();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadDonations();
    getAllDonors().then(setDonors).catch(() => setDonors([]));
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
    setFormData({ donorId: "", categoryId: "", quantity: record.quantity || "", donationDate: record.donationDate || "", location: record.location || "", notes: record.notes || "" });
    setFormError("");
    setModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setFormError("");
    try {
      if (selected) {
        await updateDonation(selected.id, { quantity: Number(formData.quantity), location: formData.location, notes: formData.notes });
      } else {
        await createDonation({ ...formData, donorId: Number(formData.donorId), categoryId: Number(formData.categoryId), quantity: Number(formData.quantity) });
      }
      setModalOpen(false);
      loadDonations();
    } catch (error) {
      setFormError(error.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    setDeleting(true);
    try {
      await deleteDonation(deleteRecord.id);
      setDeleteRecord(null);
      loadDonations();
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    } finally {
      setDeleting(false);
    }
  }

  const filteredDonations = state.data.filter((item) => includesText(item.donorName, filters.donorName) && matchesValue(item.bloodGroup, filters.bloodGroup) && matchesValue(item.status, filters.status) && matchesNumberRange(item.quantity, filters.minQuantity, filters.maxQuantity));
  const totalUnits = state.data.reduce((sum, item) => sum + Number(item.quantity || 0), 0);
  const summaryItems = [{ title: "Total Donations", value: state.data.length }, { title: "Total Donated Units", value: totalUnits }, { title: "Recorded Donations", value: state.data.filter((item) => item.status === "RECORDED").length }, { title: "Cancelled Donations", value: state.data.filter((item) => item.status === "CANCELLED").length }];

  return (
    <StandardModulePage
      title="Donations"
      description="Track donation records, quantities, dates, and locations."
      summaryItems={summaryItems}
      filters={[{ name: "donorName", label: "Search by donor name" }, { name: "bloodGroup", label: "Blood group", type: "select", options: bloodGroups }, { name: "status", label: "Donation status", type: "select", options: ["PENDING", "APPROVED", "REJECTED", "RECORDED", "CANCELLED"] }, { name: "minQuantity", label: "Minimum quantity", type: "number" }, { name: "maxQuantity", label: "Maximum quantity", type: "number" }]}
      filterValues={filters}
      onFilterChange={(name, value) => setFilters({ ...filters, [name]: value })}
      onClearFilters={() => setFilters(emptyFilters)}
      columns={[{ key: "donorName", label: "Donor" }, { key: "bloodGroup", label: "Group" }, { key: "quantity", label: "Units" }, { key: "donationDate", label: "Date", render: (row) => formatDate(row.donationDate) }, { key: "location", label: "Location" }, { key: "status", label: "Status", type: "status" }]}
      records={filteredDonations}
      loading={state.loading}
      error={state.error}
      primaryAction={role !== "ROLE_USER" ? { label: "Add Donation", onClick: openCreate } : null}
      rowActions={role !== "ROLE_USER" ? [{ label: "Edit", onClick: openEdit }, { label: "Delete", onClick: setDeleteRecord }] : []}
      emptyMessage="No donations match the selected filters."
      modal={{ open: modalOpen, title: selected ? "Edit Donation" : "Add Donation", error: formError, saving, submitLabel: selected ? "Update Donation" : "Create Donation", onSubmit: handleSubmit, onClose: () => setModalOpen(false), children: <DonationForm formData={formData} setFormData={setFormData} donors={donors} categories={categories} selected={selected} /> }}
      deleteModal={{ open: Boolean(deleteRecord), title: "Delete Donation", message: `Delete donation for ${deleteRecord?.donorName || "this donor"}?`, loading: deleting, onCancel: () => setDeleteRecord(null), onConfirm: handleDelete }}
    />
  );
}

function DonationForm({ formData, setFormData, donors, categories, selected }) {
  return <>{!selected && <label className="block text-sm font-medium text-gray-600">Donor<select className="form-input mt-1" value={formData.donorId} onChange={(event) => setFormData({ ...formData, donorId: event.target.value })} required><option value="">Select donor</option>{donors.map((item) => <option key={item.id} value={item.id}>{item.fullName} - {item.bloodGroup}</option>)}</select></label>}{!selected && <label className="block text-sm font-medium text-gray-600">Blood category<select className="form-input mt-1" value={formData.categoryId} onChange={(event) => setFormData({ ...formData, categoryId: event.target.value })} required><option value="">Select group</option>{categories.map((item) => <option key={item.id} value={item.id}>{item.bloodGroup}</option>)}</select></label>}<div className="grid grid-cols-1 gap-4 sm:grid-cols-2"><label className="block text-sm font-medium text-gray-600">Quantity<input className="form-input mt-1" type="number" value={formData.quantity} onChange={(event) => setFormData({ ...formData, quantity: event.target.value })} required /></label>{!selected && <label className="block text-sm font-medium text-gray-600">Donation date<input className="form-input mt-1" type="date" value={formData.donationDate} onChange={(event) => setFormData({ ...formData, donationDate: event.target.value })} required /></label>}</div><label className="block text-sm font-medium text-gray-600">Location<input className="form-input mt-1" value={formData.location} onChange={(event) => setFormData({ ...formData, location: event.target.value })} required /></label><label className="block text-sm font-medium text-gray-600">Notes<textarea className="form-input mt-1" rows="3" value={formData.notes} onChange={(event) => setFormData({ ...formData, notes: event.target.value })} /></label></>;
}

export default DonationsPage;

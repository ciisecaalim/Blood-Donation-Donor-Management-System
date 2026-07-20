import { useEffect, useReducer, useState } from "react";
import StandardModulePage from "../../shared/StandardModulePage";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesNumberRange, matchesValue } from "../../utils/filterHelpers";
import { getRole } from "../../utils/storage";
import { getActiveCategories } from "../categories/categoryService";
import { createInventory, deleteInventory, getInventory, updateInventory } from "./inventoryService";

const emptyFilters = { bloodGroup: "", status: "", minQuantity: "", maxQuantity: "" };
const emptyForm = { categoryId: "", quantity: "", notes: "" };

function InventoryPage() {
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

  async function loadInventory() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getInventory();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadInventory();
    getActiveCategories().then(setCategories).catch(() => setCategories([]));
  }, []);

  function openCreate() { setSelected(null); setFormData(emptyForm); setFormError(""); setModalOpen(true); }
  function openEdit(record) { setSelected(record); setFormData({ categoryId: "", quantity: record.quantity || "", notes: record.notes || "" }); setFormError(""); setModalOpen(true); }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setFormError("");
    try {
      if (selected) {
        await updateInventory(selected.id, { quantity: Number(formData.quantity), notes: formData.notes });
      } else {
        await createInventory({ categoryId: Number(formData.categoryId), quantity: Number(formData.quantity), notes: formData.notes });
      }
      setModalOpen(false);
      loadInventory();
    } catch (error) { setFormError(error.message); } finally { setSaving(false); }
  }

  async function handleDelete() {
    setDeleting(true);
    try { await deleteInventory(deleteRecord.id); setDeleteRecord(null); loadInventory(); } catch (error) { dispatch({ type: "FETCH_ERROR", payload: error.message }); } finally { setDeleting(false); }
  }

  const filteredInventory = state.data.filter((item) => includesText(item.bloodGroup, filters.bloodGroup) && matchesValue(item.status, filters.status) && matchesNumberRange(item.quantity, filters.minQuantity, filters.maxQuantity));
  const totalUnits = state.data.reduce((sum, item) => sum + Number(item.quantity || 0), 0);
  const summaryItems = [{ title: "Total Blood Units", value: totalUnits }, { title: "Available Groups", value: state.data.filter((item) => item.status === "AVAILABLE").length }, { title: "Low Stock Groups", value: state.data.filter((item) => item.status === "LOW_STOCK").length }, { title: "Out of Stock Groups", value: state.data.filter((item) => item.status === "OUT_OF_STOCK").length }];

  return <StandardModulePage title="Inventory" description="Monitor blood stock levels and availability." summaryItems={summaryItems} filters={[{ name: "bloodGroup", label: "Search by blood group" }, { name: "status", label: "Inventory status", type: "select", options: ["AVAILABLE", "LOW_STOCK", "OUT_OF_STOCK"] }, { name: "minQuantity", label: "Minimum quantity", type: "number" }, { name: "maxQuantity", label: "Maximum quantity", type: "number" }]} filterValues={filters} onFilterChange={(name, value) => setFilters({ ...filters, [name]: value })} onClearFilters={() => setFilters(emptyFilters)} columns={[{ key: "bloodGroup", label: "Blood Group" }, { key: "quantity", label: "Units" }, { key: "status", label: "Status", type: "status" }, { key: "notes", label: "Notes" }]} records={filteredInventory} loading={state.loading} error={state.error} primaryAction={role !== "ROLE_USER" ? { label: "Add Inventory", onClick: openCreate } : null} rowActions={role !== "ROLE_USER" ? [{ label: "Edit", onClick: openEdit }, { label: "Delete", onClick: setDeleteRecord }] : []} emptyMessage="No inventory records match the selected filters." modal={{ open: modalOpen, title: selected ? "Edit Inventory" : "Add Inventory", error: formError, saving, submitLabel: selected ? "Update Inventory" : "Create Inventory", onSubmit: handleSubmit, onClose: () => setModalOpen(false), children: <InventoryForm formData={formData} setFormData={setFormData} categories={categories} selected={selected} /> }} deleteModal={{ open: Boolean(deleteRecord), title: "Delete Inventory", message: `Delete ${deleteRecord?.bloodGroup || "this inventory record"}?`, loading: deleting, onCancel: () => setDeleteRecord(null), onConfirm: handleDelete }} />;
}

function InventoryForm({ formData, setFormData, categories, selected }) {
  return <>{!selected && <label className="block text-sm font-medium text-gray-600">Blood category<select className="form-input mt-1" value={formData.categoryId} onChange={(event) => setFormData({ ...formData, categoryId: event.target.value })} required><option value="">Select group</option>{categories.map((item) => <option key={item.id} value={item.id}>{item.bloodGroup}</option>)}</select></label>}<label className="block text-sm font-medium text-gray-600">Quantity<input className="form-input mt-1" type="number" value={formData.quantity} onChange={(event) => setFormData({ ...formData, quantity: event.target.value })} required /></label><label className="block text-sm font-medium text-gray-600">Notes<textarea className="form-input mt-1" rows="3" value={formData.notes} onChange={(event) => setFormData({ ...formData, notes: event.target.value })} /></label></>;
}

export default InventoryPage;

import { useEffect, useReducer, useState } from "react";
import DataTable from "../../components/common/DataTable";
import ErrorMessage from "../../components/common/ErrorMessage";
import LoadingState from "../../components/common/LoadingState";
import PageHeader from "../../components/common/PageHeader";
import SummaryGrid from "../../components/common/SummaryGrid";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesValue, formatDate } from "../../utils/filterHelpers";
import { getCompleteSystemReport } from "./reportService";

const sections = ["users", "categories", "donors", "donations", "inventory", "announcements"];
const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

function ReportsPage() {
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState({ search: "", section: "users", bloodGroup: "", status: "" });

  async function loadReport() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getCompleteSystemReport();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadReport();
  }, []);

  if (state.loading) {
    return <LoadingState message="Loading system report..." />;
  }

  const report = state.data || {};
  const records = Array.isArray(report[filters.section]) ? report[filters.section] : [];
  const filteredRecords = records.filter((item) => {
    const textMatch = Object.values(item).some((value) => includesText(value, filters.search));
    return textMatch && matchesValue(item.bloodGroup, filters.bloodGroup) && matchesValue(item.status, filters.status);
  });
  const summary = report.summary || {};
  const summaryItems = [
    { title: "Users", value: summary.totalUsers || report.users?.length || 0 },
    { title: "Categories", value: summary.totalCategories || report.categories?.length || 0 },
    { title: "Donors", value: summary.totalDonors || report.donors?.length || 0 },
    { title: "Donations", value: summary.totalDonations || report.donations?.length || 0 },
  ];

  return (
    <div className="space-y-6">
      <PageHeader title="Complete System Report" description={`Generated at: ${formatDate(report.generatedAt)}`} action={<button className="btn-primary" type="button" onClick={() => window.print()}>Print</button>} />
      <ErrorMessage message={state.error} />
      <SummaryGrid items={summaryItems} />
      <section className="content-card">
        <div className="grid grid-cols-1 gap-3 md:grid-cols-4">
          <input className="form-input" placeholder="Global search" value={filters.search} onChange={(event) => setFilters({ ...filters, search: event.target.value })} />
          <select className="form-input" value={filters.section} onChange={(event) => setFilters({ ...filters, section: event.target.value })}>{sections.map((item) => <option key={item} value={item}>{item}</option>)}</select>
          <select className="form-input" value={filters.bloodGroup} onChange={(event) => setFilters({ ...filters, bloodGroup: event.target.value })}><option value="">All blood groups</option>{bloodGroups.map((item) => <option key={item} value={item}>{item}</option>)}</select>
          <input className="form-input" placeholder="Status" value={filters.status} onChange={(event) => setFilters({ ...filters, status: event.target.value })} />
        </div>
        <button className="btn-secondary mt-4" type="button" onClick={() => setFilters({ search: "", section: "users", bloodGroup: "", status: "" })}>Clear Filters</button>
      </section>
      <section className="content-card">
        <h2 className="mb-4 text-lg font-bold capitalize text-ink">{filters.section}</h2>
        <DataTable columns={makeColumns(filteredRecords)} rows={filteredRecords} loading={false} emptyMessage="No report records match the filters." />
      </section>
    </div>
  );
}

function makeColumns(records) {
  if (!records.length) {
    return [{ key: "id", label: "ID" }];
  }

  return Object.keys(records[0]).slice(0, 8).map((key) => ({ key, label: key.replace(/([A-Z])/g, " $1").replace(/^./, (letter) => letter.toUpperCase()), type: key === "status" || key === "role" ? "status" : undefined }));
}

export default ReportsPage;

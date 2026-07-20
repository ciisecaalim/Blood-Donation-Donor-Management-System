import { useEffect, useReducer, useState } from "react";
import ErrorMessage from "../../components/common/ErrorMessage";
import LoadingState from "../../components/common/LoadingState";
import PageHeader from "../../components/common/PageHeader";
import SummaryGrid from "../../components/common/SummaryGrid";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { getDashboard } from "./dashboardService";

function DashboardPage() {
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [lastRefresh, setLastRefresh] = useState("");

  async function loadDashboard() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getDashboard();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
      setLastRefresh(new Date().toLocaleTimeString());
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  // Initial backend fetch for dashboard numbers.
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadDashboard();
  }, []);

  const data = state.data || {};
  const summaryItems = [
    { title: "Total Donors", value: data.totalDonors || 0 },
    { title: "Total Donations", value: data.totalDonations || 0 },
    { title: "Total Blood Units", value: data.totalBloodUnits || 0 },
    { title: "Total Announcements", value: data.totalAnnouncements || 0 },
    { title: "Active Announcements", value: data.activeAnnouncements || 0 },
    { title: "Available Groups", value: data.availableGroups || 0 },
    { title: "Low Stock Groups", value: data.lowStockGroups || 0 },
    { title: "Out of Stock Groups", value: data.outOfStockGroups || 0 },
  ];

  if (state.loading) {
    return <LoadingState message="Loading dashboard..." />;
  }

  return (
    <div className="space-y-6">
      <PageHeader title="Dashboard" description={`Welcome back. Today is ${new Date().toLocaleDateString()}. Last refresh: ${lastRefresh || "Not refreshed yet"}`} action={<button className="btn-primary" type="button" onClick={loadDashboard}>Refresh</button>} />
      <ErrorMessage message={state.error} />
      <SummaryGrid items={summaryItems} />
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <section className="content-card">
          <h2 className="text-lg font-bold text-ink">Blood Inventory Overview</h2>
          <div className="mt-4 space-y-3">
            <Progress label="Available groups" value={data.availableGroups || 0} total={8} />
            <Progress label="Low stock groups" value={data.lowStockGroups || 0} total={8} />
            <Progress label="Out of stock groups" value={data.outOfStockGroups || 0} total={8} />
          </div>
        </section>
        <section className="content-card">
          <h2 className="text-lg font-bold text-ink">System Status Overview</h2>
          <ul className="mt-4 space-y-3 text-sm text-gray-600">
            <li className="flex justify-between"><span>Donor records</span><strong>{data.totalDonors || 0}</strong></li>
            <li className="flex justify-between"><span>Donation records</span><strong>{data.totalDonations || 0}</strong></li>
            <li className="flex justify-between"><span>Active requests</span><strong>{data.activeAnnouncements || 0}</strong></li>
          </ul>
        </section>
        <section className="content-card">
          <h2 className="text-lg font-bold text-ink">Low-stock Warning</h2>
          <p className="mt-3 text-sm text-gray-600">{data.lowStockGroups ? `${data.lowStockGroups} blood group(s) are low in stock.` : "No low-stock groups reported."}</p>
        </section>
        <section className="content-card">
          <h2 className="text-lg font-bold text-ink">Out-of-stock Warning</h2>
          <p className="mt-3 text-sm text-gray-600">{data.outOfStockGroups ? `${data.outOfStockGroups} blood group(s) are out of stock.` : "No out-of-stock groups reported."}</p>
        </section>
      </div>
    </div>
  );
}

function Progress({ label, value, total }) {
  const percent = total ? Math.min(100, Math.round((value / total) * 100)) : 0;
  return <div><div className="mb-1 flex justify-between text-sm text-gray-600"><span>{label}</span><span>{value}</span></div><div className="h-2 rounded-full bg-gray-100"><div className="h-2 rounded-full bg-blood" style={{ width: `${percent}%` }} /></div></div>;
}

export default DashboardPage;




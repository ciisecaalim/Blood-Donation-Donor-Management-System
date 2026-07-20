import { useState } from "react";
import { Link } from "react-router-dom";
import PageHeader from "../components/common/PageHeader";
import ErrorMessage from "../components/common/ErrorMessage";
import { getUser, saveAuthData } from "../utils/storage";

function ProfilePage() {
  const [user, setUser] = useState(getUser() || { fullName: "System User", email: "user@system.local", role: "ROLE_USER", status: "ACTIVE" });
  const [editForm, setEditForm] = useState({ fullName: user.fullName, email: user.email });
  const [securityForm, setSecurityForm] = useState({ currentPassword: "", newPassword: "", confirmPassword: "" });
  const [infoMessage, setInfoMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [savingDetails, setSavingDetails] = useState(false);
  const [savingSecurity, setSavingSecurity] = useState(false);

  // Mock checklist state for Staff
  const [tasks, setTasks] = useState([
    { id: 1, text: "Verify pending donor registration records", completed: true },
    { id: 2, text: "Check inventory logs for O- low-stock warning", completed: false },
    { id: 3, text: "Draft announcement for upcoming weekend blood drive", completed: false },
  ]);

  function handleTaskToggle(id) {
    setTasks(tasks.map(t => t.id === id ? { ...t, completed: !t.completed } : t));
  }

  function handleDetailsSubmit(e) {
    e.preventDefault();
    setSavingDetails(true);
    setInfoMessage("");
    setErrorMessage("");

    setTimeout(() => {
      try {
        const updatedUser = { ...user, fullName: editForm.fullName, email: editForm.email };
        // Save to local storage
        saveAuthData({
          token: localStorage.getItem("token") || "",
          role: user.role,
          userId: user.id || "demo-user",
          fullName: editForm.fullName,
          email: editForm.email,
          status: user.status
        });
        setUser(updatedUser);
        setInfoMessage("Profile details updated successfully!");
        // Refresh page header / sidebar by custom trigger if necessary (reloads window after short delay)
        setTimeout(() => window.location.reload(), 1000);
      } catch (err) {
        setErrorMessage("Failed to save changes. Please try again.");
      } finally {
        setSavingDetails(false);
      }
    }, 800);
  }

  function handleSecuritySubmit(e) {
    e.preventDefault();
    setSavingSecurity(true);
    setInfoMessage("");
    setErrorMessage("");

    if (securityForm.newPassword !== securityForm.confirmPassword) {
      setErrorMessage("New password and confirmation do not match.");
      setSavingSecurity(false);
      return;
    }

    setTimeout(() => {
      setInfoMessage("Password updated successfully!");
      setSecurityForm({ currentPassword: "", newPassword: "", confirmPassword: "" });
      setSavingSecurity(false);
    }, 1000);
  }

  // Get compatible blood types for ROLE_USER
  function getCompatibility(bloodType) {
    const defaultComp = { give: ["All Groups"], receive: ["All Groups"] };
    if (!bloodType) return defaultComp;
    const comps = {
      "O-": { give: ["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"], receive: ["O-"] },
      "O+": { give: ["O+", "A+", "B+", "AB+"], receive: ["O-", "O+"] },
      "A-": { give: ["A-", "A+", "AB-", "AB+"], receive: ["O-", "A-"] },
      "A+": { give: ["A+", "AB+"], receive: ["O-", "O+", "A-", "A+"] },
      "B-": { give: ["B-", "B+", "AB-", "AB+"], receive: ["O-", "B-"] },
      "B+": { give: ["B+", "AB+"], receive: ["O-", "O+", "B-", "B+"] },
      "AB-": { give: ["AB-", "AB+"], receive: ["O-", "A-", "B-", "AB-"] },
      "AB+": { give: ["AB+"], receive: ["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"] },
    };
    return comps[bloodType.toUpperCase()] || defaultComp;
  }

  const roleText = user.role?.replace("ROLE_", "") || "USER";
  const userBloodType = "O+"; // Mock blood type for demo user

  return (
    <div className="space-y-6">
      <PageHeader
        title="Profile Dashboard"
        description="View your account details, manage configurations, and update security credentials."
      />

      {infoMessage && (
        <div className="flex items-center gap-3 rounded-xl bg-emerald-50 border border-emerald-200 p-4 text-sm font-semibold text-emerald-800 animate-fadeIn">
          <svg className="h-5 w-5 text-emerald-600 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {infoMessage}
        </div>
      )}

      {errorMessage && <ErrorMessage message={errorMessage} />}

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
        {/* Profile Card & Info */}
        <div className="lg:col-span-1 space-y-6">
          <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm flex flex-col items-center text-center">
            <div className="relative">
              <div className="flex h-24 w-24 items-center justify-center rounded-2xl bg-gradient-to-br from-red-500 to-red-700 text-3xl font-black text-white shadow-xl shadow-red-200 uppercase">
                {user.fullName ? user.fullName.split(" ").map(n => n[0]).join("").substring(0, 2) : "US"}
              </div>
              <span className="absolute bottom-0 right-0 h-4 w-4 rounded-full bg-green-500 border-2 border-white shadow-sm" />
            </div>

            <h2 className="mt-4 text-lg font-bold text-ink">{user.fullName}</h2>
            <p className="text-xs text-gray-500 font-medium">{user.email}</p>

            <div className="mt-4 flex flex-wrap gap-2 justify-center">
              <span className="rounded-full bg-red-50 px-3 py-1 text-xs font-bold text-blood tracking-wider capitalize border border-red-100">
                {roleText.toLowerCase()}
              </span>
              <span className="rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-emerald-700 tracking-wider uppercase border border-emerald-100">
                {user.status || "ACTIVE"}
              </span>
            </div>

            <div className="mt-6 w-full border-t border-gray-100 pt-5 space-y-3 text-left">
              <div className="flex justify-between text-xs text-gray-500 font-medium">
                <span>Account ID</span>
                <span className="font-bold text-ink">{user.id || "USR-2026-90"}</span>
              </div>
              <div className="flex justify-between text-xs text-gray-500 font-medium">
                <span>Joined Date</span>
                <span className="font-bold text-ink">July 15, 2026</span>
              </div>
            </div>
          </div>

          {/* Quick Access or Blood Compatibility Info */}
          {user.role === "ROLE_USER" && (
            <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-4">
              <h3 className="text-sm font-extrabold text-ink tracking-tight flex items-center gap-2">
                <svg className="h-5 w-5 text-blood" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
                </svg>
                Blood Compatibility ({userBloodType})
              </h3>
              
              <div className="space-y-3">
                <div>
                  <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide">Can Donate To</p>
                  <div className="mt-1 flex flex-wrap gap-1.5">
                    {getCompatibility(userBloodType).give.map((grp) => (
                      <span key={grp} className="bg-red-50 text-blood text-[10px] font-black px-2 py-0.5 rounded-lg border border-red-100">
                        {grp}
                      </span>
                    ))}
                  </div>
                </div>

                <div>
                  <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide">Can Receive From</p>
                  <div className="mt-1 flex flex-wrap gap-1.5">
                    {getCompatibility(userBloodType).receive.map((grp) => (
                      <span key={grp} className="bg-blue-50 text-blue-600 text-[10px] font-black px-2 py-0.5 rounded-lg border border-blue-100">
                        {grp}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Dynamic Dashboards and Forms */}
        <div className="lg:col-span-2 space-y-6">
          
          {/* 1. ADMIN ROLE PORTAL */}
          {user.role === "ROLE_ADMIN" && (
            <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-6">
              <div className="flex items-center justify-between border-b border-gray-100 pb-4">
                <h3 className="text-base font-extrabold text-ink tracking-tight">System Administration Portal</h3>
                <span className="bg-purple-50 text-purple-700 text-[10px] font-extrabold px-2 py-0.5 rounded-full border border-purple-100">
                  Admin Exclusive
                </span>
              </div>
              
              {/* Quick Admin Actions Grid */}
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <Link to="/users" className="flex flex-col items-center justify-center p-4 border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors group">
                  <div className="h-10 w-10 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center group-hover:scale-105 transition-transform">
                    <svg className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94-3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z" />
                    </svg>
                  </div>
                  <span className="mt-2 text-xs font-bold text-gray-700">Manage Users</span>
                </Link>

                <Link to="/reports" className="flex flex-col items-center justify-center p-4 border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors group">
                  <div className="h-10 w-10 rounded-lg bg-amber-50 text-amber-600 flex items-center justify-center group-hover:scale-105 transition-transform">
                    <svg className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 6a7.5 7.5 0 107.5 7.5h-7.5V6z" />
                      <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 10.5H21A7.5 7.5 0 0013.5 3v7.5z" />
                    </svg>
                  </div>
                  <span className="mt-2 text-xs font-bold text-gray-700">Analytics & Reports</span>
                </Link>

                <Link to="/categories" className="flex flex-col items-center justify-center p-4 border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors group">
                  <div className="h-10 w-10 rounded-lg bg-rose-50 text-rose-600 flex items-center justify-center group-hover:scale-105 transition-transform">
                    <svg className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 6A2.25 2.25 0 016 3.75h2.25A2.25 2.25 0 0110.5 6v2.25a2.25 2.25 0 01-2.25 2.25H6a2.25 2.25 0 01-2.25-2.25V6zM3.75 15.75A2.25 2.25 0 016 13.5h2.25a2.25 2.25 0 012.25 2.25V18a2.25 2.25 0 01-2.25 2.25H6A2.25 2.25 0 013.75 18v-2.25zM13.5 6a2.25 2.25 0 012.25-2.25H18A2.25 2.25 0 0120.25 6v2.25A2.25 2.25 0 0118 10.5h-2.25a2.25 2.25 0 01-2.25-2.25V6zM13.5 15.75a2.25 2.25 0 012.25-2.25H18a2.25 2.25 0 012.25 2.25V18A2.25 2.25 0 0118 20.25h-2.25A2.25 2.25 0 0113.5 18v-2.25z" />
                    </svg>
                  </div>
                  <span className="mt-2 text-xs font-bold text-gray-700">Categories Grid</span>
                </Link>
              </div>

              {/* Admin simulated log */}
              <div className="space-y-3">
                <p className="text-xs font-bold text-gray-400 uppercase tracking-wider">Recent Admin Activity Log</p>
                <div className="border border-gray-150 rounded-xl divide-y divide-gray-100 overflow-hidden text-xs">
                  <div className="p-3 bg-gray-50 flex items-center justify-between text-gray-600">
                    <span>Performed system cleanup of temporary sessions</span>
                    <span className="font-semibold text-gray-400">10 mins ago</span>
                  </div>
                  <div className="p-3 bg-white flex items-center justify-between text-gray-600">
                    <span>Approved status change for category AB-</span>
                    <span className="font-semibold text-gray-400">1 hour ago</span>
                  </div>
                  <div className="p-3 bg-gray-50 flex items-center justify-between text-gray-600">
                    <span>Added new announcement for blood donation drive</span>
                    <span className="font-semibold text-gray-400">Yesterday</span>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* 2. STAFF ROLE PORTAL */}
          {user.role === "ROLE_STAFF" && (
            <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-6">
              <div className="flex items-center justify-between border-b border-gray-100 pb-4">
                <h3 className="text-base font-extrabold text-ink tracking-tight">Medical Staff Workspace</h3>
                <span className="bg-emerald-50 text-emerald-700 text-[10px] font-extrabold px-2 py-0.5 rounded-full border border-emerald-100">
                  Staff View
                </span>
              </div>

              {/* Tasks List */}
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <p className="text-xs font-bold text-gray-400 uppercase tracking-wider">My Shift Tasks Checklist</p>
                  <span className="text-[10px] text-gray-500 font-bold bg-gray-100 px-2 py-0.5 rounded">
                    {tasks.filter(t => t.completed).length}/{tasks.length} Completed
                  </span>
                </div>
                <div className="space-y-2">
                  {tasks.map((task) => (
                    <label key={task.id} className="flex items-center gap-3 p-3 border border-gray-200 rounded-xl hover:bg-gray-50 cursor-pointer transition-colors">
                      <input
                        type="checkbox"
                        checked={task.completed}
                        onChange={() => handleTaskToggle(task.id)}
                        className="rounded border-gray-300 text-blood focus:ring-blood h-4 w-4"
                      />
                      <span className={`text-xs font-semibold ${task.completed ? "line-through text-gray-400" : "text-gray-700"}`}>
                        {task.text}
                      </span>
                    </label>
                  ))}
                </div>
              </div>

              {/* Staff quick actions */}
              <div className="flex gap-3">
                <Link to="/donors" className="btn-secondary w-full text-center text-xs py-2.5 font-bold">
                  Manage Donors list
                </Link>
                <Link to="/donations" className="btn-primary w-full text-center text-xs py-2.5 font-bold">
                  Log New Donation
                </Link>
              </div>
            </div>
          )}

          {/* 3. USER ROLE PORTAL */}
          {user.role === "ROLE_USER" && (
            <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-6">
              <div className="flex items-center justify-between border-b border-gray-100 pb-4">
                <h3 className="text-base font-extrabold text-ink tracking-tight">Donor Timeline & History</h3>
                <span className="bg-red-50 text-blood text-[10px] font-extrabold px-2 py-0.5 rounded-full border border-red-100">
                  Donor Portal
                </span>
              </div>

              <div className="space-y-4">
                <div className="flex items-start gap-4">
                  <div className="h-8 w-8 rounded-full bg-red-100 text-blood flex items-center justify-center font-bold text-xs shrink-0 mt-0.5">
                    1
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-ink">Whole Blood Donation</h4>
                    <p className="text-xs text-gray-500 mt-0.5">City Blood Bank Center - Unit: O+</p>
                    <p className="text-[10px] text-gray-400 font-semibold mt-1">Date: April 12, 2026 | Status: Verified</p>
                  </div>
                </div>

                <div className="flex items-start gap-4">
                  <div className="h-8 w-8 rounded-full bg-red-100 text-blood flex items-center justify-center font-bold text-xs shrink-0 mt-0.5">
                    2
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-ink">Platelets Donation</h4>
                    <p className="text-xs text-gray-500 mt-0.5">Mobile Blood Drive Camp A</p>
                    <p className="text-[10px] text-gray-400 font-semibold mt-1">Date: November 05, 2025 | Status: Verified</p>
                  </div>
                </div>
              </div>

              <div className="rounded-xl bg-gradient-to-r from-red-50 to-rose-50 p-4 border border-red-100 flex items-center gap-3">
                <svg className="h-6 w-6 text-blood shrink-0 animate-bounce" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <div className="text-xs">
                  <p className="font-extrabold text-blood">Eligible for Donation</p>
                  <p className="text-gray-500 mt-0.5 font-medium">It has been more than 56 days since your last donation. You are good to donate again!</p>
                </div>
              </div>
            </div>
          )}

          {/* EDIT DETAILS CARD */}
          <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-4">
            <h3 className="text-sm font-extrabold text-ink border-b border-gray-100 pb-3">Edit Profile Details</h3>
            <form onSubmit={handleDetailsSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-gray-500 uppercase tracking-wide">Full Name</label>
                <input
                  type="text"
                  required
                  value={editForm.fullName}
                  onChange={(e) => setEditForm({ ...editForm, fullName: e.target.value })}
                  className="form-input mt-1 text-xs"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-gray-500 uppercase tracking-wide">Email Address</label>
                <input
                  type="email"
                  required
                  value={editForm.email}
                  onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                  className="form-input mt-1 text-xs"
                />
              </div>

              <button
                type="submit"
                disabled={savingDetails}
                className="btn-primary w-full text-xs font-bold py-2.5"
              >
                {savingDetails ? "Saving details..." : "Save Details"}
              </button>
            </form>
          </div>

          {/* SECURITY MANAGEMENT */}
          <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm space-y-4">
            <h3 className="text-sm font-extrabold text-ink border-b border-gray-100 pb-3">Update Account Security</h3>
            <form onSubmit={handleSecuritySubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-gray-500 uppercase tracking-wide">Current Password</label>
                <input
                  type="password"
                  required
                  placeholder="••••••••"
                  value={securityForm.currentPassword}
                  onChange={(e) => setSecurityForm({ ...securityForm, currentPassword: e.target.value })}
                  className="form-input mt-1 text-xs"
                />
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-gray-500 uppercase tracking-wide">New Password</label>
                  <input
                    type="password"
                    required
                    placeholder="••••••••"
                    value={securityForm.newPassword}
                    onChange={(e) => setSecurityForm({ ...securityForm, newPassword: e.target.value })}
                    className="form-input mt-1 text-xs"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-gray-500 uppercase tracking-wide">Confirm New Password</label>
                  <input
                    type="password"
                    required
                    placeholder="••••••••"
                    value={securityForm.confirmPassword}
                    onChange={(e) => setSecurityForm({ ...securityForm, confirmPassword: e.target.value })}
                    className="form-input mt-1 text-xs"
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={savingSecurity}
                className="btn-secondary w-full text-xs font-bold py-2.5"
              >
                {savingSecurity ? "Updating password..." : "Change Password"}
              </button>
            </form>
          </div>

        </div>
      </div>
    </div>
  );
}

export default ProfilePage;

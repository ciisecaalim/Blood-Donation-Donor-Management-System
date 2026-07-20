import { NavLink, useNavigate } from "react-router-dom";
import { clearAuthData, getRole, getUser } from "../../utils/storage";

// Modern inline SVG Icons for sidebar navigation
const DashboardIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
  </svg>
);

const UsersIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
  </svg>
);

const CategoryIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.656 48.656 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.017.22-.032.441-.046.662M19.5 12l3-3m-3 3l-3-3m-12 3c0 1.232.046 2.453.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7c.017-.22.032-.441.046-.662M4.5 12l-3 3m3-3l3 3" />
  </svg>
);

const DonorsIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />
  </svg>
);

const DonationsIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
);

const InventoryIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5M10 11.25h4M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z" />
  </svg>
);

const AnnouncementsIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.008v.008H9V10zm3.007 0h.008v.008h-.008V10zm3.007 0h.008v.008h-.008V10zm-9.004 8h12a2.25 2.25 0 002.25-2.25V7.25a2.25 2.25 0 00-2.25-2.25h-12a2.25 2.25 0 00-2.25 2.25v8.5c0 1.24.1 2.25 2.25 2.25z" />
  </svg>
);

const ReportsIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z" />
  </svg>
);

const ProfileIcon = () => (
  <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" d="M17.982 18.725A7.488 7.488 0 0012 15.75a7.488 7.488 0 00-5.982 2.975m11.963 0a9 9 0 10-11.963 0m11.963 0A8.966 8.966 0 0112 21a8.966 8.966 0 01-5.982-2.275M15 9.75a3 3 0 11-6 0 3 3 0 016 0z" />
  </svg>
);

const navItems = [
  { label: "Dashboard", path: "/dashboard", roles: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"], icon: DashboardIcon },
  { label: "Users", path: "/users", roles: ["ROLE_ADMIN"], icon: UsersIcon },
  { label: "Blood Categories", path: "/categories", roles: ["ROLE_ADMIN", "ROLE_STAFF"], icon: CategoryIcon },
  { label: "Donors", path: "/donors", roles: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"], icon: DonorsIcon },
  { label: "Donations", path: "/donations", roles: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"], icon: DonationsIcon },
  { label: "Inventory", path: "/inventory", roles: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"], icon: InventoryIcon },
  { label: "Announcements", path: "/announcements", roles: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"], icon: AnnouncementsIcon },
  { label: "Reports", path: "/reports", roles: ["ROLE_ADMIN"], icon: ReportsIcon },
];

function Sidebar({ mobileOpen, onClose }) {
  const navigate = useNavigate();
  const role = getRole();
  const user = getUser();
  const visibleItems = navItems.filter((item) => item.roles.includes(role));

  function handleLogout() {
    clearAuthData();
    navigate("/login");
  }

  return (
    <>
      {mobileOpen && (
        <button
          aria-label="Close menu"
          className="fixed inset-0 z-30 bg-ink/50 backdrop-blur-sm lg:hidden transition-opacity"
          type="button"
          onClick={onClose}
        />
      )}
      <aside
        className={`fixed inset-y-0 left-0 z-40 w-72 bg-gradient-to-b from-ink via-ink to-slate-950 text-white transition-all duration-300 shadow-2xl lg:translate-x-0 ${
          mobileOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-full flex-col px-4 py-6 justify-between">
          <div>
            <div className="mb-8 border-b border-white/10 pb-5 px-2 flex items-center gap-3">
              <div className="h-10 w-10 rounded-xl bg-blood flex items-center justify-center shadow-lg shadow-blood/30">
                <svg className="h-6 w-6 text-white" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
                </svg>
              </div>
              <div>
                <p className="text-lg font-bold tracking-tight text-white">Blood Life</p>
                <p className="text-xs text-gray-400 font-medium">Donor Management</p>
              </div>
            </div>
            
            <nav className="flex flex-col gap-1">
              {visibleItems.map((item) => {
                const Icon = item.icon;
                return (
                  <NavLink
                    key={item.path}
                    to={item.path}
                    onClick={onClose}
                    className={({ isActive }) =>
                      `flex items-center rounded-lg px-4 py-3 text-sm font-semibold transition-all duration-200 hover:bg-white/5 hover:text-white ${
                        isActive
                          ? "bg-gradient-to-r from-blood to-red-700 text-white shadow-md shadow-blood/20 border-l-4 border-white"
                          : "text-gray-300"
                      }`
                    }
                  >
                    <Icon />
                    {item.label}
                  </NavLink>
                );
              })}
            </nav>
          </div>

          <div className="mt-auto space-y-4">
            {/* User Info Block */}
            {user && (
              <div className="border-t border-white/10 pt-4 flex items-center gap-3 px-2">
                <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-gradient-to-br from-blood to-red-700 text-xs font-extrabold text-white uppercase shadow-md shadow-blood/10">
                  {user.fullName ? user.fullName.split(" ").map(n => n[0]).join("").substring(0, 2) : "US"}
                </div>
                <div className="flex-1 min-w-0">
                  <p className="truncate text-xs font-semibold text-white">{user.fullName || "User"}</p>
                  <p className="truncate text-[10px] text-gray-400 capitalize font-medium">
                    {user.role?.replace("ROLE_", "").toLowerCase() || "user"}
                  </p>
                </div>
              </div>
            )}
            
            <button
              className="w-full flex items-center rounded-lg px-4 py-3 text-left text-sm font-semibold text-gray-300 transition-all duration-200 hover:bg-blood hover:text-white"
              type="button"
              onClick={handleLogout}
            >
              <svg className="h-5 w-5 mr-3 shrink-0" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75" />
              </svg>
              Logout
            </button>
          </div>
        </div>
      </aside>
    </>
  );
}

export default Sidebar;

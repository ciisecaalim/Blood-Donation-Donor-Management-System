import { Link } from "react-router-dom";
import { getUser } from "../../utils/storage";

function Topbar({ onMenuClick }) {
  const user = getUser();

  return (
    <header className="sticky top-0 z-20 border-b border-gray-200 bg-white/95 px-4 py-3 shadow-sm backdrop-blur sm:px-6 lg:px-8">
      <div className="flex items-center justify-between gap-4">
        <button className="btn-secondary px-3 py-2 lg:hidden" type="button" onClick={onMenuClick}>
          Menu
        </button>
        
        <Link to="/profile" className="flex items-center gap-2 group hover:opacity-90 transition-opacity ml-auto" title="View Profile">
          <div className="h-9 w-9 rounded-full bg-gradient-to-br from-red-500 to-red-700 text-xs font-black text-white flex items-center justify-center shadow-md uppercase transition-all ring-2 ring-transparent group-hover:ring-red-200/50">
            {user?.fullName ? user.fullName.split(" ").map(n => n[0]).join("").substring(0, 2) : "US"}
          </div>
        </Link>
      </div>
    </header>
  );
}

export default Topbar;

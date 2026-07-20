function StatusBadge({ value }) {
  const status = value || "UNKNOWN";
  let style = "border-gray-200 bg-gray-50 text-gray-700";

  if (["ACTIVE", "AVAILABLE", "RECORDED", "APPROVED", "FULFILLED"].includes(status)) {
    style = "border-green-200 bg-green-50 text-green-700";
  }

  if (["LOW_STOCK", "PENDING", "TEMPORARILY_INELIGIBLE", "URGENT"].includes(status)) {
    style = "border-yellow-200 bg-yellow-50 text-yellow-700";
  }

  if (["OUT_OF_STOCK", "INACTIVE", "BLOCKED", "CANCELLED", "REJECTED", "EMERGENCY"].includes(status)) {
    style = "border-red-200 bg-red-50 text-blood";
  }

  return <span className={`status-badge ${style}`}>{status.replaceAll("_", " ")}</span>;
}

export default StatusBadge;

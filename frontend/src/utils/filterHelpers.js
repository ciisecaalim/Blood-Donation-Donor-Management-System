export function includesText(value, searchText) {
  if (!searchText) {
    return true;
  }

  return String(value || "")
    .toLowerCase()
    .includes(searchText.toLowerCase());
}

export function matchesValue(value, selectedValue) {
  if (!selectedValue) {
    return true;
  }

  return value === selectedValue;
}

export function matchesNumberRange(value, min, max) {
  const numberValue = Number(value || 0);
  const minValue = min === "" ? null : Number(min);
  const maxValue = max === "" ? null : Number(max);

  if (minValue !== null && numberValue < minValue) {
    return false;
  }

  if (maxValue !== null && numberValue > maxValue) {
    return false;
  }

  return true;
}

export function formatDate(value) {
  if (!value) {
    return "-";
  }

  return new Date(value).toLocaleDateString();
}

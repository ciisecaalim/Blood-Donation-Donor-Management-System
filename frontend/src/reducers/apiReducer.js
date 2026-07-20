export const initialApiState = {
  data: [],
  loading: true,
  error: "",
};

export function apiReducer(state, action) {
  if (action.type === "FETCH_START") {
    return { ...state, loading: true, error: "" };
  }

  if (action.type === "FETCH_SUCCESS") {
    return { data: action.payload, loading: false, error: "" };
  }

  if (action.type === "FETCH_ERROR") {
    return { ...state, loading: false, error: action.payload };
  }

  return state;
}

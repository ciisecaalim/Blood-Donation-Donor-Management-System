import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthLayout from "../../components/layout/AuthLayout";
import ErrorMessage from "../../components/common/ErrorMessage";
import { registerUser } from "./authService";

function RegisterPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ fullName: "", email: "", password: "" });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  function handleChange(event) {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError("");

    try {
      await registerUser(formData);
      navigate("/login");
    } catch (apiError) {
      setError(apiError.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <AuthLayout title="Register" subtitle="Create your user account, then login to continue.">
      <ErrorMessage message={error} />
      <form className="mt-4 space-y-4" onSubmit={handleSubmit}>
        <label className="block text-sm font-medium text-gray-600">
          Full name
          <input className="form-input mt-1" name="fullName" value={formData.fullName} onChange={handleChange} required />
        </label>
        <label className="block text-sm font-medium text-gray-600">
          Email
          <input className="form-input mt-1" name="email" type="email" value={formData.email} onChange={handleChange} required />
        </label>
        <label className="block text-sm font-medium text-gray-600">
          Password
          <input className="form-input mt-1" name="password" type="password" value={formData.password} onChange={handleChange} required />
        </label>
        <button className="btn-primary w-full" type="submit" disabled={saving}>
          {saving ? "Creating account..." : "Register"}
        </button>
      </form>
      <p className="mt-5 text-center text-sm text-gray-500">
        Already registered? <Link className="font-semibold text-blood" to="/login">Login</Link>
      </p>
    </AuthLayout>
  );
}

export default RegisterPage;

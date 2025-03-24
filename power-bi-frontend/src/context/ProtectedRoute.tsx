import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

/**
 * Protected Route
 * Wraps components to restrict access to authenticated users only.
 * Redirects unauthenticated users to the login page.
 */
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const { isAuthenticated } = useAuth();

  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;

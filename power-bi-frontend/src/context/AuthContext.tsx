import React, { createContext, useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { CheckAuthentication } from "../api/AuthenticationCalls";

interface AuthContextType {
  isAuthenticated: boolean;
  setAuth: (authState: boolean) => void; // Allow manual auth state updates
}

/**
 * AuthContext
 * Provides authentication state and a method to update it.
 * Used to manage and share authentication status across the application.
 */
const AuthContext = createContext<AuthContextType | undefined>(undefined);

/**
 * AuthProvider
 * Wraps the application or specific components to provide authentication context.
 * Automatically checks authentication status on route changes.
 * @param children The components wrapped by the provider.
 */
export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
  const location = useLocation();

  useEffect(() => {
    CheckAuthentication()
      .then((authenticated) => {
        setIsAuthenticated(authenticated);
      })
      .catch((error) => {
        console.error("Error checking authentication", error);
        setIsAuthenticated(false);
      });
  }, [location.pathname]);

  return (
    <AuthContext.Provider
      value={{ isAuthenticated: isAuthenticated, setAuth: setIsAuthenticated }}
    >
      {children}
    </AuthContext.Provider>
  );
};

/**
 * useAuth
 * Custom hook to access the authentication context.
 * Ensures the hook is used within an AuthProvider.
 * @returns The authentication context, including `isAuthenticated` and `setAuth`.
 * @throws Error if used outside of an AuthProvider.
 */
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

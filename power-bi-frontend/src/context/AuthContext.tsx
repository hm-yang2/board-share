import React, { createContext, useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { CheckAuthentication } from "../api/AuthenticationCalls";

interface AuthContextType {
  isAuthenticated: boolean;
  setAuth: (authState: boolean) => void; // Allow manual auth state updates
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

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
    <AuthContext.Provider value={{ isAuthenticated: isAuthenticated, setAuth: setIsAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

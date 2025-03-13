import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./context/ProtectedRoute";
import { Box } from "@mui/joy";
import NavBar from "./components/NavBar";
import LoginPage from "./pages/LoginPage";
import LinkPage from "./pages/LinkPage";
import EditLinkPage from "./pages/EditLinkPage";
import CreateLinkPage from "./pages/CreateLinkPage";
import ChannelPage from "./pages/ChannelPage";
import UserPage from "./pages/UserPage";
import AzureLandingPage from "./pages/AzureLandingPage";
import HomePage from "./pages/HomePage";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Box>
          <NavBar />
        </Box>
        <Box mt="40px" px={10}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/azure-landing" element={<AzureLandingPage />} />
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <HomePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <UserPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user/:id"
              element={
                <ProtectedRoute>
                  <UserPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/channel/:id/*"
              element={
                <ProtectedRoute>
                  <ChannelPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/link/:id"
              element={
                <ProtectedRoute>
                  <LinkPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/edit-link/:id"
              element={
                <ProtectedRoute>
                  <EditLinkPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/create-link"
              element={
                <ProtectedRoute>
                  <CreateLinkPage />
                </ProtectedRoute>
              }
            />
          </Routes>
        </Box>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;

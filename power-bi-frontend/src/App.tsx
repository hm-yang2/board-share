import "./App.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./context/ProtectedRoute";
import { Box } from "@mui/joy";
import NavBar from "./components/NavBar";
import LoginPage from "./pages/LoginPage";
import LinkPage from "./pages/LinkPage";
import ChannelUsersPage from "./pages/channel/ChannelUsersPage";
import ChannelSettingsPage from "./pages/channel/ChannelSettingsPage";
import ChannelDefaultPage from "./pages/channel/ChannelDefaultPage";
import AzureLandingPage from "./pages/AzureLandingPage";
import CreateLinkPage from "./pages/CreateLinkPage";
import HomePage from "./pages/HomePage";
import UserPage from "./pages/UserPage";
import ChannelLinkPage from "./pages/channel/ChannelLinkPage";
import ChannelPage from "./pages/channel/ChannelPage";
import SuperUserPage from "./pages/SuperUserPage";

/**
 * App Component
 *
 * The root component of the application.
 * Sets up routing, authentication context, and the navigation bar.
 * Protects routes using the `ProtectedRoute` component to ensure only authenticated users can access certain pages.
 *
 */
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Box>
          <NavBar />
        </Box>
        <Box mt="40px">
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
              path="/channel/:id/*"
              element={
                <ProtectedRoute>
                  <ChannelPage />
                </ProtectedRoute>
              }
            >
              <Route index element={<ChannelDefaultPage />} />
              <Route path=":channelLinkId" element={<ChannelLinkPage />} />
              <Route path="users" element={<ChannelUsersPage />} />
              <Route path="settings" element={<ChannelSettingsPage />} />
            </Route>
            <Route
              path="/link/:id"
              element={
                <ProtectedRoute>
                  <LinkPage />
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
            <Route
              path="/superuser"
              element={
                <ProtectedRoute>
                  <SuperUserPage />
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

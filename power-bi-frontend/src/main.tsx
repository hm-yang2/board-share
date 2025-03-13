import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { CssVarsProvider } from "@mui/joy";
import theme from "./Theme.tsx";

createRoot(document.getElementById("root")!).render(
  <CssVarsProvider theme={theme}>
    <App />
  </CssVarsProvider>
);

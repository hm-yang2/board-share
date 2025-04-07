import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { GetJWTTokens } from "../api/AuthenticationCalls";
import { useAuth } from "../context/AuthContext";
import CloseIcon from "@mui/icons-material/Close";
import {
  IconButton,
  LinearProgress,
  Snackbar,
  Stack,
  Typography,
} from "@mui/joy";

/**
 * Azure Landing Page
 * Handles the redirection after Azure login and exchanges the authorization code for JWT tokens.
 * If successful, it authenticates the user and redirects to the home page.
 * If unsuccessful, it displays an error message.
 */
function AzureLandingPage() {
  const [openSnack, setOpenSnack] = useState(false);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { setAuth } = useAuth();

  useEffect(() => {
    const code = searchParams.get("code");
    if (!code) {
      setOpenSnack(true);
      return;
    }

    GetJWTTokens(code)
      .then((success) => {
        if (success) {
          setAuth(true);
          navigate("/");
        } else {
          setOpenSnack(true);
          console.error("Failed to obtain JWT tokens.");
        }
      })
      .catch((error) => {
        setOpenSnack(true);
        console.error("Error exchanging token:", error);
      });
  }, [searchParams, setAuth, navigate]);

  return (
    <Stack gap={2}>
      <Typography level="h4">
        Verifying Azure Login... Thank you for your patience
      </Typography>
      <LinearProgress />
      <Snackbar
        open={openSnack}
        autoHideDuration={5000}
        variant="solid"
        color="warning"
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setOpenSnack(false)}>
            <CloseIcon />
          </IconButton>
        }
      >
        <Typography>Failed to verify azure login</Typography>
      </Snackbar>
    </Stack>
  );
}
export default AzureLandingPage;

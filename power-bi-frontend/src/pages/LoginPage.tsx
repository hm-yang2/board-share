import { useEffect, useState } from "react";
import { AzureLogin } from "../api/AuthenticationCalls";
import { Stack, Typography, LinearProgress, Snackbar, IconButton } from "@mui/joy";
import CloseIcon from '@mui/icons-material/Close';

function LoginPage() {
  const [openSnack, setOpenSnack] = useState(false);
  useEffect(() => {
    async function handleLogin() {
    const result = await AzureLogin();
    if (result === false) {
      setOpenSnack(true);
    }
}

handleLogin();
  })
  return (
    <Stack gap={2}>
      <Typography level="h4">
        Directing you to Azure Login...
      </Typography>
      <LinearProgress/>
      <Snackbar 
        open={openSnack} 
        autoHideDuration={5000} 
        variant='solid'
        color='warning'
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
        endDecorator= {
          <IconButton variant="plain" onClick={() => setOpenSnack(false)}>
            <CloseIcon/>
          </IconButton>
        }
      >
        <Typography>
          Failed to get azure login
        </Typography>
        
      </Snackbar>
    </Stack>
  );
}

export default LoginPage;

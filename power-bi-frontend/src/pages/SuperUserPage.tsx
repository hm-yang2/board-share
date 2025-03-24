import { useEffect, useState } from "react";
import {
  Box,
  Button,
  Stack,
  Typography,
  Modal,
  ModalDialog,
  Snackbar,
  IconButton,
} from "@mui/joy";
import CloseIcon from "@mui/icons-material/Close";
import { GetUsers, DeleteUser } from "../api/UserCalls";
import {
  GetSuperUsers,
  AddSuperUser,
  DeleteSuperUser,
} from "../api/SuperUserCalls";
import { User } from "../models/User";
import { SuperUser } from "../models/SuperUser";
import ChannelUserCard from "../components/channel/ChannelUserCard";
import { GetChannelRole } from "../api/ChannelCalls";
import { useNavigate } from "react-router-dom";

function SuperUserPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [superUsers, setSuperUsers] = useState<SuperUser[]>([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    color: "success" as "success" | "warning",
  });
  const navigate = useNavigate();

  useEffect(() => {
    GetChannelRole().then((role) => {
      if (role !== "SUPER_USER") {
        navigate("/"); // Redirect to home page if not a super user
      }
    });
    fetchUsers();
    fetchSuperUsers();
  }, [navigate]);

  const fetchUsers = () => {
    GetUsers()
      .then((fetchedUsers) => setUsers(fetchedUsers))
      .catch(() =>
        setSnackbar({
          open: true,
          message: "Failed to fetch users",
          color: "warning",
        }),
      );
  };

  const fetchSuperUsers = () => {
    GetSuperUsers()
      .then((fetchedSuperUsers) => setSuperUsers(fetchedSuperUsers))
      .catch(() =>
        setSnackbar({
          open: true,
          message: "Failed to fetch super users",
          color: "warning",
        }),
      );
  };

  const handleDeleteUser = (userId: number) => {
    DeleteUser(userId)
      .then((result) => {
        if (result === false) {
          setSnackbar({
            open: true,
            message: "Failed to delete user",
            color: "warning",
          });
        } else {
          fetchUsers();
          setSnackbar({
            open: true,
            message: "User deleted successfully",
            color: "success",
          });
        }
      })
      .catch(() =>
        setSnackbar({
          open: true,
          message: "Failed to delete user",
          color: "warning",
        }),
      );
  };

  const handleAddSuperUser = () => {
    if (selectedUserId) {
      AddSuperUser(selectedUserId)
        .then((success) => {
          if (success) {
            fetchSuperUsers();
            setOpenDialog(false);
            setSelectedUserId(null);
            setSnackbar({
              open: true,
              message: "Super user added successfully",
              color: "success",
            });
          } else {
            setSnackbar({
              open: true,
              message: "Failed to add super user",
              color: "warning",
            });
          }
        })
        .catch(() =>
          setSnackbar({
            open: true,
            message: "Failed to add super user",
            color: "warning",
          }),
        );
    }
  };

  const handleDeleteSuperUser = (superUserId: number) => {
    DeleteSuperUser(superUserId)
      .then((result) => {
        if (result === false) {
          setSnackbar({
            open: true,
            message: "Failed to delete super user",
            color: "warning",
          });
        } else {
          fetchSuperUsers();
          setSnackbar({
            open: true,
            message: "Super user deleted successfully",
            color: "success",
          });
        }
      })
      .catch(() =>
        setSnackbar({
          open: true,
          message: "Failed to delete super user",
          color: "warning",
        }),
      );
  };

  return (
    <Box
      display={"flex"}
      position={"absolute"}
      minWidth={"99vw"}
      top={"8vh"}
      left={0}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <Stack spacing={4} width={"80%"} alignItems="flex-start">
        <Typography level="h3">Super User Management</Typography>

        {/* Super Users Section */}
        <Stack spacing={2} width="100%">
          <Stack direction="row" gap={5} alignItems="center">
            <Typography level="h4">Super Users</Typography>
            <Button onClick={() => setOpenDialog(true)}>Add Super User</Button>
          </Stack>
          <Stack spacing={2}>
            {superUsers.length > 0 &&
              superUsers.map((superUser) => (
                <ChannelUserCard
                  key={superUser.user.id}
                  userEntity={superUser}
                  role="SUPER_USER"
                  customDelete={() => handleDeleteSuperUser(superUser.id)}
                />
              ))}
          </Stack>
        </Stack>

        {/* All Users Section */}
        <Stack spacing={2} width="100%" alignItems={"flex-start"}>
          <Typography level="h4">All Users</Typography>
          <Stack spacing={2}>
            {users.map((user) => (
              <ChannelUserCard
                key={user.id}
                userEntity={user}
                customDelete={() => handleDeleteUser(user.id)}
              />
            ))}
          </Stack>
        </Stack>
      </Stack>

      {/* Add Super User Dialog */}
      <Modal open={openDialog} onClose={() => setOpenDialog(false)}>
        <ModalDialog>
          <Typography level="h4">Add Super User</Typography>
          <Stack spacing={3}>
            <Stack spacing={2} maxHeight="300px" overflow="auto">
              {users.map((user) => {
                const isSuperUser = superUsers.some(
                  (superUser) => superUser.user.id === user.id,
                );
                return (
                  <Button
                    key={user.id}
                    variant={selectedUserId === user.id ? "solid" : "outlined"}
                    onClick={() => setSelectedUserId(user.id)}
                    disabled={isSuperUser} // Disable button if user is already a super user
                  >
                    {user.email}
                  </Button>
                );
              })}
            </Stack>
            <Stack direction="row" spacing={2}>
              <Button
                onClick={handleAddSuperUser}
                disabled={!selectedUserId}
                color="primary"
              >
                Confirm
              </Button>
              <Button
                onClick={() => {
                  setOpenDialog(false);
                  setSelectedUserId(null);
                }}
                color="neutral"
              >
                Cancel
              </Button>
            </Stack>
          </Stack>
        </ModalDialog>
      </Modal>

      {/* Snackbar */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        variant="solid"
        color={snackbar.color}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton
            variant="plain"
            onClick={() => setSnackbar({ ...snackbar, open: false })}
          >
            <CloseIcon />
          </IconButton>
        }
      >
        {snackbar.message}
      </Snackbar>
    </Box>
  );
}

export default SuperUserPage;

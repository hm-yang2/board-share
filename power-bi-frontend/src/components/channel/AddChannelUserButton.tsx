import { useEffect, useState } from "react";
import {
  Button,
  Chip,
  Input,
  List,
  ListItem,
  Modal,
  ModalDialog,
  ModalClose,
  Stack,
  Typography,
  Box,
} from "@mui/joy";
import { User } from "../../models/User";
import { ChannelMember } from "../../models/ChannelMember";
import { ChannelAdmin } from "../../models/ChannelAdmin";
import { ChannelOwner } from "../../models/ChannelOwner";
import { GetUsers } from "../../api/UserCalls";
import { AddChannelMember } from "../../api/ChannelMemberCalls";
import { AddChannelAdmin } from "../../api/ChannelAdminCalls";
import { AddChannelOwner } from "../../api/ChannelOwnerCalls";
import { ChannelRole } from "../../models/Channel";

interface AddUserButtonProps {
  userRole: ChannelRole["role"];
  channelId: number;
  channelMembers: ChannelMember[];
  channelAdmins: ChannelAdmin[];
  channelOwners: ChannelOwner[];
}

/**
 * Button to add a user to a channel with a specific role (Member, Admin, or Owner).
 * @param userRole The role of the current user in the channel.
 * @param channelId The ID of the channel.
 * @param channelMembers List of current channel members.
 * @param channelAdmins List of current channel admins.
 * @param channelOwners List of current channel owners.
 * @returns The AddChannelUserButton component.
 */
function AddChannelUserButton({
  userRole,
  channelId,
  channelMembers,
  channelAdmins,
  channelOwners,
}: AddUserButtonProps) {
  const [users, setUsers] = useState<User[]>([]);
  const [search, setSearch] = useState("");
  const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
  const [open, setOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);

  useEffect(() => {
    GetUsers().then(setUsers);
  }, []);

  useEffect(() => {
    setFilteredUsers(
      users.filter((user) =>
        user.email.toLowerCase().includes(search.toLowerCase()),
      ),
    );
  }, [search, users]);

  const handleAddUser = async (role: ChannelRole["role"]) => {
    if (!selectedUser) return;

    try {
      if (role === "MEMBER") {
        await AddChannelMember(channelId, selectedUser.id);
      } else if (role === "ADMIN") {
        await AddChannelAdmin(channelId, selectedUser.id);
      } else if (role === "OWNER") {
        await AddChannelOwner(channelId, selectedUser.id);
      }
      setOpen(false);
      setSelectedUser(null);
    } catch (error) {
      console.error("Error adding user:", error);
    } finally {
      window.location.reload();
    }
  };

  const isUserInRole = (user: User, role: ChannelRole["role"]) => {
    if (role === "MEMBER") {
      return channelMembers.some((member) => member.user.id === user.id);
    } else if (role === "ADMIN") {
      return channelAdmins.some((admin) => admin.user.id === user.id);
    } else if (role === "OWNER") {
      return channelOwners.some((owner) => owner.user.id === user.id);
    }
    return false;
  };

  return (
    <>
      <Button
        disabled={userRole == "MEMBER"}
        onClick={() => setOpen(true)}
        variant="soft"
        sx={{ outline: "none", "&:focus": { outline: "none" } }}
      >
        Add User
      </Button>
      <Modal open={open} onClose={() => setOpen(false)}>
        <ModalDialog minWidth={"30vw"}>
          <ModalClose />
          <Stack spacing={2}>
            <Typography level="h4">Add User to Channel</Typography>
            <Input
              placeholder="Search users..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <List>
              {filteredUsers.map((user) => (
                <ListItem
                  key={user.id}
                  onClick={() => setSelectedUser(user)}
                  sx={{
                    cursor: "pointer",
                    "&:hover": { backgroundColor: "neutral.softBg" },
                  }}
                >
                  <Stack
                    direction="row"
                    justifyContent="space-between"
                    width="100%"
                  >
                    <Typography>{user.email}</Typography>
                    {selectedUser?.id === user.id && (
                      <Chip size="sm" color="primary">
                        Selected
                      </Chip>
                    )}
                  </Stack>
                </ListItem>
              ))}
            </List>
            <Box height={"10vh"}>
              {selectedUser && (
                <Stack spacing={2}>
                  <Typography level="h4">
                    Assign Role to {selectedUser.email}
                  </Typography>
                  <Stack direction="row" spacing={2}>
                    <Button
                      onClick={() => handleAddUser("MEMBER")}
                      disabled={isUserInRole(selectedUser, "MEMBER")}
                    >
                      Member
                    </Button>
                    <Button
                      onClick={() => handleAddUser("ADMIN")}
                      disabled={
                        userRole == "MEMBER" ||
                        isUserInRole(selectedUser, "ADMIN")
                      }
                    >
                      Admin
                    </Button>
                    <Button
                      onClick={() => handleAddUser("OWNER")}
                      disabled={
                        (userRole !== "OWNER" && userRole !== "SUPER_USER") ||
                        isUserInRole(selectedUser, "OWNER")
                      }
                    >
                      Owner
                    </Button>
                  </Stack>
                </Stack>
              )}
            </Box>
          </Stack>
        </ModalDialog>
      </Modal>
    </>
  );
}

export default AddChannelUserButton;

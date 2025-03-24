import {
  Card,
  Stack,
  Typography,
  Chip,
  IconButton,
  Snackbar,
} from "@mui/joy";
import { ChannelOwner } from "../../models/ChannelOwner";
import { ChannelAdmin } from "../../models/ChannelAdmin";
import { ChannelMember } from "../../models/ChannelMember";
import { RemoveChannelOwner } from "../../api/ChannelOwnerCalls";
import DeleteIcon from "@mui/icons-material/Delete";
import { RemoveChannelAdmin } from "../../api/ChannelAdminCalls";
import { RemoveChannelMember } from "../../api/ChannelMemberCalls";
import { ChannelRole } from "../../models/Channel";
import { useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import { User } from "../../models/User";
import { DeleteUser } from "../../api/UserCalls";
import { SuperUser } from "../../models/SuperUser";

interface ChannelUserCardProps {
  userEntity: ChannelOwner | ChannelAdmin | ChannelMember | SuperUser | User;
  role?: ChannelRole["role"];
  customDelete?: () => void;
}

/**
 * Card component for displaying a user's details in a channel.
 * Includes the user's email, role, and join date, with an option to remove the user.
 * @param userEntity The user entity (ChannelOwner, ChannelAdmin, ChannelMember, SuperUser, or User).
 * @param role The role of the user in the channel (optional).
 * @param customDelete Custom delete function for removing the user (optional).
 * @returns The ChannelUserCard component.
 */
function ChannelUserCard({
  userEntity,
  role,
  customDelete,
}: ChannelUserCardProps) {
  const user = "user" in userEntity ? userEntity.user : userEntity;
  const channel = "channel" in userEntity ? userEntity.channel : null;

  const [error, setError] = useState(false);

  const handleDelete = () => {
    if (customDelete) {
      customDelete();
    } else if (channel && role === "OWNER") {
      RemoveChannelOwner(channel.id, userEntity.id)
        .then((result) => {
          if (result !== false) {
            window.location.reload();
          } else {
            setError(true);
          }
        })
        .catch((error) => {
          console.error("Error removing owner:", error);
          setError(true);
        });
    } else if (channel && role === "ADMIN") {
      RemoveChannelAdmin(channel.id, userEntity.id)
        .then((result) => {
          if (result !== false) {
            window.location.reload();
          } else {
            setError(true);
          }
        })
        .catch((error) => {
          console.error("Error removing admin:", error);
          setError(true);
        });
    } else if (channel && role === "MEMBER") {
      RemoveChannelMember(channel.id, userEntity.id)
        .then((result) => {
          if (result !== false) {
            window.location.reload();
          } else {
            setError(true);
          }
        })
        .catch((error) => {
          console.error("Error removing member:", error);
          setError(true);
        });
    } else if (channel == null) {
      DeleteUser(user.id)
        .then((result) => {
          if (result !== false) {
            window.location.reload();
          } else {
            setError(true);
          }
        })
        .catch((error) => {
          console.error("Error removing user:", error);
          setError(true);
        });
    }
  };

  return (
    <>
      <Card
        variant="soft"
        size="md"
        sx={{
          alignItems: "center",
          justifyContent: "space-between",
          width: "20vw",
        }}
      >
        <Stack
          direction="row"
          gap={2}
          justifyContent={"space-between"}
          width={"100%"}
        >
          <Stack>
            <Typography level="title-lg">{user.email}</Typography>
            <Typography level="body-sm" textColor="neutral.500">
              Joined on {new Date(userEntity.dateCreated).toLocaleDateString()}
            </Typography>
          </Stack>
          <Chip
            size="sm"
            color={
              role === "OWNER"
                ? "success"
                : role === "ADMIN"
                  ? "primary"
                  : "neutral"
            }
          >
            {role}
          </Chip>
          <IconButton
            variant="soft"
            color="danger"
            onClick={handleDelete}
            sx={{ outline: "none", "&:focus": { outline: "none" } }}
          >
            <DeleteIcon />
          </IconButton>
        </Stack>
      </Card>
      <Snackbar
        open={error}
        autoHideDuration={3000}
        variant="solid"
        color="warning"
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setError(false)}>
            <CloseIcon />
          </IconButton>
        }
        onClose={() => setError(false)}
      >
        <Typography>Unable to remove user from the channel.</Typography>
      </Snackbar>
    </>
  );
}

export default ChannelUserCard;

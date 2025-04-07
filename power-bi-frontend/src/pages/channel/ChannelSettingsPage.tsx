import { useEffect, useState } from "react";
import {
  DeleteChannel,
  GetChannel,
  UpdateChannel,
} from "../../api/ChannelCalls";
import { useNavigate, useParams } from "react-router-dom";
import { Channel } from "../../models/Channel";
import {
  Box,
  Button,
  Input,
  Stack,
  Typography,
  Textarea,
  Snackbar,
  IconButton,
  Divider,
  Chip,
} from "@mui/joy";
import CloseIcon from "@mui/icons-material/Close";

/**
 * Channel Settings Page
 * Allows users with appropriate permissions to update channel details (name, description, visibility).
 * Also provides an option to delete the channel.
 */
export default function ChannelSettingsPage() {
  const [channel, setChannel] = useState<Channel>({
    id: 0,
    name: "Loading...",
    description: "Temporary channel description",
    visibility: "PUBLIC",
    dateCreated: new Date(),
  });
  const [editedChannel, setEditedChannel] = useState(channel);
  const [isEditing, setIsEditing] = useState(false);
  const [error, setError] = useState(false);
  const [success, setSuccess] = useState(false);

  const [deleteError, setDeleteError] = useState(false);

  const { id } = useParams();
  const channelId = id; // Get channelId from URL params

  const navigate = useNavigate();

  useEffect(() => {
    if (!channelId) return;
    GetChannel(Number(channelId))
      .then((channelData) => {
        if (channelData) {
          setChannel(channelData);
          setEditedChannel(channelData);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch channel data:", error);
      });
  }, [channelId]);

  const handleSave = () => {
    UpdateChannel(editedChannel)
      .then((updatedChannel) => {
        if (updatedChannel) {
          setChannel(updatedChannel);
          setIsEditing(false);
          setSuccess(true);
          setTimeout(() => {
            // Reload to show updates in settings
            window.location.reload();
          }, 2000);
        } else {
          setError(true);
        }
      })
      .catch((error) => {
        console.error("Failed to update channel:", error);
        setError(true);
      });
  };

  const handleCancel = () => {
    setEditedChannel(channel);
    setIsEditing(false);
  };

  const handleInputChange = (field: keyof Channel, value: string) => {
    setEditedChannel({ ...editedChannel, [field]: value });
    setIsEditing(true);
  };

  const handleDelete = () => {
    if (!channelId) return;
    DeleteChannel(Number(channelId))
      .then((result) => {
        console.log(result);
        if (result) {
          navigate("/");
        } else {
          setDeleteError(true);
        }
      })
      .catch((error) => {
        console.error("Failed to delete channel:", error);
        setDeleteError(true);
      });
  };

  return (
    <Box
      paddingTop={3}
      paddingLeft={3}
      paddingBottom={4}
      display={"flex"}
      top={"7vh"}
      width={"88vw"}
      left={0}
      flexDirection={"column"}
      alignItems={"flex-start"}
    >
      <Stack spacing={3} width="70%" alignItems={"flex-start"}>
        <Typography level="h3">Channel Settings</Typography>
        <Stack spacing={2} width="100%" alignItems={"flex-start"}>
          <Typography level="title-lg">Channel Name</Typography>
          <Input
            fullWidth
            value={editedChannel.name}
            onChange={(e) => handleInputChange("name", e.target.value)}
          />
        </Stack>
        <Stack spacing={2} width="100%" alignItems={"flex-start"}>
          <Typography level="title-lg">Channel Description</Typography>
          <Textarea
            sx={{ width: "100%" }}
            value={editedChannel.description}
            onChange={(e) => handleInputChange("description", e.target.value)}
            minRows={3}
          />
        </Stack>
        <Stack spacing={2} alignItems="flex-start">
          <Typography level="title-lg">Visibility</Typography>
          <Stack direction="row" spacing={2}>
            <Button
              color="primary"
              onClick={() => {
                setEditedChannel({ ...editedChannel, visibility: "PUBLIC" });
                setIsEditing(true);
              }}
              disabled={editedChannel.visibility === "PUBLIC"}
            >
              Make Public
            </Button>
            <Button
              color="warning"
              onClick={() => {
                setEditedChannel({ ...editedChannel, visibility: "PRIVATE" });
                setIsEditing(true);
              }}
              disabled={editedChannel.visibility === "PRIVATE"}
            >
              Make Private
            </Button>
          </Stack>
        </Stack>
        <Box height={"4vh"}>
          {isEditing ? (
            <Stack height={"4vh"} direction="row" spacing={2}>
              <Button color="success" onClick={handleSave}>
                Confirm
              </Button>
              <Button color="neutral" onClick={handleCancel}>
                Cancel
              </Button>
            </Stack>
          ) : (
            <></>
          )}
        </Box>

        <Divider>
          <Chip variant="solid" color="danger" size="lg">
            Danger Zone
          </Chip>
        </Divider>
        <Stack spacing={1} alignItems="flex-start">
          <Typography level="title-lg">Delete Channel</Typography>
          <Button color="danger" onClick={handleDelete}>
            Delete Channel
          </Button>
        </Stack>
      </Stack>
      <Snackbar
        open={error}
        autoHideDuration={3000}
        variant="solid"
        color="warning"
        onClose={() => setError(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setError(false)}>
            <CloseIcon />
          </IconButton>
        }
      >
        <Typography>Failed to update channel</Typography>
      </Snackbar>
      <Snackbar
        open={success}
        autoHideDuration={3000}
        variant="solid"
        color="success"
        onClose={() => setSuccess(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setSuccess(false)}>
            <CloseIcon />
          </IconButton>
        }
      >
        <Typography>Channel updated successfully</Typography>
      </Snackbar>
      <Snackbar
        open={deleteError}
        autoHideDuration={3000}
        variant="solid"
        color="warning"
        onClose={() => setDeleteError(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setDeleteError(false)}>
            <CloseIcon />
          </IconButton>
        }
      >
        <Typography>Failed to delete channel</Typography>
      </Snackbar>
    </Box>
  );
}

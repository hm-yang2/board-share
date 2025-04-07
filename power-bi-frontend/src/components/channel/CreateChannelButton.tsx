import {
  Button,
  ButtonGroup,
  DialogContent,
  DialogTitle,
  IconButton,
  Input,
  Modal,
  ModalClose,
  ModalDialog,
  Snackbar,
  Stack,
  Textarea,
  Tooltip,
  Typography,
} from "@mui/joy";
import { useState } from "react";
import { CreateChannel } from "../../api/ChannelCalls";
import { ChannelDTO } from "../../models/ChannelDTO";
import { useNavigate } from "react-router-dom";
import CloseIcon from "@mui/icons-material/Close";
import AddIcon from "@mui/icons-material/Add";

/**
 * Button to create a new channel.
 * Opens a modal dialog for entering channel details like name, description, and visibility.
 * @returns The CreateChannelButton component.
 */
function CreateChannelButton() {
  const [openModal, setOpenModal] = useState(false);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [visibility, setVisibility] = useState<"PUBLIC" | "PRIVATE">("PUBLIC");
  const [isPublic, setIsPublic] = useState(true);

  const [inputError, setInputError] = useState(false);
  const [gotError, setGotError] = useState(false);

  const navigate = useNavigate();

  const handleButton = () => {
    setOpenModal(true);
  };

  const handleCreateChannel = () => {
    if (name == "") {
      setInputError(true);
      return;
    }

    const channelDTO: ChannelDTO = {
      name,
      description,
      visibility,
    };
    CreateChannel(channelDTO)
      .then((createdChannel) => {
        setOpenModal(false);
        if (createdChannel) {
          navigate(`/channel/${createdChannel.id}`);
        } else {
          setGotError(true);
        }
      })
      .catch((error) => {
        console.error("Error creating channel:", error);
        setGotError(true);
      })
      .finally(() => {
        setName("");
        setDescription("");
      });
  };

  return (
    <>
      <Button
        onClick={handleButton}
        startDecorator={<AddIcon />}
        variant="soft"
        sx={{ outline: "none", "&:focus": { outline: "none" } }}
      >
        <Typography>Create channel</Typography>
      </Button>
      <Modal open={openModal} onClose={() => setOpenModal(false)}>
        <ModalDialog>
          <ModalClose />
          <DialogTitle>Create new Channel</DialogTitle>
          <DialogContent>
            A name and description help people understand what your channel is
            all about.
          </DialogContent>
          <Stack gap={5}>
            <Stack gap={2}>
              <Input
                error={inputError}
                size="md"
                placeholder="Name"
                variant="soft"
                onChange={(e) => {
                  setName(e.target.value);
                  setInputError(false);
                }}
              />
              <ButtonGroup>
                <Tooltip title="Channel visible to all users" size="sm">
                  <Button
                    variant={isPublic ? "solid" : "soft"}
                    onClick={() => {
                      setVisibility("PUBLIC");
                      setIsPublic(true);
                    }}
                    color={isPublic ? "success" : "neutral"}
                    sx={{
                      outline: "none",
                      "&:focus": { outline: "none" },
                    }}
                  >
                    Public
                  </Button>
                </Tooltip>
                <Tooltip title="Channel only visible to members" size="sm">
                  <Button
                    variant={!isPublic ? "solid" : "soft"}
                    onClick={() => {
                      setVisibility("PRIVATE");
                      setIsPublic(false);
                    }}
                    color={!isPublic ? "success" : "neutral"}
                    sx={{
                      outline: "none",
                      "&:focus": { outline: "none" },
                    }}
                  >
                    Private
                  </Button>
                </Tooltip>
              </ButtonGroup>
              <Textarea
                size="md"
                placeholder="Description"
                onChange={(e) => setDescription(e.target.value)}
                minRows={5}
                variant="soft"
              />
            </Stack>
            <Button variant="solid" size="lg" onClick={handleCreateChannel}>
              Create Channel
            </Button>
          </Stack>
        </ModalDialog>
      </Modal>
      <Snackbar
        open={gotError}
        autoHideDuration={3000}
        variant="solid"
        color="warning"
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setGotError(false)}>
            <CloseIcon />
          </IconButton>
        }
        onClose={() => setGotError(false)}
      >
        <Typography>Failed to create Channel</Typography>
      </Snackbar>
    </>
  );
}
export default CreateChannelButton;

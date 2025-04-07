import { useState } from "react";
import {
  IconButton,
  Menu,
  MenuItem,
  MenuButton,
  Dropdown,
  Modal,
  DialogTitle,
  DialogContent,
  Input,
  Button,
  DialogActions,
  ModalDialog,
  FormControl,
  FormLabel,
  Typography,
  Stack,
} from "@mui/joy";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import { MoreVert } from "@mui/icons-material";
import { Link } from "../../models/Link";
import { ChannelLink } from "../../models/ChannelLink";
import {
  CreateChannelLink,
  DeleteChannelLink,
  UpdateChannelLink,
} from "../../api/ChannelLinkCalls";
import { DeleteLink, UpdateLink } from "../../api/LinkCalls";
import { useNavigate } from "react-router-dom";
import { Channel } from "../../models/Channel";
import { ChannelLinkDTO } from "../../models/ChannelLinkDTO";

interface EditLinkMenuProps {
  link: Link;
  channelLink?: ChannelLink;
  channels?: Channel[];
}

/**
 * Menu for editing, deleting, or posting a link to a channel.
 * @param link The link object to edit or delete.
 * @param channelLink The channel link object (optional).
 * @param channels The list of channels (optional).
 * @returns The LinkEditMenu component.
 */
function EditLinkMenu({ link, channelLink, channels }: EditLinkMenuProps) {
  const [editOpen, setEditOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [postOpen, setPostOpen] = useState(false);

  const [title, setTitle] = useState("");
  const [newLink, setNewLink] = useState(link.link);

  const [channelLinkDTO, setChannelLinkDTO] = useState<ChannelLinkDTO>();

  const navigate = useNavigate();

  // Open & close functions
  const handleEditOpen = () => {
    setTitle(channelLink ? channelLink.title : link.title);
    setNewLink(link.link);
    setEditOpen(true);
  };
  const handleEditClose = () => setEditOpen(false);

  const handleDeleteOpen = () => setDeleteOpen(true);
  const handleDeleteClose = () => setDeleteOpen(false);

  const handlePostOpen = () => setPostOpen(true);
  const handlePostClose = () => {
    setPostOpen(false);
    setChannelLinkDTO(undefined); // Reset the selected channel
  };

  const handleSave = async () => {
    if (channelLink) {
      await UpdateChannelLink(channelLink.id, {
        ...channelLink,
        title, // Only update title for channel links
        linkId: link.id,
        channelId: channelLink.id,
      });
    } else {
      await UpdateLink({ ...link, title, link: newLink });
    }
    handleEditClose();
    window.location.reload();
  };

  const handleDelete = () => {
    try {
      if (channelLink) {
        DeleteChannelLink(channelLink.id, channelLink.id).then(() => {
          navigate(`/channel/${channelLink.channel.id}`);
          window.location.reload();
        });
      } else {
        DeleteLink(link.id).then(() => {
          navigate("/profile");
          window.location.reload();
        });
      }
      handleDeleteClose();
    } catch (error) {
      console.error("Error deleting link:", error);
    }
  };

  const handlePostConfirm = async () => {
    if (channelLinkDTO) {
      try {
        await CreateChannelLink(link.id, channelLinkDTO);
        setPostOpen(false);
        navigate(`/channel/${channelLinkDTO.channelId}`);
      } catch (error) {
        console.error("Error posting link to channel:", error);
      }
    }
  };

  return (
    <>
      <Dropdown>
        <MenuButton
          slots={{ root: IconButton }}
          sx={{ outline: "none", "&:focus": { outline: "none" } }}
        >
          <MoreVert />
        </MenuButton>
        <Menu>
          <MenuItem color="primary" onClick={handleEditOpen}>
            <EditIcon />
            Edit
          </MenuItem>

          <MenuItem color="danger" onClick={handleDeleteOpen}>
            <DeleteIcon />
            Delete
          </MenuItem>
          {!channelLink && (
            <MenuItem color="primary" onClick={handlePostOpen}>
              <AddIcon />
              Post
            </MenuItem>
          )}
        </Menu>
      </Dropdown>

      {/* Post Dialog */}
      <Modal open={postOpen} onClose={handlePostClose}>
        <ModalDialog minWidth={"50vw"}>
          <DialogTitle>Post Link to Channel</DialogTitle>
          <DialogContent>
            <Typography>Select a channel to post this link:</Typography>
            <Stack spacing={2} maxHeight="300px" overflow="auto">
              {channels?.map((channel) => (
                <Button
                  key={channel.id}
                  variant={
                    channelLinkDTO?.channelId === channel.id
                      ? "solid"
                      : "outlined"
                  }
                  onClick={() =>
                    setChannelLinkDTO({
                      title: link.title,
                      linkId: link.id,
                      channelId: channel.id,
                    })
                  }
                >
                  {channel.name}
                </Button>
              ))}
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button onClick={handlePostClose} variant="plain">
              Cancel
            </Button>
            <Button
              onClick={handlePostConfirm}
              color="primary"
              disabled={!channelLinkDTO}
            >
              Confirm
            </Button>
          </DialogActions>
        </ModalDialog>
      </Modal>

      {/* Edit Dialog */}
      <Modal open={editOpen} onClose={handleEditClose}>
        <ModalDialog minWidth={"50vw"}>
          <DialogTitle>Edit Link</DialogTitle>
          <DialogContent></DialogContent>
          <FormControl>
            <FormLabel>Title</FormLabel>
            <Input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter new title"
            />
          </FormControl>

          {/* Only show the link input if it's a personal link */}
          {!channelLink && (
            <FormControl>
              <FormLabel>Link</FormLabel>
              <Input
                value={newLink}
                onChange={(e) => setNewLink(e.target.value)}
                placeholder="Enter new link URL"
              />
            </FormControl>
          )}
          <DialogActions>
            <Button onClick={handleEditClose} variant="plain">
              Cancel
            </Button>
            <Button onClick={handleSave} color="primary">
              Save
            </Button>
          </DialogActions>
        </ModalDialog>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <Modal open={deleteOpen} onClose={handleDeleteClose}>
        <ModalDialog minWidth={"40vw"}>
          <DialogTitle>Confirm Deletion</DialogTitle>
          <DialogContent>
            <Typography>
              Are you sure you want to delete this{" "}
              {channelLink ? "channel link" : "personal link"}?
            </Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleDeleteClose} variant="plain">
              Cancel
            </Button>
            <Button onClick={handleDelete} color="danger">
              Delete
            </Button>
          </DialogActions>
        </ModalDialog>
      </Modal>
    </>
  );
}

export default EditLinkMenu;

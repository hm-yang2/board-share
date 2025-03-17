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
} from "@mui/joy";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { MoreVert } from "@mui/icons-material";
import { Link } from "../../models/Link";
import { ChannelLink } from "../../models/ChannelLink";
import {
  DeleteChannelLink,
  UpdateChannelLink,
} from "../../api/ChannelLinkCalls";
import { DeleteLink, UpdateLink } from "../../api/LinkCalls";
import { useNavigate } from "react-router-dom";

interface EditLinkMenuProps {
  link: Link;
  channelLink?: ChannelLink;
}

function EditLinkMenu({ link, channelLink }: EditLinkMenuProps) {
  const [editOpen, setEditOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [title, setTitle] = useState("");
  const [newLink, setNewLink] = useState(link.link);

  const navigate = useNavigate();

  // Open & close functions
  const handleEditOpen = () => {
    setTitle(link.title);
    setNewLink(link.link);
    setEditOpen(true);
  };
  const handleEditClose = () => setEditOpen(false);
  const handleDeleteOpen = () => setDeleteOpen(true);
  const handleDeleteClose = () => setDeleteOpen(false);

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

  const handleDelete = async () => {
    try {
      if (channelLink) {
        await DeleteChannelLink(channelLink.id, channelLink.id);
        navigate(`/channel/${channelLink.channel.id}`);
      } else {
        await DeleteLink(link.id);
        navigate("/profile");
      }
      handleDeleteClose();
    } catch (error) {
      console.error("Error deleting link:", error);
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
        </Menu>
      </Dropdown>

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

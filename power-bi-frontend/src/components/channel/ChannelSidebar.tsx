import { useState } from "react";
import {
  Box,
  Chip,
  Drawer,
  ListDivider,
  ListItemButton,
  Stack,
  Typography,
} from "@mui/joy";
import { Button } from "@mui/joy";
import { Input } from "@mui/joy";
import { List, ListItem } from "@mui/joy";
import { useNavigate } from "react-router-dom";
import { Channel, ChannelRole } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";
import HomeIcon from "@mui/icons-material/Home";
import GroupIcon from "@mui/icons-material/Group";
import SettingsIcon from "@mui/icons-material/Settings";
import AssignmentIcon from "@mui/icons-material/Assignment";
import AddIcon from "@mui/icons-material/Add";

/**
 * Props for the ChannelSidebar component.
 */
interface SidebarProps {
  /** The channel object containing details like name and visibility. */
  channel: Channel;
  /** List of links associated with the channel. */
  channelLinks: ChannelLink[];
  /** The role of the current user in the channel. */
  role: ChannelRole["role"];
}

/**
 * Sidebar component for navigating within a channel.
 * Includes links to channel-specific pages like Home, Links, Users, and Settings.
 * @param channel The channel object containing details like name and visibility.
 * @param channelLinks List of links associated with the channel.
 * @param role The role of the current user in the channel.
 * @returns The ChannelSidebar component.
 */
export default function ChannelSidebar({
  channel,
  channelLinks,
  role,
}: SidebarProps) {
  const [search, setSearch] = useState("");
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  const isActive = (path: string) => location.pathname == path;

  return (
    <Box width={"9vw"}>
      <List size="lg">
        <ListItem>
          <Stack width={"100%"} gap={1} alignItems={"center"}>
            <Typography level="title-lg">{channel.name}</Typography>
            <Chip>{channel.visibility}</Chip>
          </Stack>
        </ListItem>
        <ListDivider />
        <ListItemButton
          variant={isActive(`/channel/${channel.id}`) ? "soft" : "plain"}
          onClick={() => navigate(`/channel/${channel.id}`)}
        >
          <HomeIcon />
          <Typography level="body-lg">Home</Typography>
        </ListItemButton>
        <ListItemButton onClick={() => setOpen(true)}>
          <AssignmentIcon />
          <Typography level="body-lg">Posts</Typography>
        </ListItemButton>

        <Drawer open={open} onClose={() => setOpen(false)} size="sm">
          <Box sx={{ p: 2, bgcolor: "background.surface", width: 320 }}>
            <Typography level="h4" sx={{ mb: 2 }}>
              Posts
            </Typography>
            <Input
              placeholder="Search posts..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <List>
              {channelLinks
                .filter((link) =>
                  link.title.toLowerCase().includes(search.toLowerCase()),
                )
                .map((link) => (
                  <ListItemButton
                    key={link.id}
                    variant={
                      isActive(`/channel/${channel.id}/${link.id}`)
                        ? "soft"
                        : "plain"
                    }
                    onClick={() => {
                      navigate(`/channel/${channel.id}/${link.id}`);
                      setOpen(false);
                    }}
                  >
                    {link.title}
                  </ListItemButton>
                ))}
            </List>
            <Button onClick={() => setOpen(false)}>Close</Button>
          </Box>
        </Drawer>

        <ListItemButton
          onClick={() => navigate(`/create-post?channelId=${channel.id}`)}
        >
          <AddIcon />
          <Typography level="body-lg">Add Post</Typography>
        </ListItemButton>

        {/* Show Users button only if the user is an Admin or higher */}
        {(role === "ADMIN" || role === "OWNER" || role === "SUPER_USER") && (
          <ListItemButton
            variant={
              isActive(`/channel/${channel.id}/users`) ? "soft" : "plain"
            }
            onClick={() => navigate(`/channel/${channel.id}/users`)}
          >
            <GroupIcon />
            <Typography level="body-lg">Users</Typography>
          </ListItemButton>
        )}

        {/* Show Settings button only if the user is an Owner or Super User */}
        {(role === "OWNER" || role === "SUPER_USER") && (
          <ListItemButton
            variant={
              isActive(`/channel/${channel.id}/settings`) ? "soft" : "plain"
            }
            onClick={() => navigate(`/channel/${channel.id}/settings`)}
          >
            <SettingsIcon />
            <Typography level="body-lg">Settings</Typography>
          </ListItemButton>
        )}
      </List>
    </Box>
  );
}

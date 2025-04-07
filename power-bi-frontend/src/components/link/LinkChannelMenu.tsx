import { useEffect, useState } from "react";
import { GetChannels } from "../../api/ChannelCalls";
import { Channel } from "../../models/Channel";
import { Box, Button, Dropdown, Menu, MenuButton, MenuItem } from "@mui/joy";

interface LinkChannelMenuProps {
  channel: Channel | null;
  setChannel: any;
}

/**
 * Dropdown menu for selecting a channel.
 * @param channel The currently selected channel (optional).
 * @param setChannel Callback function to update the selected channel.
 * @returns The LinkChannelMenu component.
 */
function LinkChannelMenu({ channel, setChannel }: LinkChannelMenuProps) {
  const [channels, setChannels] = useState<Channel[]>([]);

  useEffect(() => {
    GetChannels("")
      .then((channels) => {
        setChannels(channels);
      })
      .catch((error) => {
        console.error("Error getting channels:", error);
      });
  }, []);

  return (
    <Box display={"flex"}>
      <Dropdown>
        <MenuButton
          size="lg"
          variant={channel == null ? "soft" : "solid"}
          sx={{ outline: "none", "&:focus": { outline: "none" } }}
        >
          {channel == null
            ? "Select Channel"
            : "Selected Channel: " + channel.name}
        </MenuButton>
        <Menu>
          <MenuItem>
            <Button
              variant="plain"
              fullWidth
              color="neutral"
              onClick={() => {
                setChannel(null);
              }}
              sx={{ outline: "none", "&:focus": { outline: "none" } }}
            >
              No Channel
            </Button>
          </MenuItem>
          {channels.map((channel) => (
            <MenuItem key={channel.id}>
              <Button
                variant="plain"
                fullWidth
                color="neutral"
                onClick={() => {
                  setChannel(channel);
                }}
                sx={{
                  width: "100%",
                  height: "100%",
                  outline: "none",
                  "&:focus": { outline: "none" },
                }}
              >
                {channel.name}
              </Button>
            </MenuItem>
          ))}
        </Menu>
      </Dropdown>
    </Box>
  );
}
export default LinkChannelMenu;

import { useEffect, useState } from "react";
import { GetChannels } from "../../api/ChannelCalls";
import { Channel } from "../../models/Channel";
import { Box, Button, Dropdown, Menu, MenuButton, MenuItem } from "@mui/joy";

interface LinkChannelMenuProps {
  originalChannel?: Channel;
  setChannel: any;
}

function LinkChannelMenu({
  originalChannel,
  setChannel,
}: LinkChannelMenuProps) {
  const [channels, setChannels] = useState<Channel[]>([]);
  const [selectedChannel, setSelectedChannel] = useState<Channel | null>(
    originalChannel ? originalChannel : null,
  );

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
          variant={selectedChannel == null ? "soft" : "solid"}
          sx={{ outline: "none", "&:focus": { outline: "none" } }}
        >
          {selectedChannel == null
            ? "Select Channels"
            : "Selected Channel: " + selectedChannel.name}
        </MenuButton>
        <Menu>
          <MenuItem>
            <Button
              variant="plain"
              fullWidth
              color="neutral"
              onClick={() => {
                setSelectedChannel(null);
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
                  setSelectedChannel(channel);
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

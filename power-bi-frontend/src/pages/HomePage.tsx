import { Box, Stack, Typography } from "@mui/joy";
import SearchBar from "../components/SearchBar";
import { Channel } from "../models/Channel";
import { useEffect, useState } from "react";
import CreateChannelButton from "../components/channel/CreateChannelButton";
import { GetChannels } from "../api/ChannelCalls";
import ChannelsGrid from "../components/channel/ChannelsGrid";

function HomePage() {
  const [channels, setChannels] = useState<Channel[]>([]);

  const handleSearch = (search: string) => {
    GetChannels(search)
      .then((channels) => {
        console.log("Channels:", channels);
        setChannels(channels);
      })
      .catch((error) => {
        console.error("Error getting channels:", error);
      });
  };

  useEffect(() => {
    handleSearch("");
  }, []);

  return (
    <Box>
      <Stack direction="column" gap={3}>
        <SearchBar onSearch={handleSearch} />
        <Stack minHeight={"80vh"} gap={3}>
          <Stack direction={"row"} gap={5}>
            <Typography level="h3"> Explore Channels</Typography>
            <CreateChannelButton />
          </Stack>
          <ChannelsGrid channels={channels} />
        </Stack>
      </Stack>
    </Box>
  );
}

export default HomePage;

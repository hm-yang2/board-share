import { Box, LinearProgress, Stack, Typography } from "@mui/joy";
import SearchBar from "../components/SearchBar";
import { Channel } from "../models/Channel";
import { useEffect, useState } from "react";
import CreateChannelButton from "../components/channel/CreateChannelButton";
import { GetChannels } from "../api/ChannelCalls";
import ChannelsGrid from "../components/channel/ChannelsGrid";

/**
 * Home Page
 * Displays a search bar and a grid of channels.
 * Allows users to explore channels and create new ones.
 */
function HomePage() {
  const [channels, setChannels] = useState<Channel[]>([]);
  const [loading, setLoading] = useState(false);

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
    setLoading(true);
    handleSearch("");
    setLoading(false);
  }, []);

  return (
    <Box
      display={"flex"}
      position={"absolute"}
      minWidth={"99vw"}
      top={"8vh"}
      left={0}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <Stack direction="column" gap={3} width={"75vw"}>
        <SearchBar onSearch={handleSearch} />
        <Stack minHeight={"80vh"} gap={3}>
          <Stack direction={"row"} gap={5}>
            <Typography level="h3"> Explore Channels</Typography>
            <CreateChannelButton />
          </Stack>
          {loading ? <LinearProgress /> : <ChannelsGrid channels={channels} />}
        </Stack>
      </Stack>
    </Box>
  );
}

export default HomePage;

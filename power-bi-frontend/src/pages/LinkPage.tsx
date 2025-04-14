import { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // For extracting the URL parameters
import LinkCard from "../components/link/LinkCard";
import { Link } from "../models/Link";
import { GetLink } from "../api/LinkCalls";
import { Box } from "@mui/joy";
import { Channel } from "../models/Channel";
import { GetChannels } from "../api/ChannelCalls";

/**
 * Link Page
 * Displays a specific link, including its details and an iframe preview.
 * Fetches the link details based on the link ID from the URL parameters.
 */
function LinkPage() {
  const { id } = useParams(); // Get the linkId from the URL
  const [link, setLink] = useState<Link | null>(null); // State to hold the fetched link
  const [avaliableChannels, setAvaliableChannels] = useState<Channel[]>([]);

  useEffect(() => {
    if (id) {
      GetLink(Number(id))
        .then((gottenLink) => {
          setLink(gottenLink);
        })
        .catch((error) => {
          console.error(error);
          setLink(null);
        });
      GetChannels().then((channels) => {
        setAvaliableChannels(channels);
      });
    }
  }, [id]); // Fetch link when the linkId in the URL changes

  if (!link) {
    return <div>No Link Found...</div>; // Show loading while fetching the link data
  }

  return (
    <Box
      paddingBottom={4}
      display={"flex"}
      position={"absolute"}
      minWidth={"99vw"}
      top={"7vh"}
      left={0}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <LinkCard
        link={link}
        channels={avaliableChannels}
        width="100vw"
        height="89vh"
      />
    </Box>
  );
}

export default LinkPage;

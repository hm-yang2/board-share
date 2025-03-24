import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { GetChannelLink } from "../../api/ChannelLinkCalls";
import LinkCard from "../../components/link/LinkCard";
import { ChannelLink } from "../../models/ChannelLink";
import { Box } from "@mui/joy";

/**
 * Channel Link Page
 * Displays a specific link within a channel.
 * Fetches the link details based on the channel ID and link ID from the URL parameters.
 */
export default function ChannelLinkPage() {
  const { id, channelLinkId } = useParams();
  const [channelLink, setChannelLink] = useState<ChannelLink>();
  useEffect(() => {
    if (id && channelLinkId) {
      GetChannelLink(Number(id), Number(channelLinkId))
        .then((fetchedChannelLink) => {
          if (fetchedChannelLink) setChannelLink(fetchedChannelLink);
        })
        .catch((error) => {
          console.error("Error fetching channel link:", error);
        });
    }
  }, [id, channelLinkId]);

  if (!channelLink) {
    return <div>Channel Link not found</div>;
  }
  return (
    <Box
      display={"flex"}
      top={"7vh"}
      width={"90vw"}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <LinkCard
        link={channelLink.link}
        channelLink={channelLink}
        width="90vw"
        height="89vh"
      />
    </Box>
  );
}

import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { GetChannel } from "../../api/ChannelCalls";
import { GetChannelLinks } from "../../api/ChannelLinkCalls";
import { Channel } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";
import LinkCardList from "../../components/link/LinkCardList";
import ChannelBanner from "../../components/channel/ChannelBanner";
import { Box, Stack, Typography } from "@mui/joy";

/**
 * Channel Default Page
 * Displays the default view of a channel, including its banner and a list of channel-specific reports (links).
 * Fetches channel details and links associated with the channel.
 */
export default function ChannelDefaultPage() {
  const [channelLinks, setChannelLinks] = useState<ChannelLink[]>([]);
  const [channel, setChannel] = useState<Channel>({
    id: 0,
    name: "Loading...",
    description: "Temporary channel description",
    visibility: "PUBLIC",
    dateCreated: new Date(),
  });
  const { id } = useParams();
  const channelId = id; // Get channelId from URL params

  useEffect(() => {
    if (!channelId) return;
    GetChannel(Number(channelId))
      .then((channelData) => {
        if (channelData) {
          setChannel(channelData);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch channel data:", error);
      });

    GetChannelLinks(Number(channelId))
      .then((linksData) => {
        setChannelLinks(linksData);
      })
      .catch((error) => {
        console.error("Failed to fetch channel links:", error);
      });
  }, [channelId]);

  return (
    <Box
      paddingBottom={4}
      display={"flex"}
      top={"7vh"}
      width={"90vw"}
      left={0}
      flexDirection={"column"}
      alignItems={"flex-start"}
    >
      <Stack display={"flex"} gap={2} width={"96%"}>
        <ChannelBanner channel={channel} />
        <Stack width={"90%"} textAlign={"left"} gap={2}>
          <Typography level="title-lg">Channel Reports</Typography>
          <LinkCardList items={channelLinks} />
        </Stack>
      </Stack>
    </Box>
  );
}

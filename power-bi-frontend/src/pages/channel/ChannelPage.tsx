import { Outlet, useNavigate, useParams } from "react-router-dom";
import { Box, Stack } from "@mui/joy";
import { useState, useEffect } from "react";
import { GetChannel, GetChannelRole } from "../../api/ChannelCalls";
import { GetChannelLinks } from "../../api/ChannelLinkCalls";
import ChannelSidebar from "../../components/channel/ChannelSidebar";
import { Channel, ChannelRole } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";

/**
 * Channel Page
 * Serves as the main layout for a channel, including a sidebar for navigation and an outlet for nested routes.
 * Fetches channel details, links, and the user's role in the channel.
 */
function ChannelPage() {
  const [channelLinks, setChannelLinks] = useState<ChannelLink[]>([]);
  const [channel, setChannel] = useState<Channel>({
    id: 0,
    name: "Loading...",
    description: "Temporary channel description",
    visibility: "PUBLIC",
    dateCreated: new Date(),
  });
  const [userRole, setUserRole] = useState<ChannelRole["role"]>("NOT_ALLOWED");

  const { id } = useParams();
  const channelId = id; // Get channelId from URL params

  const navigate = useNavigate();

  useEffect(() => {
    if (!channelId) {
      navigate("/");
      return;
    }

    GetChannel(Number(channelId))
      .then((channelData) => {
        if (channelData) {
          setChannel(channelData);
        } else {
          navigate("/");
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
    GetChannelRole(Number(channelId))
      .then((role) => {
        if (role) {
          setUserRole(role);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch channel role:", error);
      });
  }, [channelId, navigate]);

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
      <Stack direction={"row"} width={"100%"} height={"100%"} gap={1.5}>
        <ChannelSidebar
          channel={channel}
          channelLinks={channelLinks}
          role={userRole}
        />
        <Outlet />
      </Stack>
    </Box>
  );
}

export default ChannelPage;

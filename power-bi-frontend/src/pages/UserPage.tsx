import { useEffect, useState } from "react";
import { Link } from "../models/Link";
import { GetLinks } from "../api/LinkCalls";
import {
  Avatar,
  Box,
  Stack,
  Tab,
  TabList,
  TabPanel,
  Tabs,
  Typography,
} from "@mui/joy";
import { Channel, ChannelRole } from "../models/Channel";
import { GetChannelRole, GetChannels } from "../api/ChannelCalls";
import SmallLinkCardList from "../components/link/SmallLinkCardList";
import ChannelsGrid from "../components/channel/ChannelsGrid";
import { User } from "../models/User";
import { GetSelf } from "../api/UserCalls";

/**
 * User Page
 * Displays the user's profile information, including their email and join date.
 * Provides tabs to view the user's personal links and channels they are part of.
 */
function UserPage() {
  const [links, setLinks] = useState<Link[]>([]);
  const [user, setUser] = useState<User>();
  const [channels, setChannels] = useState<Channel[]>([]);
  const [channelRoles, setChannelRoles] = useState<
    Map<number, ChannelRole["role"]>
  >(new Map());

  useEffect(() => {
    GetSelf().then((user) => {
      if (user) {
        setUser(user);
      }
    });
    GetLinks().then(setLinks);
    GetChannels().then(setChannels);
  }, []);

  useEffect(() => {
    if (channels.length === 0) return; // Prevent unnecessary fetches

    let isMounted = true; // To prevent state updates if the component unmounts

    Promise.all(
      channels.map(async (channel) => {
        const role = await GetChannelRole(channel.id);
        return role ? [channel.id, role] : null; // Return tuple for map
      }),
    ).then((roles) => {
      if (isMounted) {
        const validRoles = new Map(
          roles.filter(
            (role): role is [number, ChannelRole["role"]] => role !== null,
          ),
        ); // Filter and create map
        setChannelRoles(validRoles); // Updates state
      }
    });

    return () => {
      isMounted = false; // Cleanup to avoid setting state on unmounted component
    };
  }, [channels]);

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
      {/* User Section */}
      {user && (
        <Stack
          direction="row"
          alignItems="center"
          spacing={2}
          sx={{
            width: "80%",
            bgcolor: "white",
            p: 2,
            mb: 3,
          }}
        >
          <Avatar
            sx={{
              bgcolor: "primary.500",
              color: "white",
              fontSize: "1.5rem",
              width: 56,
              height: 56,
            }}
          >
            {user.email.charAt(0).toUpperCase()}
          </Avatar>
          <Stack alignItems={"flex-start"} spacing={1}>
            <Typography level="h3">{user.email}</Typography>
            <Typography level="body-sm" textColor="neutral.500">
              Joined on{" "}
              {new Date(user.dateCreated).toLocaleDateString() +
                " " +
                new Date(user.dateCreated).toLocaleTimeString()}
            </Typography>
          </Stack>
        </Stack>
      )}
      <Tabs
        sx={{ width: "80%", display: "flex", bgcolor: "white" }}
        variant="plain"
      >
        <TabList>
          <Tab sx={{ outline: "none", "&:focus": { outline: "none" } }}>
            Posts
          </Tab>
          <Tab sx={{ outline: "none", "&:focus": { outline: "none" } }}>
            Channels
          </Tab>
        </TabList>
        <TabPanel value={0} sx={{ width: "100%" }}>
          <SmallLinkCardList items={links} channels={channels} />
        </TabPanel>
        <TabPanel value={1} sx={{ width: "100%" }}>
          <ChannelsGrid channels={channels} channelRoles={channelRoles} />
        </TabPanel>
      </Tabs>
    </Box>
  );
}
export default UserPage;

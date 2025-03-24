import { useEffect, useState } from "react";
import { ChannelMember } from "../../models/ChannelMember";
import { ChannelAdmin } from "../../models/ChannelAdmin";
import { ChannelOwner } from "../../models/ChannelOwner";
import { GetChannelMembers } from "../../api/ChannelMemberCalls";
import { GetChannelAdmins } from "../../api/ChannelAdminCalls";
import { GetChannelOwners } from "../../api/ChannelOwnerCalls";
import { GetChannelRole } from "../../api/ChannelCalls";
import { useParams } from "react-router-dom";
import { Box, Stack, Typography } from "@mui/joy";
import ChannelUserCard from "../../components/channel/ChannelUserCard";
import AddChannelUserButton from "../../components/channel/AddChannelUserButton";
import { ChannelRole } from "../../models/Channel";

/**
 * Channel Users Page
 * Displays the list of users in a channel, categorized by their roles (Owners, Admins, Members).
 * Allows users with sufficient permissions to add or remove users from the channel.
 */
export default function ChannelUsersPage() {
  const [channelMembers, setChannelMembers] = useState<ChannelMember[]>([]);
  const [channelAdmins, setChannelAdmins] = useState<ChannelAdmin[]>([]);
  const [channelOwners, setChannelOwners] = useState<ChannelOwner[]>([]);
  const [userRole, setUserRole] = useState<ChannelRole["role"]>("NOT_ALLOWED");
  const { id } = useParams();

  useEffect(() => {
    if (!id) return;

    const fetchData = async () => {
      try {
        // Fetch the user's role
        const role = await GetChannelRole(Number(id));
        if (role) {
          setUserRole(role);
        }

        // If the user is not allowed, stop further execution
        if (role && (role === "NOT_ALLOWED" || role === "MEMBER")) {
          return;
        }

        // Fetch members, admins, and owners
        const [members, admins, owners] = await Promise.all([
          GetChannelMembers(Number(id)),
          GetChannelAdmins(Number(id)),
          GetChannelOwners(Number(id)),
        ]);

        setChannelMembers(members);
        setChannelAdmins(admins);
        setChannelOwners(owners);
      } catch (error) {
        console.error("Error fetching channel data:", error);
      }
    };

    fetchData();
  }, [id]);

  // Redirect or show "Not Allowed" message for members or not allowed users
  if (userRole === "NOT_ALLOWED" || userRole === "MEMBER") {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        height="100vh"
      >
        <Typography level="h3" color="danger">
          Not allowed to view this page.
        </Typography>
      </Box>
    );
  }

  return (
    <Box
      paddingTop={3}
      paddingLeft={3}
      paddingBottom={4}
      display={"flex"}
      top={"7vh"}
      width={"89vw"}
      left={0}
      flexDirection={"column"}
      alignItems={"flex-start"}
    >
      <Stack
        width={"80%"}
        spacing={4}
        alignItems="flex-start"
        textAlign={"left"}
      >
        <Stack direction="row" justifyContent="space-between" spacing={4}>
          <Typography level="h3">Channel Users</Typography>
          <AddChannelUserButton
            userRole={userRole}
            channelOwners={channelOwners}
            channelMembers={channelMembers}
            channelAdmins={channelAdmins}
            channelId={Number(id)}
          />
        </Stack>

        {(userRole === "OWNER" || userRole == "SUPER_USER") && (
          <Stack spacing={1}>
            <Typography level="h4">Owners</Typography>
            {channelOwners.map((owner) => (
              <ChannelUserCard
                key={owner.user.id}
                userEntity={owner}
                role="OWNER"
              />
            ))}
          </Stack>
        )}

        <Stack spacing={1}>
          <Typography level="h4">Admins</Typography>
          {channelAdmins.map((admin) => (
            <ChannelUserCard
              key={admin.user.id}
              userEntity={admin}
              role="ADMIN"
            />
          ))}
        </Stack>

        <Stack spacing={1}>
          <Typography level="h4">Members</Typography>
          {channelMembers.map((member) => (
            <ChannelUserCard
              key={member.user.id}
              userEntity={member}
              role="MEMBER"
            />
          ))}
        </Stack>
      </Stack>
    </Box>
  );
}

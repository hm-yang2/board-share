import {
  Box,
  Card,
  CardContent,
  Chip,
  Divider,
  Link,
  Stack,
  Typography,
} from "@mui/joy";
import { useNavigate } from "react-router-dom";
import { Channel, ChannelRole } from "../../models/Channel";

interface ChannelCardProps {
  channel: Channel;
  role?: ChannelRole["role"];
}

/**
 * Card component for displaying a channel's details.
 * Includes the channel name, description, creation date, and visibility.
 * @param channel The channel object to display.
 * @param role The role of the current user in the channel (optional).
 * @returns The ChannelCard component.
 */
function ChannelCard({ channel, role }: ChannelCardProps) {
  const navigate = useNavigate();
  const date = "Created: " + new Date(channel.dateCreated).toLocaleDateString();
  const description =
    channel.description === "" ? "No channel description" : channel.description;

  return (
    <Box width="20vw" height="12vh">
      <Card
        size="md"
        variant="outlined"
        sx={{
          height: "100%",
          width: "100%",
          backgroundColor: "sliver",
          boxShadow: "0px 2px 6px rgba(0, 0, 0, 0.15)", // Subtle shadow
          transition: "box-shadow 0.3s ease-in-out", // Smooth transition for hover effect
          "&:hover": {
            boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.2)", // Slightly increased shadow on hover
          },
          "&:active": {
            transform: "scale(0.98)", // Slight press-down effect when clicked
          },
        }}
      >
        <CardContent>
          <Stack
            alignItems={"flex-start"}
            justifyContent={"flex-start"}
            gap={1}
          >
            <Link overlay onClick={() => navigate(`/channel/${channel.id}`)}>
              <Stack alignItems={"flex-start"}>
                <Typography level="title-lg">{channel.name}</Typography>
                <Typography level="body-xs">{date}</Typography>
              </Stack>
            </Link>
            <Stack
              direction={"row"}
              gap={2}
              divider={
                <Divider
                  sx={{
                    borderColor: "grey", // Ensures the border is black
                    borderWidth: "1px", // Makes it thicker
                    borderStyle: "solid", // Ensures it's fully visible
                    backgroundColor: "grey", // Fills the middle area
                    width: "1px", // Adjust width if needed
                  }}
                  orientation="vertical"
                />
              }
            >
              {role && <Chip>{role}</Chip>}

              <Chip size="md" color="primary">
                {channel.visibility.toUpperCase()}
              </Chip>
            </Stack>
            <Typography width={"100%"} textAlign="left" noWrap>
              {description}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}

export default ChannelCard;

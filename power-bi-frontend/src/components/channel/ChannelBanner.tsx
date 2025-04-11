import { Box, Typography, Stack, Chip } from "@mui/joy";
import { Channel } from "../../models/Channel";

interface ChannelBannerProps {
  /** The channel object containing details like name, description, and visibility. */
  channel: Channel;
}

/**
 * Banner component for displaying channel details.
 * @param channel The channel object containing details like name, description, and visibility.
 * @returns The ChannelBanner component.
 */
function ChannelBanner({ channel }: ChannelBannerProps) {
  const formattedDate = new Date(channel.dateCreated).toLocaleDateString();
  const formattedTime = new Date(channel.dateCreated).toLocaleTimeString();

  const generateGradient = (id: string, dateCreated: string) => {
    const idHash = [...id].reduce((acc, char) => acc + char.charCodeAt(0), 0);
    const dateHash = [...dateCreated].reduce(
      (acc, char) => acc + char.charCodeAt(0),
      0,
    );

    const hue1 = (idHash * 37 + dateHash * 17) % 360; // Unique hue 1
    const hue2 = (hue1 + 60) % 360; // Offset for a complementary color

    return `linear-gradient(135deg, hsl(${hue1}, 80%, 85%), hsl(${hue2}, 70%, 75%))`;
  };

  return (
    <Box
      sx={{
        width: "100%",
        height: "10vh",
        background: generateGradient(String(channel.id), formattedTime),
        color: "white",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        borderRadius: "8px",
        boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.2)",
        padding: "2rem",
      }}
    >
      <Stack
        spacing={1}
        width={"100%"}
        alignItems="flex-start"
        textAlign="left"
      >
        <Stack direction={"row"} alignItems={"center"} spacing={2}>
          <Typography level="h2" fontWeight="bold">
            {channel.name}
          </Typography>
          <Chip
            size="md"
            variant="soft"
            color={channel.visibility === "PUBLIC" ? "success" : "warning"}
          >
            {channel.visibility}
          </Chip>
        </Stack>
        <Typography level="body-md" sx={{ opacity: 1 }}>
          Created on: {formattedDate} {formattedTime}
        </Typography>
        <Typography level="body-lg">
          {channel.description || "No description available."}
        </Typography>
      </Stack>
    </Box>
  );
}

export default ChannelBanner;

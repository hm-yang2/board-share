import { Box, Typography, Stack, Chip } from "@mui/joy";
import { Channel } from "../../models/Channel";

interface ChannelBannerProps {
  channel: Channel;
}

/**
 * Banner component for displaying channel details.
 * @param channel The channel object containing details like name, description, and visibility.
 * @returns The ChannelBanner component.
 */
function ChannelBanner({ channel }: ChannelBannerProps) {
  const formattedDate = new Date(channel.dateCreated).toLocaleDateString();

  return (
    <Box
      sx={{
        width: "100%",
        height: "10vh",
        background:
          "linear-gradient(135deg,rgb(239, 239, 247),rgb(186, 181, 243))",
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
        <Typography level="body-lg" sx={{ opacity: 0.8 }}>
          Created on: {formattedDate}
        </Typography>
        <Typography level="body-md">
          {channel.description || "No description available."}
        </Typography>
      </Stack>
    </Box>
  );
}

export default ChannelBanner;

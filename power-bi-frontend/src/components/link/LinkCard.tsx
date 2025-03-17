import { Box, Card, IconButton, Stack, Typography } from "@mui/joy";
import { ChannelLink } from "../../models/ChannelLink";
import { Link } from "../../models/Link";
import LinkEditMenu from "./LinkEditMenu";
import { useEffect, useRef, useState } from "react";
import { FullscreenOutlined } from "@mui/icons-material";

interface LinkCardProps {
  link: Link;
  channelLink?: ChannelLink;
  height?: string;
  width?: string;
}
function LinkCard({ link, channelLink, width, height }: LinkCardProps) {
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const [isFullScreen, setIsFullScreen] = useState(false);

  // Parsing link data
  const content = channelLink == undefined ? link : channelLink;
  const date = "Created: " + new Date(content.dateCreated).toLocaleDateString();

  const enterFullScreen = () => {
    if (iframeRef.current) {
      iframeRef.current.requestFullscreen();
      setIsFullScreen(true);
    }
  };

  useEffect(() => {
    const handleFullScreenChange = () => {
      setIsFullScreen(document.fullscreenElement !== null);
    };
    const handleKeyPress = (event: { key: string }) => {
      if (event.key === "f" || event.key === "F") {
        enterFullScreen();
      }
    };
    document.addEventListener("fullscreenchange", handleFullScreenChange);
    document.addEventListener("keydown", handleKeyPress);
    return () => {
      document.removeEventListener("fullscreenchange", handleFullScreenChange);
      document.removeEventListener("keydown", handleKeyPress);
    };
  }, []);

  return (
    <Box display="flex" alignSelf={"center"} height={height ? height : "88vh"} width={width?width:"85vw"}>
      <Card
        variant="soft"
        sx={{
          height: "100%",
          width: "100%",
          overflow: "auto",
          display: "flex",
        }}
      >
        <Stack
          marginLeft={2}
          direction={"row"}
          justifyContent={"space-between"}
        >
          <Stack alignItems={"flex-start"}>
            <Typography level="h4">{content.title}</Typography>
            <Typography level="body-sm">
              {date + ". By " + link.user.email}
            </Typography>
          </Stack>
          <Stack direction={"row"}>
            <EnterFullScreenButton handleFullScreen={enterFullScreen} />
            <LinkEditMenu link={link} />
          </Stack>
        </Stack>
        <iframe
          loading="lazy"
          ref={iframeRef}
          title={content.title}
          src={link.link}
          style={{ border: "none", width: "100%", height: "100%" }}
          allowFullScreen
        />
      </Card>
    </Box>
  );
}

function EnterFullScreenButton({
  handleFullScreen,
}: {
  handleFullScreen: () => void;
}) {
  return (
    <IconButton
      onClick={handleFullScreen}
      sx={{ outline: "none", "&:focus": { outline: "none" } }}
    >
      <FullscreenOutlined />
    </IconButton>
  );
}

export default LinkCard;

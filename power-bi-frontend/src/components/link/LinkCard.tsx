import {
  Box,
  Card,
  Link as JoyLink,
  IconButton,
  Stack,
  Typography,
} from "@mui/joy";
import { ChannelLink } from "../../models/ChannelLink";
import { Link } from "../../models/Link";
import LinkEditMenu from "./EditLinkMenu";
import { useEffect, useRef } from "react";
import { FullscreenOutlined } from "@mui/icons-material";
import { Channel } from "../../models/Channel";
import { useLocation } from "react-router-dom";

/**
 * Props for the LinkCard component.
 */
interface LinkCardProps {
  /** The link object to display. */
  link: Link;
  /** The channel link object (optional). */
  channelLink?: ChannelLink;
  /** The height of the card (optional). */
  height?: string;
  /** The width of the card (optional). */
  width?: string;
  /** The list of channels (optional). */
  channels?: Channel[];
}

/**
 * Card component for displaying a link or channel link.
 * Includes an iframe preview of the link and options to edit or view in fullscreen.
 * @param link The link object to display.
 * @param channelLink The channel link object (optional).
 * @param width The width of the card (optional).
 * @param height The height of the card (optional).
 * @returns The LinkCard component.
 */
function LinkCard({
  link,
  channelLink,
  width,
  height,
  channels,
}: LinkCardProps) {
  const iframeRef = useRef<HTMLIFrameElement>(null);

  // Determine link path
  const linkPath = channelLink
    ? `/channel/${channelLink.channel.id}/${channelLink.id}`
    : `/link/${link.id}`;
  const location = useLocation();

  // Parsing link data
  const content = channelLink == undefined ? link : channelLink;
  const date = "Created: " + new Date(content.dateCreated).toLocaleDateString();

  const enterFullScreen = () => {
    if (iframeRef.current) {
      iframeRef.current.requestFullscreen();
    }
  };

  useEffect(() => {
    const handleFullScreenChange = () => {};
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
    <Box
      display="flex"
      alignSelf={"center"}
      height={height ? height : "88vh"}
      width={width ? width : "85vw"}
    >
      <Card
        variant="soft"
        sx={{
          height: "100%",
          width: "100%",
          overflow: "auto",
          display: "flex",
          bgcolor: "rgb(247, 239, 239)",
        }}
      >
        <Stack
          marginLeft={2}
          marginBottom={1}
          direction={"row"}
          justifyContent={"space-between"}
          height={"5.5%"}
        >
          <JoyLink
            key={link.id}
            href={linkPath}
            underline="hover"
            disabled={location.pathname == linkPath}
          >
            <Stack alignItems={"flex-start"}>
              <Typography level="h4">{content.title}</Typography>
              <Typography level="body-sm">
                {date + ". By " + link.user.email}
              </Typography>
            </Stack>
          </JoyLink>
          <Stack direction={"row"}>
            <EnterFullScreenButton handleFullScreen={enterFullScreen} />
            <LinkEditMenu
              link={link}
              channels={channels}
              channelLink={channelLink}
            />
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

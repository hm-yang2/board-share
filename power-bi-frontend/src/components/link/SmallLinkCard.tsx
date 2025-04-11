import { Card, Typography, Stack, Link as JoyLink } from "@mui/joy";
import { Link } from "../../models/Link";
import LinkEditMenu from "./EditLinkMenu";
import { ChannelLink } from "../../models/ChannelLink";
import { Channel } from "../../models/Channel";

/**
 * Props for the SmallLinkCard component.
 */
interface SmallLinkCardProps {
  /** The link object to display. */
  link: Link;
  /** The channel link object (optional). */
  channelLink?: ChannelLink;
  /** The width of the card (optional). */
  width?: string;
  /** The height of the card (optional). */
  height?: string;
  /** The list of channels (optional). */
  channels?: Channel[];
}

/**
 * Compact card component for displaying a link or channel link.
 * Includes a preview iframe and options to edit the link.
 * @param link The link object to display.
 * @param channelLink The channel link object (optional).
 * @param width The width of the card (optional).
 * @param height The height of the card (optional).
 * @param channels The list of channels (optional).
 * @returns The SmallLinkCard component.
 */
function SmallLinkCard({
  link,
  channelLink,
  width,
  height,
  channels,
}: SmallLinkCardProps) {
  const linkPath = channelLink
    ? `/channel/${channelLink.channel.id}/${channelLink.id}`
    : `/link/${link.id}`;
  return (
    <Card
      variant="plain"
      sx={{
        width: width ? width : "35vw",
        height: height ? height : "11vh",
        p: 1,
        bgcolor: "rgb(247, 239, 239)",
        transition: "all 0.3s ease-in-out",
        "&:hover": {
          boxShadow: "0px 8px 16px rgba(0, 0, 0, 0.15)", // Soft shadow on hover
        },
        "&:active": {
          transform: "scale(0.98)", // Slight press-down effect when clicked
        },
      }}
    >
      <Stack direction="row" spacing={2} alignItems="center">
        <iframe
          src={link.link}
          style={{
            width: "10vw",
            height: "10vh",
            borderRadius: 8,
            border: "none",
            pointerEvents: "none",
          }}
          loading="lazy"
          allowFullScreen={false}
        />
        <Stack spacing={0.5} flex={1} textAlign={"start"}>
          <Typography level="title-lg" noWrap>
            <JoyLink overlay href={linkPath} underline="hover">
              {channelLink ? channelLink.title : link.title}
            </JoyLink>
          </Typography>
          <Typography level="body-sm" textColor="neutral.500">
            Posted by {link.user?.email} •{" "}
            {new Date(link.dateCreated).toLocaleDateString()}
          </Typography>
        </Stack>
        <LinkEditMenu
          link={link}
          channelLink={channelLink}
          channels={channels}
        />
      </Stack>
    </Card>
  );
}

export default SmallLinkCard;

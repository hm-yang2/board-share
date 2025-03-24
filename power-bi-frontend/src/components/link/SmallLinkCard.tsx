import { Card, Typography, Stack, Link as JoyLink } from "@mui/joy";
import { Link } from "../../models/Link";
import LinkEditMenu from "./LinkEditMenu";
import { ChannelLink } from "../../models/ChannelLink";
import { Channel } from "../../models/Channel";

interface SmallLinkCardProps {
  link: Link;
  channelLink?: ChannelLink;
  width?: string;
  height?: string;
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
      variant="outlined"
      sx={{
        width: width ? width : "35vw",
        height: height ? height : "11vh",
        p: 1,
      }}
    >
      <Stack direction="row" spacing={1} alignItems="center">
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

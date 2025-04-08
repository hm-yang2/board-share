import { Divider, Link as JoyLink, Stack } from "@mui/joy";
import { Channel } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";
import LinkCard from "./LinkCard";
import { Link } from "../../models/Link";

interface LinkCardListProps {
  items: (Link | ChannelLink)[];
  channels?: Channel[];
}

/**
 * List component for displaying multiple links or channel links.
 * @param items The list of links or channel links to display.
 * @param channels The list of channels (optional).
 * @returns The LinkCardList component.
 */
function LinkCardList({ items }: LinkCardListProps) {
  if (items.length < 1) {
    return <> NO POSTS</>;
  }

  return (
    <Stack gap={5} divider={<Divider />}>
      {items.map((item) => {
        if ("channel" in item) {
          // Render ChannelLink
          const channelLink = item as ChannelLink;
          const linkPath = `/channel/${channelLink.channel.id}/${channelLink.id}`;
          return (
            <JoyLink key={channelLink.id} href={linkPath} underline="hover">
              <LinkCard
                link={channelLink.link}
                channelLink={channelLink}
                width="80vw"
                height="70vh"
              />
            </JoyLink>
          );
        } else {
          // Render Link
          const link = item as Link;
          const linkPath = `/link/${link.id}`;
          return (
            <JoyLink key={link.id} href={linkPath} underline="hover">
              <LinkCard link={link} width="70vw" height="10vh" />
            </JoyLink>
          );
        }
      })}
    </Stack>
  );
}

export default LinkCardList;

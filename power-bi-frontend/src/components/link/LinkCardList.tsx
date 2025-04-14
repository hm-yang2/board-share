import { Divider, Stack } from "@mui/joy";
import { Channel } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";
import LinkCard from "./LinkCard";
import { Link } from "../../models/Link";

/**
 * Props for the LinkCardList component.
 */
interface LinkCardListProps {
  /** The list of links or channel links to display. */
  items: (Link | ChannelLink)[];
  /** The list of channels (optional). */
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
          return (
            <LinkCard
              link={channelLink.link}
              channelLink={channelLink}
              width="80vw"
              height="70vh"
            />
          );
        } else {
          // Render Link
          const link = item as Link;
          return <LinkCard link={link} width="70vw" height="10vh" />;
        }
      })}
    </Stack>
  );
}

export default LinkCardList;

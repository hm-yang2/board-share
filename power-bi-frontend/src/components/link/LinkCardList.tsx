import { Stack } from "@mui/joy";
import { ChannelLink } from "../../models/ChannelLink";
import { Link } from "../../models/Link";
import SmallLinkCard from "./SmallLinkCard";
import { Channel } from "../../models/Channel";

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
function LinkCardList({ items, channels }: LinkCardListProps) {
  if (items.length < 1) {
    return <> NO REPORTS</>;
  }
  return (
    <>
      <Stack spacing={3}>
        {items.map((item) => {
          if ("channel" in item) {
            // Render ChannelLink
            const channelLink = item as ChannelLink;
            return (
              <SmallLinkCard
                key={channelLink.id}
                link={channelLink.link}
                channelLink={channelLink}
                width="80vw"
              />
            );
          } else {
            // Render Link
            const link = item as Link;
            return (
              <SmallLinkCard
                key={link.id}
                link={link}
                width="70vw"
                height="10vh"
                channels={channels}
              />
            );
          }
        })}
      </Stack>
    </>
  );
}

export default LinkCardList;

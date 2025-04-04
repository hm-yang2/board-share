import { Stack } from "@mui/joy";
import { ChannelLink } from "../../models/ChannelLink";
import { Link } from "../../models/Link";
import SmallLinkCard from "./SmallLinkCard";
import { Channel } from "../../models/Channel";

interface SmallLinkCardListProps {
  items: (Link | ChannelLink)[];
  channels?: Channel[];
}

/**
 * List component for displaying multiple small link cards or small channel link cards.
 * @param items The list of links or channel links to display.
 * @param channels The list of channels (optional).
 * @returns The SmallLinkCardList component.
 */
function SmallLinkCardList({ items, channels }: SmallLinkCardListProps) {
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

export default SmallLinkCardList;

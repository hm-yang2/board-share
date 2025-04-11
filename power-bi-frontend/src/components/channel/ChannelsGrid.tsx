import { Grid } from "@mui/joy";
import { Channel, ChannelRole } from "../../models/Channel";
import ChannelCard from "./ChannelCard";

/**
 * Props for the ChannelsGrid component.
 */
interface ChannelsGridProps {
  /** The list of channels to display. */
  channels: Channel[];
  /** A map of channel IDs to user roles (optional). */
  channelRoles?: Map<number, ChannelRole["role"]>;
  /** The spacing between rows in the grid (optional). */
  rowSpacing?: number;
  /** The spacing between columns in the grid (optional). */
  columnSpacing?: number;
}

/**
 * Grid layout for displaying a list of channels.
 * @param channels The list of channels to display.
 * @param channelRoles A map of channel IDs to user roles (optional).
 * @param rowSpacing The spacing between rows in the grid (optional).
 * @param columnSpacing The spacing between columns in the grid (optional).
 * @returns The ChannelsGrid component.
 */
function ChannelsGrid({
  channels,
  rowSpacing,
  columnSpacing,
  channelRoles,
}: ChannelsGridProps) {
  return (
    <Grid
      container
      rowSpacing={rowSpacing ? rowSpacing : 10}
      columnSpacing={columnSpacing ? columnSpacing : 10}
      columns={3}
    >
      {channels.map((channel) => (
        <Grid xs={1} sm={1} md={1} lg={1} key={channel.id}>
          <ChannelCard channel={channel} role={channelRoles?.get(channel.id)} />
        </Grid>
      ))}
    </Grid>
  );
}
export default ChannelsGrid;

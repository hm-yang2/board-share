import { Grid } from "@mui/joy";
import { Channel } from "../../models/Channel";
import ChannelCard from "./ChannelCard";

interface ChannelsGridProps {
  channels: Channel[];
}

function ChannelsGrid({ channels }: ChannelsGridProps) {
  return (
    <Grid container rowSpacing={10} columnSpacing={10} columns={3}>
      {channels.map((channel) => (
        <Grid xs={1} sm={1} md={1} lg={1} key={channel.id}>
          <ChannelCard channel={channel} />
        </Grid>
      ))}
    </Grid>
  );
}
export default ChannelsGrid;

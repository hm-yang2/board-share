import {
  Box,
  Card,
  CardContent,
  Chip,
  Link,
  Stack,
  Typography,
} from "@mui/joy";
import { useNavigate } from "react-router-dom";
import { Channel } from "../../models/Channel";

interface ChannelCardProps {
  channel: Channel;
}
function ChannelCard({ channel }: ChannelCardProps) {
  const navigate = useNavigate();
  const date = "Created: " + new Date(channel.dateCreated).toLocaleDateString();
  const description =
    channel.description === "" ? "No channel description" : channel.description;

  return (
    <Box width="20vw" height="12vh">
      <Card
        size="md"
        variant="outlined"
        sx={{ height: "100%", width: "100%", backgroundColor: "beige" }}
      >
        <CardContent>
          <Stack
            alignItems={"flex-start"}
            justifyContent={"flex-start"}
            gap={1}
          >
            <Link overlay onClick={() => navigate(`/channel/${channel.id}`)}>
              <Stack alignItems={"flex-start"}>
                <Typography level="title-lg">{channel.name}</Typography>
                <Typography level="body-xs">{date}</Typography>
              </Stack>
            </Link>
            <Chip size="md" color="primary">
              {channel.visibility.toUpperCase()}
            </Chip>
            <Typography width={"100%"} textAlign="left" noWrap>
              {description}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}

export default ChannelCard;

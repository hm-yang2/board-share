import {
  Card,
  CardContent,
  Typography,
  Stack,
  IconButton,
  Link as JoyLink,
} from "@mui/joy";
import { MoreVert } from "@mui/icons-material";
import { Link } from "../../models/Link";
import LinkEditMenu from "./LinkEditMenu";
import { ChannelLink } from "../../models/ChannelLink";

interface SmallLinkCardProps {
  link: Link;
  channelLink?: ChannelLink
}

function SmallLinkCard({ link, channelLink }: SmallLinkCardProps) {
  const linkPath = channelLink 
    ? `/channel/${channelLink.channel.id}/${channelLink.id}` 
    : `/link/${link.id}`;

  return (
    <Card variant="outlined" sx={{ width: "35vw", height: "11vh", p: 1 }}>
      <Stack direction="row" spacing={1} alignItems="center">
        <iframe
          src={link.link}
          style={{
            width: "10vw",
            height: "10vh",
            borderRadius: 8,
            border: "none",
          }}
          allowFullScreen={false}
        />
        <Stack spacing={0.5} flex={1} textAlign={'start'}>
          <Typography level="title-md" noWrap>
            <JoyLink href={linkPath} underline="hover">
              {link.title}
            </JoyLink>
          </Typography>
          <Typography level="body-sm" textColor="neutral.500">
            Posted by {link.user?.email} •{" "}
            {new Date(link.dateCreated).toLocaleDateString()}
          </Typography>
        </Stack>
        <LinkEditMenu link={link} />
      </Stack>
    </Card>
  );
}

export default SmallLinkCard;

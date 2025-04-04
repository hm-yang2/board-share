import { useState, useEffect, useMemo } from "react";
import { useParams } from "react-router-dom";
import { GetChannel } from "../../api/ChannelCalls";
import { GetChannelLinks } from "../../api/ChannelLinkCalls";
import { Channel } from "../../models/Channel";
import { ChannelLink } from "../../models/ChannelLink";
import SmallLinkCardList from "../../components/link/SmallLinkCardList";
import ChannelBanner from "../../components/channel/ChannelBanner";
import {
  Box,
  Dropdown,
  IconButton,
  Input,
  Menu,
  MenuButton,
  MenuItem,
  Stack,
  Typography,
} from "@mui/joy";
import LinkCardList from "../../components/link/LinkCardList";
import ViewAgendaIcon from "@mui/icons-material/ViewAgenda";
import ViewListIcon from "@mui/icons-material/ViewList";

/**
 * Channel Default Page
 * Displays the default view of a channel, including its banner and a list of channel-specific reports (links).
 * Fetches channel details and links associated with the channel.
 */
export default function ChannelDefaultPage() {
  const [channelLinks, setChannelLinks] = useState<ChannelLink[]>([]);
  const [channel, setChannel] = useState<Channel>({
    id: 0,
    name: "Loading...",
    description: "Temporary channel description",
    visibility: "PUBLIC",
    dateCreated: new Date(),
  });
  const [isSmallCardView, setIsSmallCardView] = useState(false);
  const [search, setSearch] = useState("");
  const { id } = useParams();
  const channelId = id; // Get channelId from URL params

  useEffect(() => {
    if (!channelId) return;
    GetChannel(Number(channelId))
      .then((channelData) => {
        if (channelData) {
          setChannel(channelData);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch channel data:", error);
      });

    GetChannelLinks(Number(channelId))
      .then((linksData) => {
        setChannelLinks(linksData);
      })
      .catch((error) => {
        console.error("Failed to fetch channel links:", error);
      });
  }, [channelId]);

  // Filter links based on the search query
  const filteredLinks = useMemo(() => {
    return channelLinks.filter((link) =>
      link.link.title.toLowerCase().includes(search.toLowerCase()),
    );
  }, [channelLinks, search]);

  return (
    <Box
      paddingBottom={4}
      display={"flex"}
      top={"7vh"}
      width={"89vw"}
      left={0}
      flexDirection={"column"}
      alignItems={"flex-start"}
    >
      <Stack display={"flex"} gap={2} width={"96%"}>
        <ChannelBanner channel={channel} />
        <Stack width={"90%"} textAlign={"left"} gap={2}>
          <Stack direction={"row"} gap={3} alignItems={"center"}>
            <Dropdown>
              <MenuButton
                slots={{ root: IconButton }}
                slotProps={{ root: { variant: "outlined", color: "neutral" } }}
                sx={{
                  outline: "none",
                  "&:focus": { outline: "none" },
                }}
                variant="plain"
              >
                {isSmallCardView ? <ViewListIcon /> : <ViewAgendaIcon />}
              </MenuButton>
              <Menu>
                <MenuItem onClick={() => setIsSmallCardView(false)}>
                  <ViewAgendaIcon />
                  <Typography>Card View</Typography>
                </MenuItem>
                <MenuItem onClick={() => setIsSmallCardView(true)}>
                  <ViewListIcon />
                  <Typography>Compact View</Typography>
                </MenuItem>
              </Menu>
            </Dropdown>
            <Input
              size="lg"
              placeholder="Search reports..."
              value={search}
              autoFocus
              onChange={(e) => setSearch(e.target.value)}
              sx={{
                "--Input-radius": "0px",
                borderBottom: "2px solid",
                borderColor: "neutral.outlinedBorder",
                "&:hover": {
                  borderColor: "neutral.outlinedHoverBorder",
                },
                "&::before": {
                  border: "1px solid var(--Input-focusedHighlight)",
                  transform: "scaleX(0)",
                  left: 0,
                  right: 0,
                  bottom: "-2px",
                  top: "unset",
                  transition: "transform .15s cubic-bezier(0.1,0.9,0.2,1)",
                  borderRadius: 0,
                },
                "&:focus-within::before": {
                  transform: "scaleX(1)",
                },
                width: "50vw",
              }}
            />
          </Stack>
          {isSmallCardView ? (
            <SmallLinkCardList items={filteredLinks} />
          ) : (
            <LinkCardList items={filteredLinks} />
          )}
        </Stack>
      </Stack>
    </Box>
  );
}

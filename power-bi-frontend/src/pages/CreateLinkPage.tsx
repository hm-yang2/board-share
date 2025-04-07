import { useEffect, useState } from "react";
import LinkChannelMenu from "../components/link/LinkChannelMenu";
import { Channel } from "../models/Channel";
import {
  Box,
  IconButton,
  Snackbar,
  Stack,
  Tooltip,
  Typography,
} from "@mui/joy";
import LinkForm from "../components/link/LinkForm";
import { CreateChannelLink } from "../api/ChannelLinkCalls";
import { CreateLink } from "../api/LinkCalls";
import { useNavigate, useSearchParams } from "react-router-dom";
import { LinkDTO } from "../models/LinkDTO";
import CloseIcon from "@mui/icons-material/Close";
import { GetChannel } from "../api/ChannelCalls";
import HelpOutlineOutlinedIcon from "@mui/icons-material/HelpOutlineOutlined";

/**
 * Create Link Page
 * Provides a form for users to create a new link.
 * Optionally allows users to associate the link with a specific channel.
 */
function CreateLinkPage() {
  const [channel, setChannel] = useState<Channel | null>(null);
  const [title, setTitle] = useState("");
  const [link, setLink] = useState("");
  const [description, setDescription] = useState("");

  const [isError, setIsError] = useState(false);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const channelId = searchParams.get("channelId");
    if (channelId) {
      GetChannel(Number(channelId))
        .then((fetchedChannel) => {
          if (fetchedChannel) {
            setChannel(fetchedChannel);
          }
        })
        .catch((error) => {
          console.error("Error fetching channel:", error);
        });
    }
  }, [searchParams]);

  const handlePost = async () => {
    const linkDTO: LinkDTO = {
      title,
      description,
      link,
    };

    const createdLink = await CreateLink(linkDTO);
    let channelLink;
    if (createdLink && channel) {
      channelLink = await CreateChannelLink(channel.id, {
        title,
        linkId: createdLink.id,
        channelId: channel.id,
      });
    }

    if (createdLink && channelLink && channel) {
      navigate(`/channel/${channel.id}/${channelLink.id}`);
    } else if (createdLink) {
      navigate(`/link/${createdLink.id}`);
    } else {
      setIsError(true);
      console.error("Error creating link or channel link");
    }
  };

  return (
    <Box display={"flex"} minHeight={"88vh"}>
      <Stack alignItems={"flex-start"} gap={3} width={"90vw"}>
        <Typography level="h2">Create post</Typography>
        <Typography level="title-lg" fontWeight={"normal"}>
          Give your post a title and a link to share with others.
        </Typography>
        <Stack width={"100%"} gap={4}>
          <Stack direction={"row"} alignItems={"center"} gap={1}>
            <LinkChannelMenu setChannel={setChannel} channel={channel} />
            <Tooltip title="Select a channel to directly post to">
              <HelpOutlineOutlinedIcon
                sx={{ fontSize: 25 }}
                color="secondary"
              />
            </Tooltip>
          </Stack>
          <LinkForm
            setTitle={setTitle}
            setDescription={setDescription}
            setLink={setLink}
            channel={channel}
            handlePost={handlePost}
          />
        </Stack>
      </Stack>
      <Snackbar
        open={isError}
        autoHideDuration={5000}
        variant="solid"
        color="warning"
        onClose={() => setIsError(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        endDecorator={
          <IconButton variant="plain" onClick={() => setIsError(false)}>
            <CloseIcon />
          </IconButton>
        }
      >
        <Typography>Unable to create post. Invalid link</Typography>
      </Snackbar>
    </Box>
  );
}
export default CreateLinkPage;

import { useState } from "react";
import LinkChannelMenu from "../components/link/LinkChannelMenu";
import { Channel } from "../models/Channel";
import { Box, IconButton, Snackbar, Stack, Typography } from "@mui/joy";
import LinkForm from "../components/link/LinkForm";
import { CreateChannelLink } from "../api/ChannelLinkCalls";
import { CreateLink } from "../api/LinkCalls";
import { useNavigate } from "react-router-dom";
import { LinkDTO } from "../models/LinkDTO";
import CloseIcon from "@mui/icons-material/Close";

function CreateLinkPage() {
  const [channel, setChannel] = useState<Channel | null>(null);
  const [title, setTitle] = useState("");
  const [link, setLink] = useState("");
  const [description, setDescription] = useState("");

  const [isError, setIsError] = useState(false);
  const navigate = useNavigate();

  const handlePost = async () => {
    const linkDTO: LinkDTO = {
      title,
      description,
      link,
    };

    const createdLink = await CreateLink(linkDTO);
    if (createdLink && channel) {
      await CreateChannelLink(channel.id, {
        title,
        linkId: createdLink.id,
        channelId: channel.id,
      });
    }

    if (createdLink) {
      console.log(createdLink);
      navigate(`/link/${createdLink.id}`);
      // TODO edit to navigate to channel link page
    } else {
      setIsError(true);
      console.error("Error creating link or channel link");
    }
  };

  return (
    <Box display={"flex"}>
      <Stack alignItems={"flex-start"} gap={3} width={"90vw"}>
        <Typography level="h2">Create post</Typography>
        <Stack minHeight={"80vh"} width={"100%"} gap={4}>
          <LinkChannelMenu setChannel={setChannel} />
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
        <Typography>Unable to create post</Typography>
      </Snackbar>
    </Box>
  );
}
export default CreateLinkPage;

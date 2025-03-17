import { useState } from "react";
import { Channel } from "../../models/Channel";
import { Box, Button, Input, Stack, Textarea, Typography } from "@mui/joy";

interface LinkFormProps {
  setTitle: any;
  setLink: any;
  setDescription?: any;
  channel?: Channel | null;
  handlePost: any;
}
function LinkForm({
  setTitle,
  setLink,
  setDescription,
  channel,
  handlePost,
}: LinkFormProps) {
  const [noTitle, setNoTitle] = useState(false);
  const [noLink, setNoLink] = useState(false);

  return (
    <Stack gap={3}>
      <Input
        error={noTitle}
        size="lg"
        placeholder="Title"
        variant="outlined"
        onChange={(e) => {
          setTitle(e.target.value);
          setNoTitle(false);
        }}
      />
      <Input
        error={noLink}
        size="lg"
        placeholder="Link"
        variant="outlined"
        onChange={(e) => {
          setLink(e.target.value);
          setNoLink(false);
        }}
      />
      <Textarea
        size="md"
        placeholder="Description"
        onChange={(e) => setDescription(e.target.value)}
        minRows={5}
        variant="soft"
      />
      <Box display={"flex"} justifyContent={"flex-end"}>
        <Button onClick={handlePost} variant="soft">
          <Typography level={"body-lg"}>Post</Typography>
        </Button>
      </Box>
    </Stack>
  );
}
export default LinkForm;

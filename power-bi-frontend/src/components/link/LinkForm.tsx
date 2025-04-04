import { useState } from "react";
import { Channel } from "../../models/Channel";
import { Box, Button, Input, Stack, Tooltip, Typography } from "@mui/joy";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";

interface LinkFormProps {
  setTitle: any;
  setLink: any;
  setDescription?: any;
  channel?: Channel | null;
  handlePost: any;
}

/**
 * Form component for creating or editing a link.
 * Includes fields for title, link URL, and description.
 * @param setTitle Callback function to set the title.
 * @param setLink Callback function to set the link URL.
 * @param setDescription Callback function to set the description (optional).
 * @param channel The selected channel (optional).
 * @param handlePost Callback function to handle form submission.
 * @returns The LinkForm component.
 */
function LinkForm({ setTitle, setLink, handlePost }: LinkFormProps) {
  const [noTitle, setNoTitle] = useState(false);
  const [noLink, setNoLink] = useState(false);

  return (
    <Stack gap={3}>
      <Stack gap={1} alignItems={"flex-start"}>
        <Typography level="title-md">Title</Typography>
        <Input
          error={noTitle}
          size="lg"
          placeholder="Enter title here..."
          variant="outlined"
          onChange={(e) => {
            setTitle(e.target.value);
            setNoTitle(false);
          }}
          fullWidth
        />
      </Stack>
      <Stack gap={1} alignItems={"flex-start"}>
        <Typography level="title-md">Link</Typography>
        <Input
          error={noLink}
          size="lg"
          placeholder="Enter link here..."
          variant="outlined"
          onChange={(e) => {
            setLink(e.target.value);
            setNoLink(false);
          }}
          endDecorator={
            <Tooltip title="Link has to be a full url. Eg. https://example.com">
              <InfoOutlinedIcon color="warning" />
            </Tooltip>
          }
          fullWidth
        />
      </Stack>
      <Box display={"flex"} justifyContent={"flex-end"}>
        <Button onClick={handlePost} variant="soft">
          <Typography level={"body-lg"}>Post</Typography>
        </Button>
      </Box>
    </Stack>
  );
}
export default LinkForm;

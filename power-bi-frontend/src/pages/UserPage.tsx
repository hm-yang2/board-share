import { useEffect, useState } from "react";
import { Link } from "../models/Link";
import { GetLink } from "../api/LinkCalls";
import LinkCard from "../components/link/LinkCard";
import { Box, Stack } from "@mui/joy";
import SmallLinkCard from "../components/link/SmallLinkCard";

function UserPage() {
  const [link, setLink] = useState<Link>({
    id: 0,
    user: { id: 0, email: "Guest", dateCreated: new Date() }, // Assuming User has at least `id` and `name`
    title: "Temporary Video",
    description: "This is a placeholder video.",
    link: "https://www.youtube.com/embed/dQw4w9WgXcQ", // Example: YouTube embed link
    dateCreated: new Date(),
  });

  useEffect(() => {
    GetLink(12).then((gottenLink) => {
      console.log(gottenLink);
      setLink(gottenLink);
    });
  }, []);

  return (
    <Box
      display={"flex"}
      position={"absolute"}
      minHeight={"100vh"}
      minWidth={"99vw"}
      top={"8vh"}
      left={0}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <Stack gap={10}>
        <LinkCard link={link} />
        <SmallLinkCard link={link} />
      </Stack>
    </Box>
  );
}
export default UserPage;

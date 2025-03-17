import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom'; // For extracting the URL parameters
import axios from 'axios'; // For making API requests
import LinkCard from '../components/link/LinkCard';
import { Link } from '../models/Link';
import { GetLink } from '../api/LinkCalls';
import { Box } from '@mui/joy';

function LinkPage() {
  const { id } = useParams(); // Get the linkId from the URL
  const [link, setLink] = useState<Link | null>(null); // State to hold the fetched link

  useEffect(() => {
    if (id) {
      GetLink(id).then((gottenLink) => {
            console.log(gottenLink);
            setLink(gottenLink);
          });
    }
  }, [id]); // Fetch link when the linkId in the URL changes

  if (!link) {
    return <div>Loading...</div>; // Show loading while fetching the link data
  }

  return (
    <Box
      paddingBottom={4}
      display={"flex"}
      position={"absolute"}
      // minHeight={"99vh"}
      minWidth={"99vw"}
      top={"7vh"}
      left={0}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <LinkCard link={link} width='100vw' height='89vh' />
    </Box>
  );
}

export default LinkPage;

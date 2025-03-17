import { Box, Button, Stack, Typography } from "@mui/joy";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import HomeIcon from "@mui/icons-material/Home";
import AddIcon from "@mui/icons-material/Add";
import { AccountCircle } from "@mui/icons-material";
import { JSX } from "react/jsx-runtime";

const Links = [
  { name: "Home", path: "/", icon: <HomeIcon /> },
  { name: "Add Link", path: "/create-link", icon: <AddIcon /> },
  { name: "Profile", path: "/profile", icon: <AccountCircle /> },
];

/**
 * Navigation component for the NavBar
 * @param to path to navigate to
 * @param name name to display
 * @returns NavLink component
 */
const NavLink = ({
  to,
  name,
  icon,
}: {
  to: string;
  name: string;
  icon: JSX.Element;
}) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isActive, setIsActive] = useState(false);

  useEffect(() => {
    setIsActive(location.pathname == to);
  }, [location.pathname, to]);

  return (
    <Button
      onClick={() => navigate(to)}
      variant={isActive ? "soft" : "plain"}
      startDecorator={icon}
      sx={{ outline: "none", "&:focus": { outline: "none" } }}
    >
      <Typography level="h4" fontWeight="normal">
        {name}
      </Typography>
    </Button>
  );
};

/**
 * Navigation top bar for the whole application
 * @returns NavBar component
 */
function NavBar() {
  return (
    <Box
      width="100%"
      height="60px"
      bgcolor="lightblue"
      display="flex"
      alignItems="center"
      justifyContent="center"
      position="fixed"
      top="0"
      left="0"
      zIndex="1000"
      px={10}
      boxShadow="0px 2px 4px rgba(0, 0, 0, 0.1)"
    >
      <Stack
        direction={"row"}
        spacing={5}
        alignItems={"center"}
        justifyContent="left"
        width="100%"
      >
        <Typography level="h3" fontWeight="normal">
          Global OPS Dashboard
        </Typography>
        <Stack direction={"row"} spacing={3}>
          {Links.map((link) => (
            <NavLink
              key={link.path}
              to={link.path}
              name={link.name}
              icon={link.icon}
            />
          ))}
        </Stack>
      </Stack>
    </Box>
  );
}

export default NavBar;

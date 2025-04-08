import { Box, Input } from "@mui/joy";
import SearchIcon from "@mui/icons-material/Search";

interface SearchBarProps {
  onSearch: (query: string) => void;
}
/**
 * Search bar component for filtering or searching items.
 * @param onSearch Callback function triggered when the user presses Enter.
 * @returns The SearchBar component.
 */
function SearchBar({ onSearch }: SearchBarProps) {
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const query = event.target.value;
    onSearch(query);
  };

  return (
    <Box width={"75vw"}>
      <Input
        size="lg"
        startDecorator={<SearchIcon />}
        placeholder="Search channels"
        autoFocus
        onChange={handleChange}
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
        }}
      />
    </Box>
  );
}
export default SearchBar;

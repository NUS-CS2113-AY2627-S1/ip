/**
 * Converts raw user input into command words and their arguments.
 */
public class Parser {

    /**
     * Creates a parser for command-line input.
     */
    public Parser() {
    }

    /**
     * Separates a command line into its first word and the remaining text.
     *
     * @param input the trimmed command line entered by the user
     * @return the parsed command
     */
    public ParsedCommand parse(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";
        return new ParsedCommand(command, arguments);
    }
}

package nga;

/** Extracts a command word and its arguments from one input line. */
public final class CommandParser {
    private CommandParser() {
    }

    /**
     * Parses one input line without executing it.
     *
     * @param input the raw line entered by the user
     * @return the command keyword, arguments, and trimmed original input
     */
    public static ParsedCommand parse(String input) {
        String original = input == null ? "" : input.trim();
        if (original.isEmpty()) {
            return new ParsedCommand("", "", original);
        }
        int firstSpace = original.indexOf(' ');
        if (firstSpace < 0) {
            return new ParsedCommand(original.toLowerCase(), "", original);
        }
        return new ParsedCommand(original.substring(0, firstSpace).toLowerCase(),
                original.substring(firstSpace + 1).trim(), original);
    }
}

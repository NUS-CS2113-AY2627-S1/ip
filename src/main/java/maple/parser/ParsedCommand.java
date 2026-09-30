package maple.parser;

/**
 * Represents a user command separated into its keyword and details.
 */
public class ParsedCommand {
    private final String keyword;
    private final String detail;

    /**
     * Constructs a parsed command with the given keyword and details.
     *
     * @param keyword the command keyword.
     * @param detail the remaining command details.
     */
    public ParsedCommand(String keyword, String detail) {
        this.keyword = keyword;
        this.detail = detail;
    }

    /**
     * Returns the command keyword.
     *
     * @return the command keyword.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns the remaining command details.
     *
     * @return the remaining command details.
     */
    public String getDetail() {
        return detail;
    }
}

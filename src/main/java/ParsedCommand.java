/**
 * Represents a command word and the argument text supplied with it.
 */
public class ParsedCommand {
    private final String command;
    private final String arguments;

    /**
     * Creates a parsed command.
     *
     * @param command the command word
     * @param arguments the text following the command word
     */
    public ParsedCommand(String command, String arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    /**
     * Returns the command word.
     *
     * @return the command word
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the command arguments.
     *
     * @return the text after the command word
     */
    public String getArguments() {
        return arguments;
    }
}

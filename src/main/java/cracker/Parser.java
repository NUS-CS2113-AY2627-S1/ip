package cracker;

/**
 * Converts user-entered text into executable commands.
 */
public final class Parser {
    /** Prevents instantiation of this utility class. */
    private Parser() {
    }

    /**
     * Creates the command represented by the supplied user input.
     *
     * @param fullCommand command text entered by the user
     * @return command represented by the input
     * @throws CrackerException if the command is unknown or malformed
     */
    public static Command parse(String fullCommand) throws CrackerException {
        String commandName = fullCommand.trim();
        if (commandName.equals("list")) {
            return new ListCommand();
        }
        if (commandName.equals("bye")) {
            return new ExitCommand();
        }
        if (hasCommandName(commandName, "find")) {
            return new FindCommand(getDetails(commandName, "find"));
        }
        if (hasCommandName(commandName, "mark")) {
            return new MarkCommand(getDetails(commandName, "mark"));
        }
        if (hasCommandName(commandName, "unmark")) {
            return new UnmarkCommand(getDetails(commandName, "unmark"));
        }
        if (hasCommandName(commandName, "delete")) {
            return new DeleteCommand(getDetails(commandName, "delete"));
        }
        if (hasCommandName(commandName, "todo")) {
            return new TodoCommand(getDetails(commandName, "todo"));
        }
        if (hasCommandName(commandName, "deadline")) {
            return parseDeadline(getDetails(commandName, "deadline"));
        }
        if (hasCommandName(commandName, "event")) {
            return parseEvent(getDetails(commandName, "event"));
        }
        throw new CrackerException("I don't recognize that command. Use todo, deadline, event, find, list, mark, "
                + "unmark, delete, or bye.");
    }

    /**
     * Returns whether an input is a command name or starts with one followed by whitespace.
     *
     * @param input input text without surrounding whitespace
     * @param commandName command name to check
     * @return whether the input starts with the command name
     */
    private static boolean hasCommandName(String input, String commandName) {
        return input.equals(commandName)
                || input.startsWith(commandName) && Character.isWhitespace(input.charAt(commandName.length()));
    }

    /**
     * Returns the text after a recognized command name.
     *
     * @param input command input
     * @param commandName recognized command name
     * @return command details without surrounding whitespace
     */
    private static String getDetails(String input, String commandName) {
        return input.substring(commandName.length()).trim();
    }

    /**
     * Parses the description and due time of a deadline command.
     *
     * @param details text after the deadline command name
     * @return deadline command represented by the details
     * @throws CrackerException if the deadline details are incomplete
     */
    private static Command parseDeadline(String details) throws CrackerException {
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new CrackerException("A deadline needs /by followed by a due time.");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new CrackerException("A deadline needs a description before /by.");
        }
        if (by.isEmpty()) {
            throw new CrackerException("A deadline needs a due time after /by.");
        }
        return new DeadlineCommand(description, by);
    }

    /**
     * Parses the description, start time, and end time of an event command.
     *
     * @param details text after the event command name
     * @return event command represented by the details
     * @throws CrackerException if the event details are incomplete
     */
    private static Command parseEvent(String details) throws CrackerException {
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to");
        if (fromIndex < 0 || toIndex <= fromIndex) {
            throw new CrackerException("An event needs /from and /to, for example: event meeting /from 2pm /to 3pm");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = details.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new CrackerException("An event needs a description before /from.");
        }
        if (from.isEmpty()) {
            throw new CrackerException("An event needs a start time after /from.");
        }
        if (to.isEmpty()) {
            throw new CrackerException("An event needs an end time after /to.");
        }
        return new EventCommand(description, from, to);
    }
}

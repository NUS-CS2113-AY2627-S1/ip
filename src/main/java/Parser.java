/**
 * Parses user input commands and arguments.
 */
public class Parser {

    /**
     * Extracts the first word of a user command in lowercase.
     *
     * @param fullCommand complete command entered by the user
     * @return normalized command word
     */
    public static String getCommandWord(String fullCommand) {
        String[] parts = fullCommand.trim().split(" ", 2);
        return parts[0].toLowerCase();
    }

    /**
     * Extracts the text following a command word.
     *
     * @param fullCommand complete command entered by the user
     * @return trimmed command arguments, or an empty string when absent
     */
    public static String getArguments(String fullCommand) {
        String[] parts = fullCommand.trim().split(" ", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /**
     * Converts one task-number argument to an integer.
     *
     * @param arguments task-number text entered by the user
     * @param commandName name of the command used in an error message
     * @return the parsed task number
     * @throws CS2113BotException if exactly one whole-number argument is not supplied
     */
    public static int parseTaskNumber(String arguments, String commandName) throws CS2113BotException {
        String[] words = arguments.split("\\s+");
        if (arguments.isEmpty() || words.length != 1) {
            throw new CS2113BotException("Please use: " + commandName + " <task number>");
        }
        try {
            return Integer.parseInt(words[0]);
        } catch (NumberFormatException e) {
            throw new CS2113BotException("The task number must be a whole number.");
        }
    }
}


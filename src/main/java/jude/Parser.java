package jude;

import jude.exception.JudeException;

/**
 * Parses user input into commands and arguments.
 */
public class Parser {
    private static final String BY_PREFIX_WITH_SPACE = " /by";
    private static final String BY_PREFIX = "/by ";
    private static final String FROM_PREFIX_WITH_SPACE = " /from";
    private static final String FROM_PREFIX = "/from ";
    private static final String TO_PREFIX_WITH_SPACE = " /to";
    private static final String TO_PREFIX = "/to ";

    /**
     * Extracts the action keyword from the command.
     *
     * @param command The full user command.
     * @return The action keyword (e.g., "todo", "list").
     */
    public static String getAction(String command) {
        return command.split(" ")[0];
    }

    /**
     * Extracts the task number from the command.
     *
     * @param command The full user command.
     * @return The task number as an integer.
     */
    public static int getTaskNumber(String command) {
        return Integer.parseInt(command.split(" ")[1]);
    }

    /**
     * Checks if the command has an empty description for the specified action.
     *
     * @param command The full user command.
     * @param action  The action keyword.
     * @throws JudeException If the description is empty.
     */
    public static void checkEmptyDescription(String command, String action) throws JudeException {
        if (command.trim().equals(action)) {
            throw new JudeException("OOPS!!! The description of a " + action + " cannot be empty.");
        }
    }

    /**
     * Parses the description of a todo task.
     *
     * @param command The full user command.
     * @return The description of the todo task.
     */
    public static String parseTodoDescription(String command) {
        return command.substring("todo ".length());
    }

    /**
     * Parses the details of a deadline task.
     *
     * @param command The full user command.
     * @return An array containing the description and the deadline time.
     */
    public static String[] parseDeadlineDetails(String command) {
        String description = command.substring("deadline ".length(), command.indexOf(BY_PREFIX_WITH_SPACE));
        String by = command.substring(command.indexOf(BY_PREFIX) + BY_PREFIX.length());
        return new String[]{description, by};
    }

    /**
     * Parses the details of an event task.
     *
     * @param command The full user command.
     * @return An array containing the description, start time, and end time.
     */
    public static String[] parseEventDetails(String command) {
        String description = command.substring("event ".length(), command.indexOf(FROM_PREFIX_WITH_SPACE));
        String start = command.substring(command.indexOf(FROM_PREFIX) + FROM_PREFIX.length(), command.indexOf(TO_PREFIX_WITH_SPACE));
        String end = command.substring(command.indexOf(TO_PREFIX) + TO_PREFIX.length());
        return new String[]{description, start, end};
    }

    /**
     * Parses the keyword for a find command.
     *
     * @param command The full user command.
     * @return The keyword to search for.
     * @throws JudeException If the search keyword is empty.
     */
    public static String parseFindKeyword(String command) throws JudeException {
        if (command.trim().equals("find")) {
            throw new JudeException("OOPS!!! The search keyword cannot be empty.");
        }
        return command.substring("find ".length());
    }
}
package cole;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Makes sense of the user's input: recognises commands and turns their
 * arguments into tasks or task numbers.
 */
public class Parser {
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";

    /** Separator used between fields in the data file, so it must not appear in user input. */
    private static final String SAVE_SEPARATOR = "|";

    private static final String ERROR_UNKNOWN_COMMAND =
            "OOPS!!! I have no idea what that command means, sorry!";

    private static final String ERROR_EVENT_TIME =
            "OOPS!!! Please specify the event time using /from and /to, e.g. \"event meeting /from Mon 2pm /to 4pm\".";
    private static final String ERROR_EVENT_DESCRIPTION =
            "OOPS!!! Umm... what's the event? You didn't give me a description.";

    /**
     * Returns true if the input is a todo, deadline or event command.
     *
     * @param input the full command typed by the user
     * @return whether the input asks to add a new task
     */
    public static boolean isAddTaskCommand(String input) {
        return isCommand(input, COMMAND_TODO)
                || isCommand(input, COMMAND_DEADLINE)
                || isCommand(input, COMMAND_EVENT);
    }

    /**
     * Returns true if the input is a find command.
     *
     * @param input the full command typed by the user
     * @return whether the input asks to search for tasks
     */
    public static boolean isFindCommand(String input) {
        return isCommand(input, COMMAND_FIND);
    }

    /**
     * Extracts the search keyword from a command like "find book".
     *
     * @param input the full command typed by the user
     * @return the keyword, with surrounding spaces removed
     * @throws ColeException if no keyword is given
     */
    public static String parseFindKeyword(String input) throws ColeException {
        String keyword = argumentsOf(input, COMMAND_FIND).trim();
        requireNonEmpty(keyword, "OOPS!!! What should I search for? e.g. \"find book\".");
        return keyword;
    }

    /**
     * Creates the task described by a todo, deadline or event command.
     *
     * @param input the full command typed by the user
     * @return the new task
     * @throws ColeException if the description or time is missing or malformed,
     *     or the input contains the "|" character used by the data file
     */
    public static Task parseTask(String input) throws ColeException {
        if (input.contains(SAVE_SEPARATOR)) {
            throw new ColeException("OOPS!!! Sorry, I can't save the \"" + SAVE_SEPARATOR
                    + "\" character. Please leave it out.");
        }

        if (isCommand(input, COMMAND_TODO)) {
            return parseTodo(input);
        } else if (isCommand(input, COMMAND_DEADLINE)) {
            return parseDeadline(input);
        } else if (isCommand(input, COMMAND_EVENT)) {
            return parseEvent(input);
        }
        throw new ColeException(ERROR_UNKNOWN_COMMAND);
    }

    /**
     * Extracts the task number from commands like "mark 2" and converts it to a 0-based index.
     *
     * @param input the full command typed by the user
     * @return the 0-based index of the task
     * @throws NumberFormatException if the task number is missing or not an integer
     */
    public static int parseTaskIndex(String input) {
        String[] words = input.trim().split("\\s+");
        if (words.length < 2) {
            throw new NumberFormatException("Missing task number");
        }
        return Integer.parseInt(words[1]) - 1;
    }

    /**
     * Returns the message shown when the user types a command Cole does not know.
     *
     * @return the unknown command error message
     */
    public static String getUnknownCommandMessage() {
        return ERROR_UNKNOWN_COMMAND;
    }

    /**
     * Creates a todo from a command like "todo read book".
     *
     * @throws ColeException if the description is missing
     */
    private static Task parseTodo(String input) throws ColeException {
        String description = argumentsOf(input, COMMAND_TODO).trim();
        requireNonEmpty(description, "OOPS!!! You forgot to tell me what the todo is about!");
        return new ToDo(description);
    }

    /**
     * Creates a deadline from a command like "deadline return book /by 2019-10-15".
     *
     * @throws ColeException if /by, the description or the date is missing,
     *     or the date is not in yyyy-mm-dd format
     */
    private static Task parseDeadline(String input) throws ColeException {
        String[] deadlineParts = argumentsOf(input, COMMAND_DEADLINE).split(" /by ", 2);
        if (deadlineParts.length < 2) {
            throw new ColeException("OOPS!!! Please specify the deadline using /by, "
                    + "e.g. \"deadline return book /by 2019-10-15\".");
        }

        String description = deadlineParts[0].trim();
        String by = deadlineParts[1];

        requireNonEmpty(description, "OOPS!!! What's the deadline for? Please add a description.");
        requireNonEmpty(by, "OOPS!!! What's the time for the deadline? Please add a specific time.");

        try {
            LocalDate byDate = LocalDate.parse(by.trim());
            return new Deadline(description, byDate);
        } catch (DateTimeParseException e) {
            throw new ColeException("OOPS!!! Please write the deadline date as yyyy-mm-dd, "
                    + "e.g. \"deadline return book /by 2019-10-15\".");
        }
    }

    /**
     * Creates an event from a command like "event meeting /from Mon 2pm /to 4pm".
     *
     * @throws ColeException if the description, /from or /to part is missing
     */
    private static Task parseEvent(String input) throws ColeException {
        String content = argumentsOf(input, COMMAND_EVENT);
        requireNonEmpty(content, ERROR_EVENT_DESCRIPTION);

        String[] eventParts = content.split(" /from ", 2);
        if (eventParts.length < 2) {
            throw new ColeException(ERROR_EVENT_TIME);
        }

        String description = eventParts[0].trim();
        requireNonEmpty(description, ERROR_EVENT_DESCRIPTION);

        String[] fromTo = eventParts[1].split(" /to ", 2);
        if (fromTo.length < 2) {
            throw new ColeException(ERROR_EVENT_TIME);
        }

        String from = fromTo[0].trim();
        String to = fromTo[1].trim();
        requireNonEmpty(from, ERROR_EVENT_TIME);
        requireNonEmpty(to, ERROR_EVENT_TIME);

        return new Event(description, from, to);
    }

    /**
     * Returns true if the input is exactly the command word or starts with it
     * followed by a space, so that "find" matches but "finder" does not.
     *
     * @param input the full command typed by the user
     * @param command the command word to check for, e.g. "mark"
     * @return whether the input is that command
     */
    public static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Returns everything after the command word, or an empty string if there is nothing.
     */
    private static String argumentsOf(String input, String command) {
        return input.length() > command.length() ? input.substring(command.length()) : "";
    }

    /**
     * Throws a ColeException with the given message if the value is empty or only spaces.
     */
    private static void requireNonEmpty(String value, String errorMessage) throws ColeException {
        if (value.trim().isEmpty()) {
            throw new ColeException(errorMessage);
        }
    }
}

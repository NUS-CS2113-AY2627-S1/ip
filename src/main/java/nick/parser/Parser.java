package nick.parser;

import nick.NickException;
import nick.command.AddCommand;
import nick.command.Command;
import nick.command.DeleteCommand;
import nick.command.ExitCommand;
import nick.command.FindCommand;
import nick.command.ListCommand;
import nick.command.MarkCommand;
import nick.task.Deadline;
import nick.task.Event;
import nick.task.Todo;

/**
 * Makes sense of raw user input: turns a command line into a Command object,
 * and parses the structured parts of deadline and event commands.
 */
public class Parser {
    private static final String BY_MARKER = " /by ";
    private static final String FROM_MARKER = " /from ";
    private static final String TO_MARKER = " /to ";

    /**
     * Parses a full command line into the matching Command.
     *
     * @param input The full command line entered by the user.
     * @return The Command to execute.
     * @throws NickException If the command is unknown or its argument is invalid.
     */
    public static Command parse(String input) throws NickException {
        String command = commandWord(input);
        String argument = argument(input);

        switch (command) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(argument, true);
        case "unmark":
            return new MarkCommand(argument, false);
        case "delete":
            return new DeleteCommand(argument);
        case "find":
            if (argument.isEmpty()) {
                throw new NickException("Please tell me what keyword to search for, e.g. find book.");
            }
            return new FindCommand(argument);
        case "todo":
            if (argument.isEmpty()) {
                throw new NickException("The description of a todo cannot be empty.");
            }
            return new AddCommand(new Todo(argument));
        case "deadline": {
            String[] parts = parseDeadline(argument);
            return new AddCommand(new Deadline(parts[0], parts[1]));
        }
        case "event": {
            String[] parts = parseEvent(argument);
            return new AddCommand(new Event(parts[0], parts[1], parts[2]));
        }
        default:
            throw new NickException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Returns the command word (the first token) of the input, lower-cased.
     *
     * @param input The full command line.
     * @return The command word, e.g. "todo" or "list".
     */
    public static String commandWord(String input) {
        String trimmed = input.trim();
        int space = trimmed.indexOf(' ');
        return (space < 0 ? trimmed : trimmed.substring(0, space)).toLowerCase();
    }

    /**
     * Returns the argument of the input: everything after the command word.
     *
     * @param input The full command line.
     * @return The argument, trimmed, or an empty string if there is none.
     */
    public static String argument(String input) {
        String trimmed = input.trim();
        int space = trimmed.indexOf(' ');
        return space < 0 ? "" : trimmed.substring(space + 1).trim();
    }

    /**
     * Parses a task number argument into a zero-based index.
     *
     * @param argument The text following the command word.
     * @param size The number of tasks currently in the list.
     * @return The zero-based index of the referenced task.
     * @throws NickException If the argument is missing, not a number, or out of range.
     */
    public static int parseTaskNumber(String argument, int size) throws NickException {
        String trimmed = argument.trim();
        if (trimmed.isEmpty()) {
            throw new NickException("Please tell me which task number to update, e.g. mark 2.");
        }

        int index;
        try {
            index = Integer.parseInt(trimmed) - 1;
        } catch (NumberFormatException e) {
            throw new NickException("'" + trimmed + "' is not a valid task number.");
        }

        if (index < 0 || index >= size) {
            throw new NickException("There is no task number " + trimmed + " in your list.");
        }
        return index;
    }

    /**
     * Splits a deadline argument into its description and "by" time.
     *
     * @param argument The text following the deadline command word.
     * @return A two-element array: {description, by}.
     * @throws NickException If the /by marker or either part is missing.
     */
    public static String[] parseDeadline(String argument) throws NickException {
        int byIndex = argument.indexOf(BY_MARKER);
        if (byIndex < 0) {
            throw new NickException("A deadline needs a '/by' time, e.g. deadline return book /by Sunday.");
        }
        String description = argument.substring(0, byIndex).trim();
        String by = argument.substring(byIndex + BY_MARKER.length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new NickException("A deadline needs both a description and a '/by' time.");
        }
        return new String[] {description, by};
    }

    /**
     * Splits an event argument into its description, "from" time, and "to" time.
     *
     * @param argument The text following the event command word.
     * @return A three-element array: {description, from, to}.
     * @throws NickException If the /from or /to markers or any part is missing.
     */
    public static String[] parseEvent(String argument) throws NickException {
        int fromIndex = argument.indexOf(FROM_MARKER);
        int toIndex = argument.indexOf(TO_MARKER);
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new NickException("An event needs a '/from' and a '/to' time, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm.");
        }
        String description = argument.substring(0, fromIndex).trim();
        String from = argument.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String to = argument.substring(toIndex + TO_MARKER.length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new NickException("An event needs a description, a '/from' time, and a '/to' time.");
        }
        return new String[] {description, from, to};
    }
}

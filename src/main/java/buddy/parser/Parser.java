package buddy.parser;

import buddy.command.AddCommand;
import buddy.command.Command;
import buddy.command.DeleteCommand;
import buddy.command.ExitCommand;
import buddy.command.FindCommand;
import buddy.command.ListCommand;
import buddy.command.MarkCommand;
import buddy.command.UnmarkCommand;
import buddy.exception.BuddyException;
import buddy.task.Deadline;
import buddy.task.Event;
import buddy.task.Todo;

/**
 * Makes sense of the raw text the user types: splits a line into a command
 * word and its arguments, and turns argument text into the values a command
 * needs (e.g. a deadline's description and due date).
 */
public class Parser {

    private Parser() {
    }

    /**
     * Parses a full line typed by the user into a {@link Command} ready to be executed.
     *
     * @param input full line typed by the user
     * @return the command described by the input
     * @throws BuddyException if the command is unknown or its arguments are invalid
     */
    public static Command parse(String input) throws BuddyException {
        String command = getCommandWord(input);
        String arguments = getArguments(input);

        switch (command) {
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseTaskNumber(arguments, "mark"));
        case "unmark":
            return new UnmarkCommand(parseTaskNumber(arguments, "unmark"));
        case "todo":
            return new AddCommand(new Todo(parseDescription(arguments, "A todo", "todo")));
        case "deadline":
            return new AddCommand(parseDeadline(arguments));
        case "event":
            return new AddCommand(parseEvent(arguments));
        case "delete":
            return new DeleteCommand(parseTaskNumber(arguments, "delete"));
        case "find":
            return new FindCommand(parseKeyword(arguments));
        case "bye":
            return new ExitCommand();
        default:
            throw new BuddyException(
                    "I don't recognise that command. Try todo, deadline, event, list, find, mark, unmark, "
                            + "delete, or bye.");
        }
    }

    /**
     * Returns the command word, i.e. the first word of the input.
     *
     * @param input full line typed by the user
     * @return the command word
     */
    private static String getCommandWord(String input) {
        return input.split(" ", 2)[0];
    }

    /**
     * Returns everything after the command word, with surrounding whitespace trimmed.
     *
     * @param input full line typed by the user
     * @return the arguments, or an empty string if there are none
     */
    private static String getArguments(String input) {
        String[] parts = input.split(" ", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /**
     * Checks that a task description is present, since todo, deadline, and
     * event tasks are meaningless without one.
     *
     * @param arguments text supplied after the command word
     * @param taskLabel task type with its article, e.g. "A todo" or "An event", used in the error message
     * @param commandWord command word used in the example, e.g. "todo"
     * @return the description, unchanged
     * @throws BuddyException if the description is blank
     */
    private static String parseDescription(String arguments, String taskLabel, String commandWord)
            throws BuddyException {
        if (arguments.isBlank()) {
            throw new BuddyException(taskLabel + " needs a description, e.g. \"" + commandWord + " read book\".");
        }
        return arguments;
    }

    /**
     * Parses the arguments of a deadline command into a {@link Deadline}.
     *
     * @param arguments text supplied after the command word, e.g. "return book /by Sunday"
     * @return the deadline described by the arguments
     * @throws BuddyException if the description or due date is missing
     */
    private static Deadline parseDeadline(String arguments) throws BuddyException {
        int byIndex = arguments.indexOf(" /by ");
        if (byIndex == -1) {
            parseDescription(arguments, "A deadline", "deadline");
            throw new BuddyException(
                    "A deadline needs a due date after /by, e.g. \"deadline return book /by Sunday\".");
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new BuddyException("A deadline needs a description before /by, e.g. \"deadline return book /by Sunday\".");
        }
        if (by.isEmpty()) {
            throw new BuddyException("A deadline needs a due date after /by, e.g. \"deadline return book /by Sunday\".");
        }
        return new Deadline(description, by);
    }

    /**
     * Parses the arguments of an event command into an {@link Event}.
     *
     * @param arguments text supplied after the command word, e.g. "meeting /from Mon 2pm /to 4pm"
     * @return the event described by the arguments
     * @throws BuddyException if the description, start time, or end time is missing
     */
    private static Event parseEvent(String arguments) throws BuddyException {
        int fromIndex = arguments.indexOf(" /from ");
        int toIndex = arguments.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            parseDescription(arguments, "An event", "event");
            throw new BuddyException(
                    "An event needs a start and end time, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = arguments.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new BuddyException(
                    "An event needs a description before /from, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("An event needs both a start (/from) and an end (/to) time.");
        }
        return new Event(description, from, to);
    }

    /**
     * Checks that a search keyword is present, since a find command with
     * nothing to search for is meaningless.
     *
     * @param arguments text supplied after the command word
     * @return the keyword, unchanged
     * @throws BuddyException if no keyword was given
     */
    private static String parseKeyword(String arguments) throws BuddyException {
        if (arguments.isBlank()) {
            throw new BuddyException("Tell me what to search for, e.g. \"find book\".");
        }
        return arguments;
    }

    /**
     * Parses the task number that should follow a mark, unmark, or delete command.
     *
     * @param arguments text supplied after the command word
     * @param commandName name of the command, used in the error message
     * @return the parsed task number
     * @throws BuddyException if no number was given or it is not a valid integer
     */
    private static int parseTaskNumber(String arguments, String commandName) throws BuddyException {
        if (arguments.isBlank()) {
            throw new BuddyException("Tell me which task number to " + commandName + ", e.g. \"" + commandName + " 2\".");
        }
        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new BuddyException("\"" + arguments + "\" is not a valid task number.");
        }
    }
}

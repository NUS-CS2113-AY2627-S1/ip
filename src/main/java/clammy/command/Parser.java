package clammy.command;

import java.util.Locale;

import clammy.exception.ClammyException;
import clammy.exception.ParseException;
import clammy.exception.UnknownCommandException;
import clammy.task.Deadline;
import clammy.task.Event;
import clammy.task.Todo;

/**
 * Converts user input into structured commands and task values.
 */
public final class Parser {
    private static final String VALID_COMMANDS = "I don't recognize that command. Valid commands are:\n"
            + "todo DESCRIPTION\n"
            + "deadline DESCRIPTION /by DATE_OR_TIME\n"
            + "event DESCRIPTION /from START /to END\n"
            + "list\n"
            + "find KEYWORD\n"
            + "mark TASK_NUMBER\n"
            + "unmark TASK_NUMBER\n"
            + "delete TASK_NUMBER\n"
            + "bye";
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    private Parser() {
    }

    /**
     * Validates user input and creates the command that will execute it.
     *
     * @param input Raw user input.
     * @return Executable command containing parsed task values or task numbers.
     * @throws ClammyException If the keyword is unknown or its arguments are malformed.
     */
    public static Command parse(String input) throws ClammyException {
        String normalizedInput = input.trim();
        String[] parts = normalizedInput.split("\\s+", 2);
        String keyword = parts[0].toLowerCase(Locale.ROOT);
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return switch (keyword) {
        case "bye" -> {
            requireNoArguments(arguments, "bye");
            yield new ExitCommand();
        }
        case "list" -> {
            requireNoArguments(arguments, "list");
            yield new ListCommand();
        }
        case "mark" -> new MarkCommand(parseTaskNumber(arguments));
        case "unmark" -> new UnmarkCommand(parseTaskNumber(arguments));
        case "delete" -> new DeleteCommand(parseTaskNumber(arguments));
        case "find" -> {
            if (arguments.isBlank()) {
                throw new ParseException("A find command must have a keyword. Use: find KEYWORD");
            }
            yield new FindCommand(arguments);
        }
        case "todo" -> new AddCommand(parseTodo(arguments));
        case "deadline" -> new AddCommand(parseDeadline(arguments));
        case "event" -> new AddCommand(parseEvent(arguments));
        default -> throw new UnknownCommandException(VALID_COMMANDS);
        };
    }

    /**
     * Rejects extra arguments for commands that do not accept them.
     */
    private static void requireNoArguments(String arguments, String commandWord) throws ParseException {
        if (!arguments.isEmpty()) {
            throw new ParseException("The " + commandWord + " command does not take arguments.");
        }
    }

    /**
     * Returns the task number in a mark, unmark, or delete command.
     *
     * @param arguments Text following the command keyword.
     * @return Positive task number supplied by the user.
     * @throws ParseException If the argument is not a positive integer.
     */
    private static int parseTaskNumber(String arguments) throws ParseException {
        try {
            int taskNumber = Integer.parseInt(arguments);
            if (taskNumber < 1) {
                throw new ParseException("Please provide a positive task number.");
            }
            return taskNumber;
        } catch (NumberFormatException exception) {
            throw new ParseException("Please provide a valid task number.");
        }
    }

    /**
     * Creates a todo after checking that its description is present.
     */
    private static Todo parseTodo(String description) throws ParseException {
        if (description.isBlank()) {
            throw new ParseException("A todo must have a description.");
        }
        return new Todo(description);
    }

    /**
     * Extracts the description and deadline, reporting invalid dates as command errors.
     */
    private static Deadline parseDeadline(String arguments) throws ParseException {
        int byIndex = arguments.indexOf(DEADLINE_SEPARATOR);
        if (byIndex <= 0 || byIndex + DEADLINE_SEPARATOR.length() >= arguments.length()) {
            throw new ParseException("A deadline must follow: deadline DESCRIPTION /by DATE_OR_TIME");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + DEADLINE_SEPARATOR.length()).trim();
        try {
            return new Deadline(description, by);
        } catch (IllegalArgumentException exception) {
            throw new ParseException(exception.getMessage());
        }
    }

    /**
     * Extracts an event's description, start, and end after checking the required separators.
     */
    private static Event parseEvent(String arguments) throws ParseException {
        int fromIndex = arguments.indexOf(EVENT_FROM_SEPARATOR);
        int toIndex = arguments.indexOf(EVENT_TO_SEPARATOR,
                fromIndex + EVENT_FROM_SEPARATOR.length());
        boolean hasMissingField = fromIndex <= 0
                || toIndex <= fromIndex + EVENT_FROM_SEPARATOR.length()
                || toIndex + EVENT_TO_SEPARATOR.length() >= arguments.length();
        if (hasMissingField) {
            throw new ParseException("An event must follow: event DESCRIPTION /from START /to END");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + EVENT_FROM_SEPARATOR.length(), toIndex).trim();
        String to = arguments.substring(toIndex + EVENT_TO_SEPARATOR.length()).trim();
        return new Event(description, from, to);
    }
}

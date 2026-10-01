package floppy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import floppy.command.AddCommand;
import floppy.command.BlankCommand;
import floppy.command.Command;
import floppy.command.DeleteCommand;
import floppy.command.ExitCommand;
import floppy.command.FindCommand;
import floppy.command.ListCommand;
import floppy.command.MarkCommand;
import floppy.task.Deadline;
import floppy.task.Event;
import floppy.task.Todo;

/**
 * Deals with making sense of the user's commands: recognising the command word,
 * pulling out its arguments, and building tasks from them.
 */
public class Parser {

    /** The command that makes Floppy exit. */
    public static final String COMMAND_EXIT = "bye";

    /** The command that makes Floppy print every task it is holding. */
    public static final String COMMAND_LIST = "list";

    /** The command that marks a task as done. */
    public static final String COMMAND_MARK = "mark";

    /** The command that marks a task as not done. */
    public static final String COMMAND_UNMARK = "unmark";

    /** The command that removes a task from the list. */
    public static final String COMMAND_DELETE = "delete";

    /** The command that adds a task with no date or time. */
    public static final String COMMAND_TODO = "todo";

    /** The command that adds a task due by a given time. */
    public static final String COMMAND_DEADLINE = "deadline";

    /** The command that adds a task spanning a start and an end time. */
    public static final String COMMAND_EVENT = "event";

    /** The command that searches task descriptions for a keyword. */
    public static final String COMMAND_FIND = "find";

    /** Separates a deadline's description from its due time. */
    public static final String MARKER_BY = "/by";

    /** Separates an event's description from its start time. */
    public static final String MARKER_FROM = "/from";

    /** Separates an event's start time from its end time. */
    public static final String MARKER_TO = "/to";

    /** The date format Floppy accepts from the user. */
    private static final String DATE_FORMAT_HINT = "yyyy-mm-dd";

    /** Regular expression matching a whole number, with an optional leading sign. */
    private static final String WHOLE_NUMBER_REGEX = "[+-]?\\d+";

    /** Stands in for a task number too large to hold in an int; never names a real task. */
    private static final int TASK_NUMBER_TOO_LARGE = -1;

    /** Regular expression matching the whitespace that separates words in a command. */
    private static final String WHITESPACE_REGEX = "\\s+";

    /** Hidden constructor: this class holds only static helpers and is never instantiated. */
    private Parser() {
    }

    /**
     * Returns the command the user asked for, built from what they typed.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the command to carry out.
     * @throws FloppyException if the command word is not recognised, or the command was
     *         given the wrong arguments.
     */
    public static Command parse(String input) throws FloppyException {
        // An exact match, so that a line merely starting with "bye" is not an exit.
        if (input.equalsIgnoreCase(COMMAND_EXIT)) {
            return new ExitCommand();
        }
        if (input.isEmpty()) {
            return new BlankCommand();
        }
        if (isCommand(input, COMMAND_LIST)) {
            requireNoArgument(input);
            return new ListCommand();
        }
        if (isCommand(input, COMMAND_MARK)) {
            return new MarkCommand(input, true);
        }
        if (isCommand(input, COMMAND_UNMARK)) {
            return new MarkCommand(input, false);
        }
        if (isCommand(input, COMMAND_DELETE)) {
            return new DeleteCommand(input);
        }
        if (isCommand(input, COMMAND_FIND)) {
            return new FindCommand(parseKeyword(input));
        }
        if (isCommand(input, COMMAND_TODO)) {
            return new AddCommand(parseTodo(input));
        }
        if (isCommand(input, COMMAND_DEADLINE)) {
            return new AddCommand(parseDeadline(input));
        }
        if (isCommand(input, COMMAND_EVENT)) {
            return new AddCommand(parseEvent(input));
        }
        throw new FloppyException("'" + commandWordOf(input) + "'? That's not in my directory. "
                + "I know todo, deadline, event, list, mark, unmark, delete, find and bye.");
    }

    /**
     * Returns the keyword the user wants to search for.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the keyword to look for.
     * @throws FloppyException if no keyword was given.
     */
    private static String parseKeyword(String input) throws FloppyException {
        String keyword = argumentOf(input);

        if (keyword.isEmpty()) {
            throw new FloppyException("Find what? Give me a keyword, e.g. '"
                    + COMMAND_FIND + " book'.");
        }
        return keyword;
    }

    /**
     * Checks that a command which takes no argument was given none.
     *
     * @param input the whole line the user entered, already stripped.
     * @throws FloppyException if anything followed the command word.
     */
    private static void requireNoArgument(String input) throws FloppyException {
        String argument = argumentOf(input);

        if (!argument.isEmpty()) {
            throw new FloppyException("'" + COMMAND_LIST + "' takes nothing after it. "
                    + "Drop the '" + argument + "' and I'll read the whole disk.");
        }
    }

    /**
     * Returns whether the user's input starts with the given command word.
     *
     * @param input       the whole line the user entered, already stripped.
     * @param commandWord the command word to look for.
     * @return true if the first word of the input is that command word.
     */
    public static boolean isCommand(String input, String commandWord) {
        return commandWordOf(input).equalsIgnoreCase(commandWord);
    }

    /**
     * Returns the first word of the user's input, which names the command.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the command word, or an empty string if the input was empty.
     */
    public static String commandWordOf(String input) {
        return input.split(WHITESPACE_REGEX, 2)[0];
    }

    /**
     * Returns everything the user typed after the command word.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the argument text, or an empty string if the command had no argument.
     */
    public static String argumentOf(String input) {
        String[] parts = input.split(WHITESPACE_REGEX, 2);
        return parts.length < 2 ? "" : parts[1].strip();
    }

    /**
     * Returns the task number given as the argument of a command such as {@code mark 2}.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the number, or {@value #TASK_NUMBER_TOO_LARGE} if it will not fit in an int,
     *         which no stored task can match.
     * @throws FloppyException if the number is missing or is not a whole number.
     */
    public static int parseTaskNumber(String input) throws FloppyException {
        String argument = argumentOf(input);

        if (argument.isEmpty()) {
            throw new FloppyException("Which one? Give me a number, e.g. '"
                    + commandWordOf(input) + " 2'.");
        }

        if (!argument.matches(WHOLE_NUMBER_REGEX)) {
            throw new FloppyException("'" + argument
                    + "' is not a number, and I only speak in sectors.");
        }

        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            // Too many digits for an int, so no stored task can carry this number.
            return TASK_NUMBER_TOO_LARGE;
        }
    }

    /**
     * Builds a todo from the user's input.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the new todo.
     * @throws FloppyException if the description was missing.
     */
    public static Todo parseTodo(String input) throws FloppyException {
        String description = argumentOf(input);

        if (description.isEmpty()) {
            throw new FloppyException("A todo needs a description, e.g. 'todo borrow book'.");
        }
        return new Todo(description);
    }

    /**
     * Builds a deadline from the user's input, splitting it at the {@value #MARKER_BY} marker.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the new deadline.
     * @throws FloppyException if the description or the due time was missing.
     */
    public static Deadline parseDeadline(String input) throws FloppyException {
        String[] parts = splitAtMarker(argumentOf(input), MARKER_BY);
        String description = parts[0].strip();
        String by = parts.length < 2 ? "" : parts[1].strip();

        if (description.isEmpty() || by.isEmpty()) {
            throw new FloppyException("A deadline needs a description and a date, e.g. "
                    + "'deadline return book " + MARKER_BY + " 2019-10-15'.");
        }
        return new Deadline(description, parseDate(by));
    }

    /**
     * Builds an event from the user's input, splitting it at the {@value #MARKER_FROM}
     * and {@value #MARKER_TO} markers.
     *
     * @param input the whole line the user entered, already stripped.
     * @return the new event.
     * @throws FloppyException if the description, start or end was missing.
     */
    public static Event parseEvent(String input) throws FloppyException {
        String[] fromParts = splitAtMarker(argumentOf(input), MARKER_FROM);
        String description = fromParts[0].strip();
        String from = "";
        String to = "";

        if (fromParts.length == 2) {
            String[] toParts = splitAtMarker(fromParts[1], MARKER_TO);
            from = toParts[0].strip();
            to = toParts.length < 2 ? "" : toParts[1].strip();
        }

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new FloppyException("An event needs a description, a start and an end, e.g. "
                    + "'event project meeting " + MARKER_FROM + " Mon 2pm " + MARKER_TO + " 4pm'.");
        }
        return new Event(description, from, to);
    }

    /**
     * Returns the date written in the given text.
     *
     * @param text a date in {@value #DATE_FORMAT_HINT} form.
     * @return the date it names.
     * @throws FloppyException if the text is not a date in that form.
     */
    public static LocalDate parseDate(String text) throws FloppyException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new FloppyException("I couldn't read '" + text + "' as a date. Use "
                    + DATE_FORMAT_HINT + ", e.g. 2019-10-15.");
        }
    }

    /**
     * Splits text at the first occurrence of a marker such as {@value #MARKER_BY},
     * discarding any whitespace around it.
     *
     * @param text   the text to split.
     * @param marker the marker to split at.
     * @return the text before the marker, followed by the text after it if present.
     */
    private static String[] splitAtMarker(String text, String marker) {
        return text.split("\\s*" + marker + "\\s*", 2);
    }
}

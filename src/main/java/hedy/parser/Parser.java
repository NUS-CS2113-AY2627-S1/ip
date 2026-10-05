package hedy.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import hedy.command.AddDeadlineCommand;
import hedy.command.AddEventCommand;
import hedy.command.AddTodoCommand;
import hedy.command.Command;
import hedy.command.DeleteCommand;
import hedy.command.ExitCommand;
import hedy.command.FindCommand;
import hedy.command.ListCommand;
import hedy.command.MarkCommand;
import hedy.command.OnCommand;
import hedy.exception.HedyException;

/**
 * Turns a line of user input into a {@link Command}.
 */
public class Parser {

    /**
     * Interprets one full command line.
     *
     * @param input the line the user typed, already trimmed
     * @return the command to run
     * @throws HedyException if the command is unknown or incomplete
     */
    public static Command parse(String input) throws HedyException {
        String command = firstWord(input);
        String arguments = argumentsOf(input);

        switch (command) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseTaskNumber(arguments, "mark"), true);
        case "unmark":
            return new MarkCommand(parseTaskNumber(arguments, "unmark"), false);
        case "todo":
            return new AddTodoCommand(parseDescription(arguments, "todo", "todo read book"));
        case "deadline":
            return parseDeadline(arguments);
        case "event":
            return parseEvent(arguments);
        case "delete":
            return new DeleteCommand(parseTaskNumber(arguments, "delete"));
        case "find":
            return new FindCommand(parseDescription(arguments, "find", "find book"));
        case "on":
            return new OnCommand(parseOnDate(arguments));
        default:
            throw new HedyException("OOPS!!! I don't know what that means. "
                    + "Try list, todo, deadline, event, mark, unmark, delete, find, on, or bye.");
        }
    }

    /**
     * Reads a deadline of the form {@code <description> /by yyyy-mm-dd}.
     */
    private static Command parseDeadline(String arguments) throws HedyException {
        String example = "deadline return book /by 2019-10-15";
        if (arguments.isEmpty()) {
            throw new HedyException("OOPS!!! The description of a deadline cannot be empty. Try: " + example);
        }
        String[] parts = arguments.split("\\s*/by\\s*", 2);
        if (parts.length < 2) {
            throw new HedyException("OOPS!!! A deadline needs a /by date. Try: " + example);
        }
        String description = parts[0].trim();
        String byText = parts[1].trim();
        if (description.isEmpty()) {
            throw new HedyException("OOPS!!! The description before /by cannot be empty. Try: " + example);
        }
        if (byText.isEmpty()) {
            throw new HedyException("OOPS!!! The date after /by cannot be empty. Try: " + example);
        }
        return new AddDeadlineCommand(description, parseDate(byText));
    }

    /**
     * Reads an event of the form {@code <description> /from <start> /to <end>}.
     * The start and end are stored as text. Only deadlines use calendar dates.
     */
    private static Command parseEvent(String arguments) throws HedyException {
        String example = "event project meeting /from Mon 2pm /to 4pm";
        if (arguments.isEmpty()) {
            throw new HedyException("OOPS!!! The description of an event cannot be empty. Try: " + example);
        }
        String[] fromParts = arguments.split("\\s*/from\\s*", 2);
        if (fromParts.length < 2) {
            throw new HedyException("OOPS!!! An event needs a /from start and a /to end. Try: " + example);
        }
        String description = fromParts[0].trim();
        String[] timeParts = fromParts[1].split("\\s*/to\\s*", 2);
        if (timeParts.length < 2) {
            throw new HedyException("OOPS!!! An event needs a /to end time. Try: " + example);
        }
        String start = timeParts[0].trim();
        String end = timeParts[1].trim();
        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            throw new HedyException("OOPS!!! The description, start, and end of an event cannot be empty. Try: "
                    + example);
        }
        return new AddEventCommand(description, start, end);
    }

    /**
     * Reads the date for {@code on yyyy-mm-dd}.
     */
    private static LocalDate parseOnDate(String arguments) throws HedyException {
        if (arguments.isEmpty()) {
            throw new HedyException("OOPS!!! Please give a date. Try: on 2019-10-15");
        }
        return parseDate(arguments.split("\\s+")[0]);
    }

    /**
     * Parses a date written as {@code yyyy-mm-dd}.
     *
     * @param text the date the user typed
     */
    private static LocalDate parseDate(String text) throws HedyException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new HedyException("OOPS!!! I need the date as yyyy-mm-dd, for example 2019-10-15.");
        }
    }

    /**
     * Requires a non-empty description or keyword.
     *
     * @param arguments text after the command word
     * @param commandName command being parsed, such as {@code todo} or {@code find}
     * @param example a valid command to show the user
     */
    private static String parseDescription(String arguments, String commandName, String example) throws HedyException {
        if (arguments.isEmpty()) {
            String what = commandName.equals("find") ? "keyword" : "description of a " + commandName;
            throw new HedyException("OOPS!!! The " + what + " cannot be empty. Try: " + example);
        }
        return arguments;
    }

    /**
     * Reads the task number at the start of the arguments.
     *
     * @param arguments text after the command word
     * @param commandName command to name in the error, such as {@code delete}
     */
    private static int parseTaskNumber(String arguments, String commandName) throws HedyException {
        if (arguments.isEmpty()) {
            throw new HedyException("OOPS!!! Please give a task number. Try: " + commandName + " 1");
        }
        String numberText = arguments.split("\\s+")[0];
        try {
            return Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new HedyException("OOPS!!! \"" + numberText + "\" is not a task number. Try: " + commandName + " 1");
        }
    }

    /**
     * Returns the first word, in lower case.
     * {@code "Todo read book"} becomes {@code "todo"}.
     */
    private static String firstWord(String input) {
        int space = input.indexOf(' ');
        String word = space == -1 ? input : input.substring(0, space);
        return word.toLowerCase();
    }

    /** Returns the text after the command word, with surrounding spaces removed. */
    private static String argumentsOf(String input) {
        int space = input.indexOf(' ');
        if (space == -1) {
            return "";
        }
        return input.substring(space + 1).trim();
    }
}

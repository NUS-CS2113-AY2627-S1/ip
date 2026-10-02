package foodielover.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import foodielover.FoodieloverException;
import foodielover.command.AddCommand;
import foodielover.command.Command;
import foodielover.command.DateFilterCommand;
import foodielover.command.DeleteCommand;
import foodielover.command.ExitCommand;
import foodielover.command.FindCommand;
import foodielover.command.ListCommand;
import foodielover.command.MarkCommand;
import foodielover.command.UnmarkCommand;
import foodielover.task.Deadline;
import foodielover.task.Event;
import foodielover.task.Todo;

/**
 * Parses user input into executable Command objects.
 */
public class Parser {
    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses the user's raw input string and returns the corresponding Command.
     *
     * @param input Raw command line entered by the user.
     * @return Concrete Command corresponding to user input.
     * @throws FoodieloverException If command syntax is invalid or unrecognized.
     */
    public static Command parse(String input) throws FoodieloverException {
        if (input == null) {
            throw new FoodieloverException("Please enter a command.");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new FoodieloverException("Please enter a command.");
        }

        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseTaskIndex(arguments, "mark"));
        case "unmark":
            return new UnmarkCommand(parseTaskIndex(arguments, "unmark"));
        case "delete":
            return new DeleteCommand(parseTaskIndex(arguments, "delete"));
        case "todo":
            return parseTodo(arguments);
        case "deadline":
            return parseDeadline(arguments);
        case "event":
            return parseEvent(arguments);
        case "find":
            return parseFind(arguments);
        case "date":
            // Fallthrough
        case "on":
            return parseDateFilter(arguments, commandWord);
        default:
            throw new FoodieloverException(
                    "This is not a valid input. Please try again with one of the following commands: "
                            + "todo, deadline, event, list, mark, unmark, delete, find, date, on, bye");
        }
    }

    /**
     * Parses a 1-based task number from the command arguments.
     *
     * @param arguments Argument string containing the task number.
     * @param command Command keyword used in the input.
     * @return Zero-based task index.
     * @throws FoodieloverException If the argument is missing or not a valid number.
     */
    private static int parseTaskIndex(String arguments, String command) throws FoodieloverException {
        if (arguments.isEmpty()) {
            throw new FoodieloverException("Please enter something after '" + command + "'.");
        }
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException exception) {
            throw new FoodieloverException("Please enter a number after '" + command + "'.");
        }
    }

    /**
     * Parses a todo command string into an AddCommand.
     *
     * @param arguments Argument string containing the todo description.
     * @return AddCommand wrapping the created Todo.
     * @throws FoodieloverException If description is empty or contains forbidden characters.
     */
    private static Command parseTodo(String arguments) throws FoodieloverException {
        if (arguments.isEmpty()) {
            throw new FoodieloverException("Please enter a description after 'todo'.");
        }
        if (arguments.contains("|")) {
            throw new FoodieloverException("Task description cannot contain the pipe character ('|').");
        }
        return new AddCommand(new Todo(arguments));
    }

    /**
     * Parses a deadline command string into an AddCommand.
     *
     * @param arguments Argument string containing description and '/by'.
     * @return AddCommand wrapping the created Deadline.
     * @throws FoodieloverException If '/by' is missing, fields are empty, or values are invalid.
     */
    private static Command parseDeadline(String arguments) throws FoodieloverException {
        int byIndex = arguments.indexOf("/by");
        if (byIndex < 0) {
            throw new FoodieloverException("Please include a deadline using '/by'.");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + 3).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new FoodieloverException(
                    "Please provide both a deadline description and a value after '/by'.");
        }
        if (description.contains("|") || by.contains("|")) {
            throw new FoodieloverException("Task description or deadline cannot contain the pipe character ('|').");
        }
        if (DateTimeParser.isDateLike(by) && DateTimeParser.parseDateTime(by) == null
                && DateTimeParser.parseDate(by) == null) {
            throw new FoodieloverException(
                    "Invalid date or time: '" + by + "'. Please provide a valid calendar date.");
        }
        return new AddCommand(new Deadline(description, by));
    }

    /**
     * Parses an event command string into an AddCommand.
     *
     * @param arguments Argument string containing description, '/from', and '/to'.
     * @return AddCommand wrapping the created Event.
     * @throws FoodieloverException If delimiters are missing, fields are empty, or times are invalid.
     */
    private static Command parseEvent(String arguments) throws FoodieloverException {
        int fromIndex = arguments.indexOf("/from");
        int toIndex = arguments.indexOf("/to");
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new FoodieloverException(
                    "Please include an event description, '/from' time, and '/to' time.");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + 5, toIndex).trim();
        String to = arguments.substring(toIndex + 3).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new FoodieloverException(
                    "Please provide an event description and values after '/from' and '/to'.");
        }
        if (description.contains("|") || from.contains("|") || to.contains("|")) {
            throw new FoodieloverException("Task description or event dates cannot contain the pipe character ('|').");
        }
        if (DateTimeParser.isDateLike(from) && DateTimeParser.parseDateTime(from) == null
                && DateTimeParser.parseDate(from) == null) {
            throw new FoodieloverException(
                    "Invalid start date or time: '" + from + "'. Please provide a valid calendar date.");
        }
        if (DateTimeParser.isDateLike(to) && DateTimeParser.parseDateTime(to) == null
                && DateTimeParser.parseDate(to) == null) {
            throw new FoodieloverException(
                    "Invalid end date or time: '" + to + "'. Please provide a valid calendar date.");
        }

        LocalDateTime fromDateTime = DateTimeParser.parseDateTime(from);
        LocalDateTime toDateTime = DateTimeParser.parseDateTime(to);
        LocalDate fromDate = DateTimeParser.parseDate(from);
        LocalDate toDate = DateTimeParser.parseDate(to);

        if (fromDateTime != null && toDateTime != null && toDateTime.isBefore(fromDateTime)) {
            throw new FoodieloverException("The event end time cannot be earlier than its start time.");
        } else if (fromDate != null && toDate != null && fromDateTime == null && toDateTime == null
                && toDate.isBefore(fromDate)) {
            throw new FoodieloverException("The event end date cannot be earlier than its start date.");
        }

        return new AddCommand(new Event(description, from, to));
    }

    /**
     * Parses a date filter command string into a DateFilterCommand.
     *
     * @param arguments Argument string containing the date.
     * @param command Command keyword used ('date' or 'on').
     * @return DateFilterCommand with the parsed target date.
     * @throws FoodieloverException If date argument is missing or in an invalid format.
     */
    private static Command parseDateFilter(String arguments, String command) throws FoodieloverException {
        if (arguments.isEmpty()) {
            throw new FoodieloverException(
                    "Please enter a date after '" + command + "' (e.g., 2019-10-15 or 2/12/2019).");
        }
        LocalDate date = DateTimeParser.parseDate(arguments);
        if (date == null) {
            throw new FoodieloverException(
                    "Please enter a valid date in the format yyyy-mm-dd or d/M/yyyy (e.g., 2019-10-15 or 2/12/2019).");
        }
        return new DateFilterCommand(date);
    }

    /**
     * Parses a find command string into a FindCommand.
     *
     * @param arguments Argument string containing the search keyword.
     * @return FindCommand with the parsed keyword.
     * @throws FoodieloverException If keyword is missing.
     */
    private static Command parseFind(String arguments) throws FoodieloverException {
        if (arguments.isEmpty()) {
            throw new FoodieloverException("Please enter a keyword after 'find'.");
        }
        return new FindCommand(arguments);
    }
}

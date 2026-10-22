package bryte;

import bryte.command.AddCommand;
import bryte.command.Command;
import bryte.command.DeleteCommand;
import bryte.command.ExitCommand;
import bryte.command.FindCommand;
import bryte.command.ListCommand;
import bryte.command.MarkCommand;
import bryte.command.ScheduleCommand;
import bryte.exception.BryteException;
import bryte.task.Deadline;
import bryte.task.Event;
import bryte.task.Todo;
import bryte.util.DateTimeUtil;

/**
 * Parses user input into executable commands.
 */
public class Parser {
    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_START_DELIMITER = " /from ";
    private static final String EVENT_END_DELIMITER = " /to ";

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses the full user command and returns the corresponding Command object.
     *
     * @param fullCommand The full user input string.
     * @return The Command to be executed.
     * @throws BryteException If the command is invalid or missing required parameters.
     */
    public static Command parse(String fullCommand) throws BryteException {
        String[] commandParts = fullCommand.trim().split(" ", 2);
        String keyword = commandParts[0].toLowerCase();

        switch (keyword) {
            case "list":
                return new ListCommand();
            case "mark":
                return prepareMarkCommand(commandParts, true);
            case "unmark":
                return prepareMarkCommand(commandParts, false);
            case "todo":
                return prepareTodoCommand(commandParts);
            case "deadline":
                return prepareDeadlineCommand(commandParts);
            case "event":
                return prepareEventCommand(commandParts);
            case "delete":
                return prepareDeleteCommand(commandParts);
            case "find":
                return prepareFindCommand(commandParts);
            case "schedule":
                return prepareScheduleCommand(commandParts);
            case "bye":
                return new ExitCommand();
            default:
                throw new BryteException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses the arguments for a mark or unmark command.
     *
     * @param commandParts The array containing the command keyword and arguments.
     * @param isDone True to mark the task as done, false to unmark.
     * @return A MarkCommand initialized with the specified task index and status.
     * @throws BryteException If the index is missing or cannot be parsed as an integer.
     */
    private static Command prepareMarkCommand(String[] commandParts, boolean isDone) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("Please specify a task number.");
        }
        try {
            int index = Integer.parseInt(commandParts[1].trim()) - 1;
            return new MarkCommand(index, isDone);
        } catch (NumberFormatException e) {
            throw new BryteException("Task number must be an integer.");
        }
    }

    /**
     * Parses the arguments for a delete command.
     *
     * @param commandParts The array containing the command keyword and arguments.
     * @return A DeleteCommand initialized with the specified task index.
     * @throws BryteException If the index is missing or cannot be parsed as an integer.
     */
    private static Command prepareDeleteCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("Please specify a task number to delete.");
        }
        try {
            int index = Integer.parseInt(commandParts[1].trim()) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new BryteException("Task number must be an integer.");
        }
    }

    /**
     * Parses the arguments for a todo command.
     *
     * @param commandParts The array containing the command keyword and description.
     * @return An AddCommand containing the new Todo task.
     * @throws BryteException If the description is empty.
     */
    private static Command prepareTodoCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("The description of a todo cannot be empty.");
        }
        return new AddCommand(new Todo(commandParts[1].trim()));
    }

    /**
     * Parses the arguments for a deadline command.
     *
     * @param commandParts The array containing the command keyword and arguments.
     * @return An AddCommand containing the new Deadline task.
     * @throws BryteException If the description or deadline date is missing or invalid.
     */
    private static Command prepareDeadlineCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("The description of a deadline cannot be empty.");
        }
        String[] deadlineParts = commandParts[1].split(DEADLINE_DELIMITER);
        if (deadlineParts.length < 2) {
            throw new BryteException("Please use " + DEADLINE_DELIMITER.trim() + " to specify the deadline.");
        }
        java.time.LocalDateTime dt = DateTimeUtil.parse(deadlineParts[1].trim());
        return new AddCommand(new Deadline(deadlineParts[0].trim(), dt));
    }

    /**
     * Parses the arguments for an event command.
     *
     * @param commandParts The array containing the command keyword and arguments.
     * @return An AddCommand containing the new Event task.
     * @throws BryteException If the description, start time, or end time is missing or invalid.
     */
    private static Command prepareEventCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("The description of an event cannot be empty.");
        }
        String[] eventParts = commandParts[1].split(EVENT_START_DELIMITER);
        if (eventParts.length < 2) {
            throw new BryteException("Please use " + EVENT_START_DELIMITER.trim()
                    + " and " + EVENT_END_DELIMITER.trim() + " to specify the event duration.");
        }
        String[] timeParts = eventParts[1].split(EVENT_END_DELIMITER);
        if (timeParts.length < 2) {
            throw new BryteException("Please use " + EVENT_END_DELIMITER.trim()
                    + " to specify the end time of the event.");
        }
        java.time.LocalDateTime startDt = DateTimeUtil.parse(timeParts[0].trim());
        java.time.LocalDateTime endDt = DateTimeUtil.parse(timeParts[1].trim());
        return new AddCommand(new Event(eventParts[0].trim(), startDt, endDt));
    }

    /**
     * Parses the arguments for a find command.
     *
     * @param commandParts The array containing the command keyword and search keyword.
     * @return A FindCommand initialized with the search keyword.
     * @throws BryteException If the search keyword is empty.
     */
    private static Command prepareFindCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("Please specify a keyword to search for (e.g., find book).");
        }
        return new FindCommand(commandParts[1].trim());
    }

    /**
     * Parses the arguments for a schedule command.
     *
     * @param commandParts The array containing the command keyword and date.
     * @return A ScheduleCommand initialized with the target date.
     * @throws BryteException If the date argument is missing or cannot be parsed.
     */
    private static Command prepareScheduleCommand(String[] commandParts) throws BryteException {
        if (commandParts.length < 2 || commandParts[1].trim().isEmpty()) {
            throw new BryteException("Please specify a date to check the schedule (e.g., schedule 2019-10-15).");
        }
        java.time.LocalDateTime dt = DateTimeUtil.parse(commandParts[1].trim());
        return new ScheduleCommand(dt.toLocalDate());
    }
}

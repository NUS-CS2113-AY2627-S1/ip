package isa.parser;

import isa.command.AddCommand;
import isa.command.Command;
import isa.command.DeleteCommand;
import isa.command.ExitCommand;
import isa.command.FindCommand;
import isa.command.ListCommand;
import isa.command.MarkCommand;
import isa.command.OnDateCommand;
import isa.exception.IsaException;
import isa.task.Deadline;
import isa.task.Event;
import isa.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Identifies commands and extracts their task arguments.
 */
public class Parser {
    private static final String COMMAND_MARK = "mark ";
    private static final String COMMAND_UNMARK = "unmark ";
    private static final String COMMAND_DEADLINE = "deadline ";
    private static final String COMMAND_EVENT = "event ";
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    /**
     * Parses a user's input into an executable command.
     *
     * @param command Full command entered by the user.
     * @return Command that can execute the requested action.
     * @throws IsaException If the command is unknown or a required argument is invalid.
     */
    public Command parse(String command) throws IsaException {
        if (command.equals("bye")) {
            return new ExitCommand();
        } else if (command.equals("list")) {
            return new ListCommand();
        } else if (command.startsWith(COMMAND_MARK)) {
            return new MarkCommand(parseMarkIndex(command), true);
        } else if (command.startsWith(COMMAND_UNMARK)) {
            return new MarkCommand(parseUnmarkIndex(command), false);
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            return new AddCommand(parseTodo(command));
        } else if (command.startsWith(COMMAND_DEADLINE)) {
            return new AddCommand(parseDeadline(command));
        } else if (command.startsWith(COMMAND_EVENT)) {
            return new AddCommand(parseEvent(command));
        } else if (command.equals("delete") || command.startsWith("delete ")) {
            return new DeleteCommand(parseDeleteIndex(command));
        } else if (command.equals("on") || command.startsWith("on ")) {
            return parseOnDate(command);
        } else if (command.equals("find") || command.startsWith("find ")) {
            return parseFind(command);
        } else {
            throw new IsaException("i don't understand :((");
        }
    }

    /**
     * Parses the task number in a mark command as a zero-based index.
     *
     * @param command Mark command entered by the user.
     * @return Zero-based task index.
     */
    public int parseMarkIndex(String command) {
        return Integer.parseInt(command.substring(COMMAND_MARK.length())) - 1;
    }

    /**
     * Parses the task number in an unmark command as a zero-based index.
     *
     * @param command Unmark command entered by the user.
     * @return Zero-based task index.
     */
    public int parseUnmarkIndex(String command) {
        return Integer.parseInt(command.substring(COMMAND_UNMARK.length())) - 1;
    }

    /**
     * Parses a todo command into a task.
     *
     * @param command Todo command entered by the user.
     * @return Parsed todo task.
     * @throws IsaException If the description is empty.
     */
    public Todo parseTodo(String command) throws IsaException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new IsaException("please enter a todo!");
        }

        return new Todo(description);
    }

    /**
     * Parses a deadline command into a task.
     *
     * @param command Deadline command entered by the user.
     * @return Parsed deadline task.
     * @throws IsaException If the description or date is missing or the date is invalid.
     */
    public Deadline parseDeadline(String command) throws IsaException {
        String details = command.substring(COMMAND_DEADLINE.length());
        String[] parts = details.split(DEADLINE_SEPARATOR, 2);

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IsaException("use deadline DESCRIPTION /by yyyy-MM-dd");
        }

        try {
            LocalDate dueDate = LocalDate.parse(parts[1].trim());
            return new Deadline(parts[0], dueDate);
        } catch (DateTimeParseException e) {
            throw new IsaException("enter a valid deadline date in yyyy-MM-dd format");
        }
    }

    /**
     * Parses an event command into a task.
     *
     * @param command Event command entered by the user.
     * @return Parsed event task.
     * @throws IsaException If the event times are missing, malformed, or out of order.
     */
    public Event parseEvent(String command) throws IsaException {
        String details = command.substring(COMMAND_EVENT.length());
        int fromPosition = details.indexOf(EVENT_FROM_SEPARATOR);
        int toPosition = details.indexOf(EVENT_TO_SEPARATOR);

        if (fromPosition < 0 || details.substring(0, fromPosition).isBlank()
                || toPosition <= fromPosition + EVENT_FROM_SEPARATOR.length()) {
            throw new IsaException("use event DESCRIPTION /from yyyy-MM-ddTHH:mm /to yyyy-MM-ddTHH:mm");
        }

        String description = details.substring(0, fromPosition);
        String startText = details.substring(
                fromPosition + EVENT_FROM_SEPARATOR.length(), toPosition).trim();
        String endText = details.substring(toPosition + EVENT_TO_SEPARATOR.length()).trim();

        if (startText.isEmpty() || endText.isEmpty()) {
            throw new IsaException("use event DESCRIPTION /from yyyy-MM-ddTHH:mm /to yyyy-MM-ddTHH:mm");
        }

        try {
            LocalDateTime startTime = Event.parseTime(startText);
            LocalDateTime endTime = Event.parseTime(endText);
            return new Event(description, startTime, endTime);
        } catch (DateTimeParseException e) {
            throw new IsaException("enter event times in yyyy-MM-ddTHH:mm format");
        } catch (IllegalArgumentException e) {
            throw new IsaException(e.getMessage());
        }
    }

    /**
     * Parses the date whose deadlines and events should be shown.
     *
     * @param command On-date command entered by the user.
     * @return Command that lists tasks on the date.
     * @throws IsaException If the date is missing or invalid.
     */
    public OnDateCommand parseOnDate(String command) throws IsaException {
        String dateText = command.substring("on".length()).trim();

        try {
            return new OnDateCommand(LocalDate.parse(dateText));
        } catch (DateTimeParseException e) {
            throw new IsaException("enter a valid date in yyyy-MM-dd format");
        }
    }

    /**
     * Parses a keyword search command.
     *
     * @param command Find command entered by the user.
     * @return Command that lists matching tasks.
     * @throws IsaException If no keyword was provided.
     */
    public FindCommand parseFind(String command) throws IsaException {
        String keyword = command.substring("find".length()).trim();

        if (keyword.isEmpty()) {
            throw new IsaException("please enter a keyword to find!");
        }

        return new FindCommand(keyword);
    }

    /**
     * Parses the task number in a delete command.
     *
     * @param command Delete command entered by the user.
     * @return Zero-based task index.
     * @throws IsaException If the task number is missing or invalid.
     */
    public int parseDeleteIndex(String command) throws IsaException {
        String taskNumber = command.substring("delete".length()).trim();

        if (taskNumber.isEmpty()) {
            throw new IsaException("please enter the number of the task to delete!");
        }

        int taskIndex;

        try {
            taskIndex = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new IsaException("please enter a valid task number!");
        }

        return taskIndex;
    }
}

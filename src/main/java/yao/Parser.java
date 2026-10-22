package yao;

import java.time.LocalDate;
import yao.command.AddCommand;
import yao.command.Command;
import yao.command.DateFilterCommand;
import yao.command.DeleteCommand;
import yao.command.ExitCommand;
import yao.command.FindCommand;
import yao.command.ListCommand;
import yao.command.MarkCommand;
import yao.command.UnmarkCommand;
import yao.task.Deadline;
import yao.task.Event;
import yao.task.Todo;

/**
 * Parses user input strings into executable Command objects.
 */
public class Parser {

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses a raw user input command line into a corresponding Command object.
     *
     * @param fullCommand The raw command string entered by the user.
     * @return The executable Command instance.
     * @throws YaoException If the command format is invalid or unrecognized.
     */
    public static Command parse(String fullCommand) throws YaoException {
        if (fullCommand.equals("bye")) {
            return new ExitCommand();
        } else if (fullCommand.equals("list")) {
            return new ListCommand();
        } else if (fullCommand.equals("mark") || fullCommand.startsWith("mark ")) {
            return parseMarkCommand(fullCommand);
        } else if (fullCommand.equals("unmark") || fullCommand.startsWith("unmark ")) {
            return parseUnmarkCommand(fullCommand);
        } else if (fullCommand.equals("delete") || fullCommand.startsWith("delete ")) {
            return parseDeleteCommand(fullCommand);
        } else if (fullCommand.equals("find") || fullCommand.startsWith("find ")) {
            return parseFindCommand(fullCommand);
        } else if (fullCommand.equals("on") || fullCommand.startsWith("on ")) {
            return parseDateFilterCommand(fullCommand, "on");
        } else if (fullCommand.equals("date") || fullCommand.startsWith("date ")) {
            return parseDateFilterCommand(fullCommand, "date");
        } else if (fullCommand.equals("todo") || fullCommand.startsWith("todo ")) {
            return parseTodoCommand(fullCommand);
        } else if (fullCommand.equals("deadline") || fullCommand.startsWith("deadline ")) {
            return parseDeadlineCommand(fullCommand);
        } else if (fullCommand.equals("event") || fullCommand.startsWith("event ")) {
            return parseEventCommand(fullCommand);
        } else {
            throw new YaoException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses a find command string into a FindCommand.
     *
     * @param fullCommand The raw command string.
     * @return A FindCommand with the extracted search keyword.
     * @throws YaoException If the keyword is missing or empty.
     */
    private static Command parseFindCommand(String fullCommand) throws YaoException {
        String keyword = fullCommand.substring(4).trim();
        if (keyword.isEmpty()) {
            throw new YaoException("OOPS!!! The search keyword cannot be empty.");
        }
        return new FindCommand(keyword);
    }

    /**
     * Parses a date query command string into a DateFilterCommand.
     *
     * @param fullCommand The raw command string.
     * @param prefix The command prefix used ("on" or "date").
     * @return A DateFilterCommand targeting the parsed date.
     * @throws YaoException If the date argument is missing or invalid.
     */
    private static Command parseDateFilterCommand(String fullCommand, String prefix) throws YaoException {
        String dateStr = fullCommand.substring(prefix.length()).trim();
        if (dateStr.isEmpty()) {
            throw new YaoException("OOPS!!! Please specify a date to search for (e.g. on 2019-10-15).");
        }
        LocalDate date = DateTimeUtil.parseDate(dateStr);
        return new DateFilterCommand(date);
    }

    /**
     * Parses a mark command string into a MarkCommand.
     *
     * @param fullCommand The raw command string.
     * @return A MarkCommand targeting the specified task index.
     * @throws YaoException If the index argument is missing or not a valid number.
     */
    private static Command parseMarkCommand(String fullCommand) throws YaoException {
        String arg = fullCommand.substring(4).trim();
        if (arg.isEmpty()) {
            throw new YaoException("OOPS!!! Please specify which task number to mark.");
        }
        try {
            int taskIndex = Integer.parseInt(arg) - 1;
            return new MarkCommand(taskIndex);
        } catch (NumberFormatException e) {
            throw new YaoException("OOPS!!! Task index must be a valid number.");
        }
    }

    /**
     * Parses an unmark command string into an UnmarkCommand.
     *
     * @param fullCommand The raw command string.
     * @return An UnmarkCommand targeting the specified task index.
     * @throws YaoException If the index argument is missing or not a valid number.
     */
    private static Command parseUnmarkCommand(String fullCommand) throws YaoException {
        String arg = fullCommand.substring(6).trim();
        if (arg.isEmpty()) {
            throw new YaoException("OOPS!!! Please specify which task number to unmark.");
        }
        try {
            int taskIndex = Integer.parseInt(arg) - 1;
            return new UnmarkCommand(taskIndex);
        } catch (NumberFormatException e) {
            throw new YaoException("OOPS!!! Task index must be a valid number.");
        }
    }

    /**
     * Parses a delete command string into a DeleteCommand.
     *
     * @param fullCommand The raw command string.
     * @return A DeleteCommand targeting the specified task index.
     * @throws YaoException If the index argument is missing or not a valid number.
     */
    private static Command parseDeleteCommand(String fullCommand) throws YaoException {
        String arg = fullCommand.substring(6).trim();
        if (arg.isEmpty()) {
            throw new YaoException("OOPS!!! Please specify which task number to delete.");
        }
        try {
            int taskIndex = Integer.parseInt(arg) - 1;
            return new DeleteCommand(taskIndex);
        } catch (NumberFormatException e) {
            throw new YaoException("OOPS!!! Task index must be a valid number.");
        }
    }

    /**
     * Parses a todo command string into an AddCommand.
     *
     * @param fullCommand The raw command string.
     * @return An AddCommand containing the new Todo task.
     * @throws YaoException If the task description is empty.
     */
    private static Command parseTodoCommand(String fullCommand) throws YaoException {
        String description = fullCommand.substring(4).trim();
        if (description.isEmpty()) {
            throw new YaoException("OOPS!!! The description of a todo cannot be empty.");
        }
        return new AddCommand(new Todo(description));
    }

    /**
     * Parses a deadline command string into an AddCommand.
     *
     * @param fullCommand The raw command string.
     * @return An AddCommand containing the new Deadline task.
     * @throws YaoException If the description or deadline limit is missing.
     */
    private static Command parseDeadlineCommand(String fullCommand) throws YaoException {
        String details = fullCommand.substring(8).trim();
        if (details.isEmpty()) {
            throw new YaoException("OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] parts = details.split(" /by ", 2);
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new YaoException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new YaoException("OOPS!!! The deadline must specify a /by time.");
        }
        String by = parts[1].trim();
        return new AddCommand(new Deadline(description, by));
    }

    /**
     * Parses an event command string into an AddCommand.
     *
     * @param fullCommand The raw command string.
     * @return An AddCommand containing the new Event task.
     * @throws YaoException If the description, start time, or end time is missing.
     */
    private static Command parseEventCommand(String fullCommand) throws YaoException {
        String details = fullCommand.substring(5).trim();
        if (details.isEmpty()) {
            throw new YaoException("OOPS!!! The description of an event cannot be empty.");
        }
        String[] parts = details.split(" /from ", 2);
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new YaoException("OOPS!!! The description of an event cannot be empty.");
        }
        if (parts.length < 2) {
            throw new YaoException("OOPS!!! An event must specify both /from and /to times.");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        String from = timeParts[0].trim();
        if (from.isEmpty() || timeParts.length < 2 || timeParts[1].trim().isEmpty()) {
            throw new YaoException("OOPS!!! An event must specify both /from and /to times.");
        }
        String to = timeParts[1].trim();
        return new AddCommand(new Event(description, from, to));
    }
}

package aglio.parser;

import aglio.command.AddCommand;
import aglio.command.Command;
import aglio.command.DeleteCommand;
import aglio.command.ExitCommand;
import aglio.command.FindCommand;
import aglio.command.ListCommand;
import aglio.command.MarkCommand;
import aglio.command.UnmarkCommand;
import aglio.exception.AglioException;
import aglio.task.Deadline;
import aglio.task.Event;
import aglio.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Deals with making sense of user commands.
 * Parses the raw command string and returns the corresponding
 * {@link Command} object, ready to be executed.
 */
public class Parser {

    /**
     * Parses a raw command string and returns the corresponding Command.
     *
     * @param fullCommand the raw command string entered by the user
     * @return the parsed Command object
     * @throws AglioException if the command is invalid or malformed
     */
    public static Command parse(String fullCommand) throws AglioException {
        if (fullCommand.equals("bye")) {
            return new ExitCommand();
        } else if (fullCommand.equals("list")) {
            return new ListCommand();
        } else if (fullCommand.startsWith("mark ")) {
            int index = parseTaskIndex(fullCommand.substring(5));
            return new MarkCommand(index);
        } else if (fullCommand.startsWith("unmark ")) {
            int index = parseTaskIndex(fullCommand.substring(7));
            return new UnmarkCommand(index);
        } else if (fullCommand.startsWith("delete ")) {
            int index = parseTaskIndex(fullCommand.substring(7));
            return new DeleteCommand(index);
        } else if (fullCommand.equals("todo")
                || (fullCommand.startsWith("todo ") && fullCommand.substring(5).trim().isEmpty())) {
            throw new AglioException("The description of a todo cannot be empty.");
        } else if (fullCommand.startsWith("todo ")) {
            String description = fullCommand.substring(5);
            return new AddCommand(new Todo(description));
        } else if (fullCommand.equals("deadline")
                || (fullCommand.startsWith("deadline ")
                        && fullCommand.substring(9).trim().isEmpty())) {
            throw new AglioException("The description of a deadline cannot be empty.");
        } else if (fullCommand.startsWith("deadline ")) {
            String rest = fullCommand.substring(9);
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new AglioException("A deadline requires a /by clause.\n"
                        + " Usage: deadline <description> /by <date>");
            }
            LocalDate date;
            try {
                date = LocalDate.parse(parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new AglioException(
                        "Invalid date format. Please use yyyy-MM-dd (e.g. 2019-10-15).");
            }
            return new AddCommand(new Deadline(parts[0], date));
        } else if (fullCommand.equals("event")
                || (fullCommand.startsWith("event ")
                        && fullCommand.substring(6).trim().isEmpty())) {
            throw new AglioException("The description of an event cannot be empty.");
        } else if (fullCommand.startsWith("event ")) {
            String rest = fullCommand.substring(6);
            String[] parts = rest.split(" /from ", 2);
            if (parts.length < 2) {
                throw new AglioException("An event requires /from and /to clauses.\n"
                        + " Usage: event <description> /from <start> /to <end>");
            }
            String[] timeParts = parts[1].split(" /to ", 2);
            if (timeParts.length < 2) {
                throw new AglioException("An event requires a /to clause.\n"
                        + " Usage: event <description> /from <start> /to <end>");
            }
            return new AddCommand(new Event(parts[0], timeParts[0], timeParts[1]));
        } else if (fullCommand.equals("find")
                || (fullCommand.startsWith("find ") && fullCommand.substring(5).trim().isEmpty())) {
            throw new AglioException("The keyword for find cannot be empty.");
        } else if (fullCommand.startsWith("find ")) {
            String keyword = fullCommand.substring(5);
            return new FindCommand(keyword);
        } else {
            throw new AglioException(
                    "I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses a task index string and validates that it is a valid number.
     * Bounds checking against the task list happens at execution time
     * inside the Command's execute() method.
     *
     * @param input the raw string after the command keyword
     * @return the zero-based index
     * @throws AglioException if the input is not a valid number
     */
    private static int parseTaskIndex(String input) throws AglioException {
        try {
            return Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            throw new AglioException(
                    "Please provide a valid task number. Usage: mark <task number>");
        }
    }
}

package zack;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import zack.command.AddCommand;
import zack.command.Command;
import zack.command.DeleteCommand;
import zack.command.DueCommand;
import zack.command.ExitCommand;
import zack.command.FindCommand;
import zack.command.ListCommand;
import zack.command.MarkCommand;
import zack.task.Deadline;
import zack.task.Event;
import zack.task.Task;
import zack.task.Todo;

/** Validates user input and converts it into executable commands. */
public class Parser {
    /**
     * Converts trimmed user input into an executable command.
     *
     * @param command command text with surrounding whitespace removed
     * @param taskCount current number of tasks, used to validate task numbers
     * @return parsed command
     * @throws ZackException if the command or its arguments are invalid
     */
    public static Command parse(String command, int taskCount) throws ZackException {
        if (command.equals("find") || command.startsWith("find ")) {
            String keyword = command.substring(4).trim();
            if (keyword.isEmpty()) {
                throw new ZackException("Please provide a keyword, for example: find book.");
            }
            return new FindCommand(keyword);
        }
        if (command.equals("due") || command.startsWith("due ")) {
            try {
                LocalDate date = LocalDate.parse(command.substring(3).trim());
                return new DueCommand(date);
            } catch (DateTimeParseException e) {
                throw new ZackException("Use: due yyyy-MM-dd, for example: due 2026-10-02.");
            }
        }
        if (command.equals("todo")) {
            throw new ZackException("Please describe the todo, for example: todo read book.");
        }
        if (command.equals("delete")) {
            throw new ZackException("Please provide a task number, for example: delete 1.");
        }
        if (command.equals("list")) {
            return new ListCommand();
        } else if (command.equals("bye")) {
            return new ExitCommand();
        } else if (command.startsWith("delete ")) {
            return new DeleteCommand(parseTaskIndex(command, taskCount));
        } else if (command.startsWith("mark ")) {
            return new MarkCommand(parseTaskIndex(command, taskCount), true);
        } else if (command.startsWith("unmark ")) {
            return new MarkCommand(parseTaskIndex(command, taskCount), false);
        } else if (command.startsWith("event ")) {
            return new AddCommand(parseEvent(command));
        } else if (command.startsWith("deadline ")) {
            return new AddCommand(parseDeadline(command));
        } else if (command.startsWith("todo ")) {
            return new AddCommand(parseTodo(command));
        } else {
            throw new ZackException(
                    "Unknown command. Use todo, deadline, event, list, find, due, mark, unmark, delete, or bye.");
        }
    }

    /**
     * Converts a one-based task number into a validated zero-based index.
     *
     * @param command command text containing a task number
     * @param taskCount current number of tasks
     * @return zero-based task index
     * @throws ZackException if the number is not an integer or is outside the list
     */
    public static int parseTaskIndex(String command, int taskCount) throws ZackException {
        String number = command.substring(command.indexOf(' ') + 1).trim();
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new ZackException("Please enter a whole task number, for example: mark 1.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new ZackException("There is no task with that number. Use list to check.");
        }
        return taskNumber - 1;
    }

    /**
     * Parses an event description and its textual start and end values.
     *
     * @param command command text beginning with the event keyword and a space
     * @return newly created event
     * @throws ZackException if required delimiters or values are missing
     */
    public static Task parseEvent(String command) throws ZackException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex < "event ".length()
                || toIndex < fromIndex + " /from ".length()) {
            throw new ZackException("Use: event DESCRIPTION /from START /to END.");
        }

        String description = command.substring("event ".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new ZackException("An event needs a description, a start time, and an end time.");
        }

        Task event = new Event(description, from, to);
        return event;
    }

    /**
     * Parses a task description and a valid ISO deadline date.
     *
     * @param command command text beginning with the deadline keyword and a space
     * @return newly created deadline task
     * @throws ZackException if the description, delimiter, or date is invalid
     */
    public static Task parseDeadline(String command) throws ZackException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex < "deadline ".length()) {
            throw new ZackException("Use: deadline DESCRIPTION /by TIME.");
        }

        String description = command.substring("deadline ".length(), byIndex).trim();
        String by = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new ZackException("A deadline needs both a description and a time.");
        }

        try {
            return new Deadline(description, by);
        } catch (IllegalArgumentException e) {
            throw new ZackException(e.getMessage());
        }
    }

    /**
     * Parses the description of a task without a time constraint.
     *
     * @param command command text beginning with the todo keyword and a space
     * @return newly created todo task
     * @throws ZackException if the description is empty
     */
    public static Task parseTodo(String command) throws ZackException {
        String description = command.substring("todo ".length()).trim();
        if (description.isEmpty()) {
            throw new ZackException("Please describe the todo, for example: todo read book.");
        }

        Task todo = new Todo(description);
        return todo;
    }
}

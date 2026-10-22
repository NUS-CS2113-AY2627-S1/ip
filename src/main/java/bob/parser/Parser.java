package bob.parser;

import bob.command.AddCommand;
import bob.command.Command;
import bob.command.DeleteCommand;
import bob.command.EmptyCommand;
import bob.command.ExitCommand;
import bob.command.FindCommand;
import bob.command.ListCommand;
import bob.command.MarkCommand;
import bob.command.UnmarkCommand;
import bob.exception.EmptyError;
import bob.exception.InvalidTaskNumberError;
import bob.exception.SyntaxError;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

/**
 * Interprets user commands without changing tasks or performing input and output.
 */
public final class Parser {
    /**
     * Prevents instantiation of this stateless utility class.
     */
    private Parser() {
    }

    /**
     * Parses user input into an executable command using the existing syntax rules.
     * Addition syntax is validated during execution, after checking list capacity.
     * Blank input produces a command that does nothing.
     *
     * @param fullCommand user input, which may contain surrounding whitespace.
     * @return command to execute.
     * @throws InvalidTaskNumberError if a mark, unmark, or delete argument is not an integer.
     * @throws SyntaxError if a find command has no keyword.
     */
    public static Command parse(String fullCommand) throws InvalidTaskNumberError, SyntaxError {
        String input = fullCommand.trim();
        if (input.equalsIgnoreCase("bye")) {
            return new ExitCommand();
        } else if (input.equalsIgnoreCase("list")) {
            return new ListCommand();
        } else if (input.equals("find") || input.startsWith("find ")) {
            String keyword = input.substring(4).trim();
            if (keyword.isEmpty()) {
                throw new SyntaxError();
            }
            return new FindCommand(keyword);
        } else if (input.startsWith("mark ")) {
            return new MarkCommand(parseTaskIndex(input));
        } else if (input.startsWith("unmark ")) {
            return new UnmarkCommand(parseTaskIndex(input));
        } else if (input.startsWith("delete ")) {
            return new DeleteCommand(parseTaskIndex(input));
        } else if (input.isEmpty()) {
            return new EmptyCommand();
        } else {
            return new AddCommand(input);
        }
    }

    /**
     * Converts the number in a mark, unmark, or delete command to a zero-based index.
     * List bounds are checked separately so deletion keeps its custom exception.
     *
     * @param input recognized command followed by a space and task number.
     * @return zero-based index, which may be outside the task list.
     * @throws InvalidTaskNumberError if the argument is not an integer.
     */
    private static int parseTaskIndex(String input) throws InvalidTaskNumberError {
        try {
            return Integer.parseInt(input.substring(input.indexOf(' ') + 1).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberError(e);
        }
    }

    /**
     * Parses an addition command into a task using the existing syntax rules.
     *
     * @param input command with surrounding whitespace already removed.
     * @return task described by the command.
     * @throws SyntaxError if the command or its arguments are not recognized.
     * @throws EmptyError if a required task description is empty.
     */
    public static Task parseTask(String input) throws SyntaxError, EmptyError {
        if (input.startsWith("deadline ")) {
            String[] parts = input.substring(9).split(" /by ", 2);
            if (parts[0].trim().isEmpty() || parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SyntaxError();
            }
            return new Deadline(parts[0].trim(), parts[1].trim());
        }
        if (input.startsWith("event ")) {
            String eventInput = input.substring(6).trim();
            if (eventInput.startsWith("/from") || eventInput.startsWith("/to")) {
                throw new SyntaxError();
            }
            String[] parts = eventInput.split(" /from ", 2);
            if (parts.length == 2) {
                String[] eventDetails = parts[1].split(" /to ", 2);

                if (eventDetails.length == 2) {
                    if (parts[0].trim().isEmpty() || eventDetails[0].trim().isEmpty()
                            || eventDetails[1].trim().isEmpty()) {
                        throw new SyntaxError();
                    }
                    return new Event(parts[0].trim(), eventDetails[0].trim(), eventDetails[1].trim());
                }

                if (parts[0].trim().isEmpty() || eventDetails[0].trim().isEmpty()) {
                    throw new SyntaxError();
                }
                return new Event(parts[0].trim(), eventDetails[0].trim(), "");
            }
            if (eventInput.isEmpty()) {
                throw new EmptyError();
            }
            return new Event(eventInput, "", "");
        }
        if (input.equals("todo") || input.startsWith("todo ")) {
            String task = input.length() > 4 ? input.substring(4).trim() : "";
            if (task.isEmpty()) {
                throw new EmptyError();
            } else {
                return new Todo(task);
            }
        } else {
            throw new SyntaxError();
        }
    }
}

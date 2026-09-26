package parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import commands.AddCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.ExitCommand;
import commands.FindCommand;
import commands.ListCommand;
import commands.MarkCommand;
import commands.OnCommand;
import commands.UnmarkCommand;
import exception.SamSquareException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/**
 * Interprets one console command without changing the task list or performing I/O.
 */
public class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String ON_COMMAND = "on";
    private static final String LIST_COMMAND = "list";
    private static final String BYE_COMMAND = "bye";

    private final String fullCommand;
    private final String command;

    /**
     * Recognizes a command after removing surrounding whitespace.
     * Spaces within descriptions and other arguments are preserved.
     *
     * @param fullCommand Complete line entered by the user.
     * @throws SamSquareException If the input is empty or the command is unknown.
     */
    public Parser(String fullCommand) throws SamSquareException {
        this.fullCommand = fullCommand.trim();
        if (this.fullCommand.isEmpty()) {
            throw new SamSquareException("Please don't leave your input empty D:");
        }

        int separatorIndex = this.fullCommand.indexOf(' ');
        command = separatorIndex == -1 ? this.fullCommand : this.fullCommand.substring(0, separatorIndex);

        // No-argument commands still reject additional words after normalization.
        boolean isKnownCommand = switch (command) {
            case TODO_COMMAND, DEADLINE_COMMAND, EVENT_COMMAND, MARK_COMMAND,
                    UNMARK_COMMAND, DELETE_COMMAND, FIND_COMMAND, ON_COMMAND -> true;
            case LIST_COMMAND, BYE_COMMAND -> this.fullCommand.equals(command);
            default -> false;
        };
        if (!isKnownCommand) {
            throw new SamSquareException(
                    "Hold up... I don't recognise that command. "
                            + "Please use todo, deadline, event, mark, "
                            + "unmark, delete, find, on, list or bye."
            );
        }
    }

    /**
     * Creates an executable command after validating its text arguments.
     * Task numbers are checked against the current list when the command executes.
     *
     * @return The command described by the user's input.
     * @throws SamSquareException If a required argument is missing or malformed.
     */
    public Command parseCommand() throws SamSquareException {
        return switch (command) {
            case LIST_COMMAND -> new ListCommand();
            case BYE_COMMAND -> new ExitCommand();
            case TODO_COMMAND, DEADLINE_COMMAND, EVENT_COMMAND -> new AddCommand(parseTask());
            case DELETE_COMMAND -> new DeleteCommand(parseTaskNumber());
            case MARK_COMMAND -> new MarkCommand(parseTaskNumber());
            case UNMARK_COMMAND -> new UnmarkCommand(parseTaskNumber());
            case FIND_COMMAND -> parseFind();
            case ON_COMMAND -> parseOn();
            default -> throw new IllegalStateException("Parser contains an unsupported command.");
        };
    }

    /**
     * Creates a task from a todo, deadline, or event command.
     *
     * @return The task described by the command, ready to add to the list.
     * @throws SamSquareException If the task description or format is invalid.
     * @throws IllegalStateException If this command does not add a task.
     */
    private Task parseTask() throws SamSquareException {
        return switch (command) {
            case TODO_COMMAND -> parseTodo();
            case DEADLINE_COMMAND -> parseDeadline();
            case EVENT_COMMAND -> parseEvent();
            default -> throw new IllegalStateException("This command does not add a task.");
        };
    }

    /**
     * Parses a mark, unmark, or delete argument as a one-based task number.
     * The task list's owner checks whether that number exists.
     *
     * @return The requested task number.
     * @throws SamSquareException If the number is missing or is not an integer.
     * @throws IllegalStateException If this command does not use a task number.
     */
    private int parseTaskNumber() throws SamSquareException {
        if (!command.equals(MARK_COMMAND) && !command.equals(UNMARK_COMMAND) && !command.equals(DELETE_COMMAND)) {
            throw new IllegalStateException("This command does not use a task number.");
        }

        String numberText = getArguments();
        if (numberText.isEmpty()) {
            throw new SamSquareException("Specify the task number to " + command + " please!");
        }

        try {
            return Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            String message = command.equals(DELETE_COMMAND)
                    ? "HEY!! The task number must be a valid number!"
                    : "The task number must be a valid number.";
            throw new SamSquareException(message);
        }
    }

    /**
     * Removes the recognized command name and surrounding argument whitespace.
     *
     * @return The argument text, preserving internal spaces.
     */
    private String getArguments() {
        return fullCommand.substring(command.length()).trim();
    }

    /**
     * Rejects empty searches and preserves internal spaces in a search phrase.
     *
     * @return A command containing the non-empty search text.
     * @throws SamSquareException If the search text is empty.
     */
    private Command parseFind() throws SamSquareException {
        String keyword = getArguments();
        if (keyword.isEmpty()) {
            throw new SamSquareException("Please specify a keyword to find.");
        }
        return new FindCommand(keyword);
    }

    /**
     * Parses a calendar date for a deadline search.
     *
     * @return A command that lists deadlines due on the requested date.
     * @throws SamSquareException If the date is missing or is not a valid ISO date.
     */
    private Command parseOn() throws SamSquareException {
        String dateText = getArguments();
        if (dateText.isEmpty()) {
            throw new SamSquareException("Please specify a date: on yyyy-MM-dd.");
        }
        try {
            return new OnCommand(LocalDate.parse(dateText));
        } catch (DateTimeParseException e) {
            throw new SamSquareException("Use a valid date in yyyy-MM-dd format (e.g., 2019-10-15).");
        }
    }

    /**
     * Validates the description before creating a ToDo.
     *
     * @return The new incomplete ToDo.
     * @throws SamSquareException If the description is empty.
     */
    private Task parseTodo() throws SamSquareException {
        String description = getArguments();

        if (description.isEmpty()) {
            throw new SamSquareException(
                    "HEY!! The description of a todo cannot be empty!"
            );
        }

        return new Todo(description);
    }

    /**
     * Validates the deadline format before creating a task with its due date.
     *
     * @return The new deadline containing a calendar date.
     * @throws SamSquareException If the description, separator, or ISO date is invalid.
     */
    private Task parseDeadline() throws SamSquareException {
        String content = getArguments();
        String[] parts = content.split(" /by ", 2);

        if (parts.length < 2) {
            throw new SamSquareException(
                    "Hey... a deadline needs the format: deadline <task> /by <date>."
            );
        }

        String description = parts[0];
        String by = parts[1];

        if (description.isEmpty()) {
            throw new SamSquareException(
                    "HEY!! The description of a deadline cannot be empty!"
            );
        }

        if (by.isEmpty()) {
            throw new SamSquareException(
                    "Hey... deadline must have a due date!"
            );
        }

        try {
            return new Deadline(description, LocalDate.parse(by.trim()));
        } catch (DateTimeParseException e) {
            throw new SamSquareException("Use a valid deadline date in yyyy-MM-dd format (e.g., 2019-10-15).");
        }
    }

    /**
     * Validates the event format before creating a task with its time range.
     *
     * @return The new event with its start and end time text.
     * @throws SamSquareException If the description or required time components are missing.
     */
    private Task parseEvent() throws SamSquareException {
        String content = getArguments();

        String[] fromParts = content.split(" /from ", 2);

        if (fromParts.length < 2) {
            throw new SamSquareException(
                    "An event needs the format: "
                            + "event <task> /from <time> /to <time>."
            );
        }

        String description = fromParts[0].trim();

        String[] toParts = fromParts[1].split(" /to ", 2);

        if (toParts.length < 2) {
            throw new SamSquareException(
                    "An event needs both a starting and ending time."
            );
        }

        String from = toParts[0].trim();
        String to = toParts[1].trim();

        if (description.isEmpty()) {
            throw new SamSquareException(
                    "HEY!! The description of an event cannot be empty!"
            );
        }

        if (from.isEmpty()) {
            throw new SamSquareException(
                    "Broski.. an event must have a starting time!"
            );
        }

        if (to.isEmpty()) {
            throw new SamSquareException(
                    "Hello? Of course the event must have an ending time..."
            );
        }

        return new Event(description, from, to);
    }
}

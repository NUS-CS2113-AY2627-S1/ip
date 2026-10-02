package parser;

import command.AddCommand;
import command.Command;
import command.DeleteCommand;
import command.ExitCommand;
import command.ListCommand;
import command.MarkCommand;
import exception.AtlasException;
import task.Deadline;
import task.Event;
import task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles interpretation and validation of user input commands and arguments.
 */
public class Parser {
    private static final String BY_FLAG = " /by";
    private static final String FROM_FLAG = " /from";
    private static final String TO_FLAG = " /to";

    /**
     * Parses the full user input string into its corresponding executable Command.
     *
     * @param input Raw command string from the user.
     * @return The Command representing the parsed instruction.
     * @throws AtlasException If the command keyword is unrecognized or arguments are malformed.
     */
    public static Command parse(String input) throws AtlasException {
        String trimmed = input.trim();
        String[] parts = trimmed.split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseIndex(args), true);
        case "unmark":
            return new MarkCommand(parseIndex(args), false);
        case "delete":
            return new DeleteCommand(parseIndex(args));
        case "todo":
            return new AddCommand(createTodo(args));
        case "deadline":
            return new AddCommand(createDeadline(args));
        case "event":
            return new AddCommand(createEvent(args));
        case "find":
            return new command.FindCommand(args);
        default:
            throw new AtlasException("Unknown input, please try again");
        }
    }

    private static Todo createTodo(String args) throws AtlasException {
        if (args.isEmpty()) {
            throw new AtlasException("Invalid TODO: Input cannot be empty");
        }
        return new Todo(args);
    }

    private static Deadline createDeadline(String args) throws AtlasException {
        String[] parts = splitArguments(args, BY_FLAG,
                "Invalid DEADLINE: Input requires description and deadline date");
        LocalDate date = parseDate(parts[1].trim());
        return new Deadline(parts[0].trim(), date);
    }

    private static Event createEvent(String args) throws AtlasException {
        String[] fromParts = splitArguments(args, FROM_FLAG,
                "Invalid EVENT: Input requires description and start date");
        String description = fromParts[0].trim();

        String[] toParts = splitArguments(fromParts[1], TO_FLAG,
                "Invalid EVENT: Input requires start and end dates");
        LocalDate from = parseDate(toParts[0].trim());
        LocalDate to = parseDate(toParts[1].trim());

        return new Event(description, from, to);
    }

    private static LocalDate parseDate(String dateString) throws AtlasException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new AtlasException("Invalid date format: Please use yyyy-mm-dd (e.g., 2026-10-15)");
        }
    }

    private static String[] splitArguments(String text, String delimiter, String errorMessage)
            throws AtlasException {
        String[] parts = text.split(delimiter, 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new AtlasException(errorMessage);
        }
        return parts;
    }

    private static int parseIndex(String args) throws AtlasException {
        if (args.isEmpty()) {
            throw new AtlasException("Invalid input: Please specify a task number");
        }
        try {
            return Integer.parseInt(args.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new AtlasException("Invalid input: Task number must be a valid integer");
        }
    }
}
package chillguy.parser;

import chillguy.exception.ChillguyException;
import chillguy.parser.Command.Type;
import chillguy.task.Deadline;
import chillguy.task.Event;
import chillguy.task.Task;
import chillguy.task.Todo;

/**
 * Validates command syntax and constructs command data without changing the task list.
 */
public class Parser {
    // Match standalone separator tokens, including at either end to detect missing fields.
    private static final String BY_SEPARATOR = "(?<!\\S)/by(?=\\s|$)";
    private static final String FROM_SEPARATOR = "(?<!\\S)/from(?=\\s|$)";
    private static final String TO_SEPARATOR = "(?<!\\S)/to(?=\\s|$)";
    private static final String DEADLINE_FORMAT_MESSAGE =
            "Sorry, deadline tasks need this format: deadline DESCRIPTION /by DATE";
    private static final String EVENT_FORMAT_MESSAGE =
            "Sorry, event tasks need this format: event DESCRIPTION /from START /to END";

    /**
     * Parses a line into an operation and its arguments.
     * Task-number range checks belong to TaskList because they depend on the stored tasks.
     *
     * @throws ChillguyException If the command name, arguments, or task fields are invalid.
     */
    public Command parse(String input) throws ChillguyException {
        String command = input.strip();
        if (command.isEmpty()) {
            throw new ChillguyException("Please enter a command, such as todo DESCRIPTION or list.");
        }

        String[] commandParts = command.split("\\s+", 2);
        String commandName = commandParts[0];
        String arguments = commandParts.length == 2 ? commandParts[1].strip() : "";
        return switch (commandName) {
            case "bye", "list" -> {
                requireNoArguments(commandName, arguments);
                yield new Command(commandName.equals("bye") ? Type.EXIT : Type.LIST, null, 0, null);
            }
            case "find" -> {
                if (arguments.isEmpty()) {
                    throw new ChillguyException("Please include a search keyword. Use: find KEYWORD");
                }
                yield new Command(Type.FIND, null, 0, arguments);
            }
            case "mark" -> new Command(Type.MARK, null, parseTaskNumber(arguments, commandName), null);
            case "unmark" -> new Command(Type.UNMARK, null, parseTaskNumber(arguments, commandName), null);
            case "delete" -> new Command(Type.DELETE, null, parseTaskNumber(arguments, commandName), null);
            case "todo" -> new Command(Type.ADD, parseTodo(arguments), 0, null);
            case "deadline" -> new Command(Type.ADD, parseDeadline(arguments), 0, null);
            case "event" -> new Command(Type.ADD, parseEvent(arguments), 0, null);
            default -> throw new ChillguyException("ERROR: Unknown command.");
        };
    }

    /**
     * Rejects extra input for commands that take no arguments.
     *
     * @param commandName Command name to include in the usage hint.
     * @param arguments Stripped text following the command name.
     * @throws ChillguyException If any arguments are present.
     */
    private void requireNoArguments(String commandName, String arguments) throws ChillguyException {
        if (!arguments.isEmpty()) {
            throw new ChillguyException("The " + commandName + " command takes no extra input. Use: " + commandName);
        }
    }

    /**
     * Parses a task number, leaving range validation to the task list.
     *
     * @param arguments Text containing a single integer.
     * @param commandName Command name to include in the usage hint.
     * @return The supplied task number without converting it to a list index.
     * @throws ChillguyException If the argument is missing or cannot be parsed as an integer.
     */
    private int parseTaskNumber(String arguments, String commandName) throws ChillguyException {
        if (arguments.isEmpty()) {
            throw new ChillguyException("Please include a task number. Use: " + commandName + " INDEX");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException exception) {
            throw new ChillguyException("Please enter one whole-number task index. Use: " + commandName + " INDEX");
        }

        return taskNumber;
    }

    /**
     * Creates a todo from a nonblank description.
     *
     * @throws ChillguyException If the description is blank.
     */
    private Task parseTodo(String description) throws ChillguyException {
        if (description.isBlank()) {
            throw new ChillguyException("ERROR: Description of todo cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline from a description and a due date separated by {@code /by}.
     *
     * @throws ChillguyException If the separator or either required field is missing or invalid.
     */
    private Task parseDeadline(String details) throws ChillguyException {
        String[] parts = details.split(BY_SEPARATOR, -1);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new ChillguyException(DEADLINE_FORMAT_MESSAGE);
        }

        String description = parts[0].strip();
        String by = parts[1].strip();
        return new Deadline(description, by);
    }

    /**
     * Creates an event after checking the order of {@code /from} and {@code /to} and all required fields.
     *
     * @throws ChillguyException If separators are missing, repeated, or out of order, or a field is blank.
     */
    private Task parseEvent(String details) throws ChillguyException {
        // Keep empty fields so missing descriptions or times cannot become valid tasks.
        String[] fromParts = details.split(FROM_SEPARATOR, -1);
        String[] toParts = details.split(TO_SEPARATOR, -1);
        if (fromParts.length != 2 || toParts.length != 2
                || fromParts[0].split(TO_SEPARATOR, -1).length != 1) {
            throw new ChillguyException(EVENT_FORMAT_MESSAGE);
        }

        String description = fromParts[0].strip();
        String[] times = fromParts[1].split(TO_SEPARATOR, -1);
        if (description.isEmpty() || times[0].isBlank() || times[1].isBlank()) {
            throw new ChillguyException(EVENT_FORMAT_MESSAGE);
        }

        return new Event(description, times[0].strip(), times[1].strip());
    }
}

package jamal;

import jamal.command.AddCommand;
import jamal.command.Command;
import jamal.command.DeleteCommand;
import jamal.command.ExitCommand;
import jamal.command.FindCommand;
import jamal.command.ListCommand;
import jamal.command.MarkCommand;
import jamal.task.Deadline;
import jamal.task.Event;
import jamal.task.Todo;

/**
 * Turns a line typed by the user into the {@link Command} it describes.
 * All input checking happens here, so commands receive only valid data.
 */
public class Parser {
    /**
     * Parses one line of user input.
     *
     * @param fullCommand The full line typed by the user.
     * @return The command described by the line.
     * @throws JamaLException If the command word is unknown or its arguments are invalid.
     */
    public static Command parse(String fullCommand) throws JamaLException {
        // Split into the command word and everything after it
        String[] input = fullCommand.trim().split(" ", 2);
        String commandWord = input[0];
        String arguments = input.length > 1 ? input[1].trim() : "";
        if (commandWord.isEmpty()) {
            throw new JamaLException("say something.");
        }

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseTaskIndex(arguments, "mark"), true);
        case "unmark":
            return new MarkCommand(parseTaskIndex(arguments, "unmark"), false);
        case "delete":
            return new DeleteCommand(parseTaskIndex(arguments, "delete"));
        case "find":
            if (arguments.isEmpty()) {
                throw new JamaLException("find what.");
            }
            return new FindCommand(arguments);
        case "todo":
            return parseTodo(arguments);
        case "deadline":
            return parseDeadline(arguments);
        case "event":
            return parseEvent(arguments);
        default:
            // Anything that is not a known command word is rejected
            throw new JamaLException("I'm not doing whatever " + commandWord + " is.");
        }
    }

    /**
     * Turns the argument of a mark/unmark/delete command into a zero-based index.
     * Whether a task exists at that index is checked later by {@link TaskList}.
     *
     * @param arguments Text typed after the command word, e.g. "2".
     * @param commandWord Command word, used in the error message.
     * @return Zero-based index of the task the user meant.
     * @throws JamaLException If the argument is missing or is not a number.
     */
    private static int parseTaskIndex(String arguments, String commandWord) throws JamaLException {
        if (arguments.isEmpty()) {
            throw new JamaLException(commandWord + " what.");
        }
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            // parseInt throws on anything that is not a plain number, e.g. "abc".
            throw new JamaLException("that's not a number.");
        }
    }

    /**
     * Rejects task text containing "|", which the save file uses to separate
     * fields. Without this check the task would save, then be dropped as a
     * broken line the next time the app starts.
     *
     * @param arguments Task text typed by the user.
     * @throws JamaLException If the text contains "|".
     */
    private static void checkNoSeparator(String arguments) throws JamaLException {
        if (arguments.contains("|")) {
            throw new JamaLException("no | in tasks. it breaks the save file.");
        }
    }

    private static Command parseTodo(String description) throws JamaLException {
        if (description.isEmpty()) {
            throw new JamaLException("todo what.");
        }
        checkNoSeparator(description);
        return new AddCommand(new Todo(description));
    }

    /** Parses arguments of the form {@code <description> /by <when>}. */
    private static Command parseDeadline(String arguments) throws JamaLException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new JamaLException("deadline what /by when.");
        }
        checkNoSeparator(arguments);
        return new AddCommand(new Deadline(parts[0].trim(), parts[1].trim()));
    }

    /** Parses arguments of the form {@code <description> /from <start> /to <end>}. */
    private static Command parseEvent(String arguments) throws JamaLException {
        String[] descAndRest = arguments.split(" /from ", 2);
        if (descAndRest.length < 2 || descAndRest[0].trim().isEmpty()) {
            throw new JamaLException("event what /from when /to when.");
        }
        String[] fromAndTo = descAndRest[1].split(" /to ", 2);
        if (fromAndTo.length < 2 || fromAndTo[0].trim().isEmpty() || fromAndTo[1].trim().isEmpty()) {
            throw new JamaLException("event what /from when /to when.");
        }
        checkNoSeparator(arguments);
        return new AddCommand(new Event(descAndRest[0].trim(), fromAndTo[0].trim(), fromAndTo[1].trim()));
    }
}

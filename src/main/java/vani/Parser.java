package vani;

import vani.exception.VaniException;
import vani.task.Deadline;
import vani.task.Event;
import vani.task.Todo;

/**
 * Converts user input into commands and validates their syntax.
 * Task numbers are checked against the actual list by TaskList.
 */
public class Parser {
    /**
     * Creates a parser for Vani's supported command syntax.
     */
    public Parser() {
    }

    /**
     * Parses a command keyword and its arguments without changing any tasks.
     *
     * @param input the line entered by the user
     * @return the parsed operation and its arguments
     * @throws VaniException if the command or its arguments are invalid
     */
    public Command parse(String input) throws VaniException {
        String[] parts = input.trim().split("\\s+", 2);
        String arguments = parts.length == 2 ? parts[1] : "";

        switch (parts[0]) {
            case "bye":
                checkNoArguments(arguments);
                return new Command(Command.Type.BYE, 0, null);
            case "list":
                checkNoArguments(arguments);
                return new Command(Command.Type.LIST, 0, null);
            case "find":
                if (arguments.isEmpty()) {
                    throw new VaniException("The keyword for find cannot be empty. o_O");
                }
                return new Command(arguments);
            case "mark":
                return new Command(Command.Type.MARK, parseTaskNumber(arguments, "mark as done"), null);
            case "unmark":
                return new Command(Command.Type.UNMARK, parseTaskNumber(arguments, "unmark"), null);
            case "delete":
                return new Command(Command.Type.DELETE, parseTaskNumber(arguments, "delete"), null);
            case "todo":
                return new Command(Command.Type.TODO, 0, parseTodo(arguments));
            case "deadline":
                return new Command(Command.Type.DEADLINE, 0, parseDeadline(arguments));
            case "event":
                return new Command(Command.Type.EVENT, 0, parseEvent(arguments));
            default:
                throw new VaniException("I'm sorry, I don't understand that command. T-T");
        }
    }

    /**
     * Rejects arguments for commands that do not accept them.
     *
     * @param arguments the text following the command keyword
     * @throws VaniException if arguments were supplied
     */
    private void checkNoArguments(String arguments) throws VaniException {
        if (!arguments.isEmpty()) {
            throw new VaniException("I'm sorry, I don't understand that command. T-T");
        }
    }

    /**
     * Reads a task number while leaving list bounds checking to TaskList.
     *
     * @param arguments the text following the command keyword
     * @param action the action named in the missing-number error
     * @return the task number entered by the user
     * @throws VaniException if the number is missing or is not an integer
     */
    private int parseTaskNumber(String arguments, String action) throws VaniException {
        if (arguments.isEmpty()) {
            throw new VaniException("You didn't specify the task number to " + action + ". o_O");
        }

        try {
            return Integer.parseInt(arguments.split("\\s+")[0]);
        } catch (NumberFormatException e) {
            throw new VaniException("Invalid task number format. >:[");
        }
    }

    /**
     * Creates a todo from a nonempty description.
     *
     * @param description the text following the todo keyword
     * @return the new todo
     * @throws VaniException if the description is empty
     */
    private Todo parseTodo(String description) throws VaniException {
        if (description.isEmpty()) {
            throw new VaniException("The description of a todo cannot be empty. o_O");
        }
        return new Todo(description);
    }

    /**
     * Separates a deadline's description from its due date using /by.
     *
     * @param content the text following the deadline keyword
     * @return the new deadline
     * @throws VaniException if a required part is missing
     */
    private Deadline parseDeadline(String content) throws VaniException {
        if (content.isEmpty()) {
            throw new VaniException("The description of a deadline cannot be empty. o_O");
        }

        int byIndex = content.indexOf("/by");
        if (byIndex == -1) {
            throw new VaniException("You didn't specify the due date for the deadline. o_O");
        }

        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + 3).trim();
        if (description.isEmpty()) {
            throw new VaniException("The description of a deadline cannot be empty. o_O");
        }
        if (by.isEmpty()) {
            throw new VaniException("The due date of a deadline cannot be empty. o_O");
        }
        return new Deadline(description, by);
    }

    /**
     * Separates an event's description, start, and end using /from and /to.
     *
     * @param content the text following the event keyword
     * @return the new event
     * @throws VaniException if a required part is missing or the markers are reversed
     */
    private Event parseEvent(String content) throws VaniException {
        if (content.isEmpty()) {
            throw new VaniException("The description of an event cannot be empty. o_O");
        }

        int fromIndex = content.indexOf("/from");
        int toIndex = content.indexOf("/to");
        if (fromIndex == -1) {
            throw new VaniException("You didn't specify the start date for the event. o_O");
        }
        if (toIndex == -1) {
            throw new VaniException("You didn't specify the end date for the event. o_O");
        }
        if (toIndex < fromIndex) {
            throw new VaniException("Please specify /from before /to. o_O");
        }

        String description = content.substring(0, fromIndex).trim();
        String from = content.substring(fromIndex + 5, toIndex).trim();
        String to = content.substring(toIndex + 3).trim();
        if (description.isEmpty()) {
            throw new VaniException("The description of an event cannot be empty. o_O");
        }
        if (from.isEmpty()) {
            throw new VaniException("The start date of an event cannot be empty. o_O");
        }
        if (to.isEmpty()) {
            throw new VaniException("The end date of an event cannot be empty. o_O");
        }
        return new Event(description, from, to);
    }
}

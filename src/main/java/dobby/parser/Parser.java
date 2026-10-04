package dobby.parser;

import dobby.command.AddCommand;
import dobby.command.Command;
import dobby.command.ExitCommand;
import dobby.command.ListCommand;
import dobby.command.MarkCommand;
import dobby.command.UnmarkCommand;
import dobby.exception.DobbyException;
import dobby.task.Deadline;
import dobby.task.Event;
import dobby.task.Task;
import dobby.task.Todo;

/**
 * Converts complete user commands into executable command objects.
 */
public final class Parser {

    /** Prevents instantiation of this utility class. */
    private Parser() {
    }

    /**
     * Parses a complete user command.
     *
     * @param command the complete command entered by the user.
     * @return the command object represented by the input.
     * @throws DobbyException when the input is unknown or malformed.
     */
    public static Command parse(String command) throws DobbyException {
        if (command.equals("bye")) {
            return new ExitCommand();
        }

        if (command.equals("list")) {
            return new ListCommand();
        }

        if (command.equals("mark") || command.startsWith("mark ")) {
            return new MarkCommand(parseTaskNumber(command, "mark"));
        }

        if (command.equals("unmark") || command.startsWith("unmark ")) {
            return new UnmarkCommand(parseTaskNumber(command, "unmark"));
        }

        if (command.equals("todo") || command.startsWith("todo ")) {
            return new AddCommand(parseToDo(command));
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return new AddCommand(parseDeadline(command));
        }

        if (command.equals("event") || command.startsWith("event ")) {
            return new AddCommand(parseEvent(command));
        }

        throw new DobbyException(
                "I don't know that command. Try list, todo <description>, deadline <description> /by <date/time>, "
                        + "event <description> /from <start> /to <end>, mark <number>, unmark <number>, or bye.");
    }

    /**
     * Parses the task number in a mark or unmark command.
     *
     * @param command the complete command entered by the user.
     * @param action the command name being validated.
     * @return the one-based task number.
     * @throws DobbyException when the task number is malformed.
     */
    private static int parseTaskNumber(String command, String action) throws DobbyException {
        String taskNumberText = command.substring(action.length()).trim();

        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new DobbyException("The " + action + " command needs a task number. Use " + action
                    + " <number>, for example " + action + " 1.");
        }
    }

    /**
     * Parses an explicit ToDo command.
     *
     * @param command the complete ToDo command.
     * @return the parsed ToDo.
     * @throws DobbyException when the ToDo description is empty.
     */
    private static Task parseToDo(String command) throws DobbyException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new DobbyException("A ToDo needs a description. Use todo <description>, for example todo read book.");
        }

        return new Todo(description);
    }

    /**
     * Parses a deadline command using the {@code /by} separator.
     *
     * @param command the complete deadline command.
     * @return the parsed deadline.
     * @throws DobbyException when the description or due time is missing.
     */
    private static Task parseDeadline(String command) throws DobbyException {
        String taskDetails = command.substring("deadline".length()).trim();
        int byIndex = taskDetails.lastIndexOf(" /by ");

        if (byIndex < 0) {
            throw new DobbyException("A deadline needs a description and a due time. Use deadline <description> "
                    + "/by <date/time>.");
        }

        if (byIndex == 0) {
            throw new DobbyException("A deadline needs a description before /by. Add text before the /by marker.");
        }

        String description = taskDetails.substring(0, byIndex).trim();
        String deadline = taskDetails.substring(byIndex + " /by ".length()).trim();

        if (deadline.isEmpty()) {
            throw new DobbyException("A deadline needs a due time after /by. For example: deadline report /by Friday.");
        }

        return new Deadline(description, deadline);
    }

    /**
     * Parses an event command using the {@code /from} and {@code /to} separators.
     *
     * @param command the complete event command.
     * @return the parsed event.
     * @throws DobbyException when an event detail is missing.
     */
    private static Task parseEvent(String command) throws DobbyException {
        String taskDetails = command.substring("event".length()).trim();
        int fromIndex = taskDetails.indexOf(" /from ");
        int toIndex = fromIndex < 0 ? -1 : taskDetails.indexOf(" /to ", fromIndex + " /from ".length());

        if (taskDetails.isEmpty()) {
            throw new DobbyException("An event needs a description, start time, and end time. Use event <description> "
                    + "/from <start> /to <end>.");
        }

        if (fromIndex < 0) {
            throw new DobbyException("An event needs a start time after /from and an end time after /to. Use event "
                    + "<description> /from <start> /to <end>.");
        }

        if (fromIndex == 0) {
            throw new DobbyException("An event needs a description before /from. Add text before the /from marker.");
        }

        if (toIndex < 0) {
            throw new DobbyException("An event needs an end time after /to. Add /to <end> after the start time.");
        }

        String description = taskDetails.substring(0, fromIndex).trim();
        String start = taskDetails.substring(fromIndex + " /from ".length(), toIndex).trim();
        String end = taskDetails.substring(toIndex + " /to ".length()).trim();

        if (start.isEmpty()) {
            throw new DobbyException("An event needs a start time after /from. Add a start time before /to.");
        }

        if (end.isEmpty()) {
            throw new DobbyException("An event needs an end time after /to. For example: event meeting /from 2pm "
                    + "/to 4pm.");
        }

        return new Event(description, start, end);
    }
}

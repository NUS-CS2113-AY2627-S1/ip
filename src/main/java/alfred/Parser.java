package alfred;

import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.Task;
import alfred.task.Todo;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public class Parser {

    /**
     * Identifies the operation requested by a user command.
     */
    public enum CommandType {
        BYE,
        DEADLINE,
        DELETE,
        EVENT,
        LIST,
        MARK,
        TODO,
        UNMARK
    }

    /**
     * Identifies the type of a user command.
     *
     * @param command Full command entered by the user.
     * @return Type of the command.
     * @throws AlfredException If the command is not recognized.
     */
    public CommandType parseCommandType(String command) throws AlfredException {
        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (isCommand(command, "delete")) {
            return CommandType.DELETE;
        } else if (isCommand(command, "unmark")) {
            return CommandType.UNMARK;
        } else if (isCommand(command, "mark")) {
            return CommandType.MARK;
        } else if (isCommand(command, "todo")) {
            return CommandType.TODO;
        } else if (isCommand(command, "deadline")) {
            return CommandType.DEADLINE;
        } else if (isCommand(command, "event")) {
            return CommandType.EVENT;
        }
        throw new AlfredException("Pardon me Master Wayne, I did not quite get that.");
    }

    /**
     * Parses the task number from a delete, mark, or unmark command.
     *
     * @param command Full command entered by the user.
     * @param commandType Type of the command.
     * @return One-based task number supplied by the user.
     * @throws AlfredException If the task number is missing, invalid, or accompanied by extra arguments.
     * @throws IllegalArgumentException If the command type does not accept a task number.
     */
    public int parseTaskNumber(String command, CommandType commandType) throws AlfredException {
        if (commandType != CommandType.DELETE
                && commandType != CommandType.MARK
                && commandType != CommandType.UNMARK) {
            throw new IllegalArgumentException("Command type does not accept a task number.");
        }

        String[] parts = command.trim().split("\\s+");
        validateTaskNumberArguments(parts, commandType);

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Parses a task-creation command into a task.
     *
     * @param command Full command entered by the user.
     * @param commandType Type of the command.
     * @return Task described by the command.
     * @throws AlfredException If the task details are missing, unsafe, or ambiguous.
     * @throws IllegalArgumentException If the command type does not create a task.
     */
    public Task parseTask(String command, CommandType commandType) throws AlfredException {
        return switch (commandType) {
            case TODO -> parseTodo(command);
            case DEADLINE -> parseDeadline(command);
            case EVENT -> parseEvent(command);
            default -> throw new IllegalArgumentException("Command type does not create a task.");
        };
    }

    /**
     * Returns whether the text is the specified command or starts with that command and an argument.
     */
    private boolean isCommand(String text, String commandWord) {
        return text.equals(commandWord) || text.startsWith(commandWord + " ");
    }

    /**
     * Validates the number of arguments supplied to a task-number command.
     */
    private void validateTaskNumberArguments(String[] parts, CommandType commandType) throws AlfredException {
        if (commandType == CommandType.DELETE) {
            if (parts.length != 2) {
                throw new AlfredException("Please provide a task number to delete.");
            }
            return;
        }

        boolean isMarkCommand = commandType == CommandType.MARK;
        if (parts.length < 2) {
            if (isMarkCommand) {
                throw new AlfredException("Please provide a task number to mark as done.");
            }
            throw new AlfredException("Please provide a task number to mark as not done.");
        }
        if (parts.length > 2) {
            if (isMarkCommand) {
                throw new AlfredException("Please provide only one task number to mark as done.");
            }
            throw new AlfredException("Please provide only one task number to mark as not done.");
        }
    }

    /**
     * Parses a todo command if it contains a safe description.
     */
    private Task parseTodo(String command) throws AlfredException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new AlfredException("The description of a todo cannot be empty.");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw new AlfredException("The description of a todo cannot contain \"" + Storage.DELIMITER + "\", "
                    + "as it is reserved for saving tasks to disk.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command containing a description and {@code /by} value.
     */
    private Task parseDeadline(String command) throws AlfredException {
        String arguments = command.substring("deadline".length()).trim();
        int byPosition = findMarker(arguments, "/by", 0);
        if (byPosition < 0) {
            throw createDeadlineException("the /by marker is missing");
        }
        if (findMarker(arguments, "/by", byPosition + "/by".length()) >= 0) {
            throw createDeadlineException("the /by marker can only be used once");
        }

        String description = arguments.substring(0, byPosition).trim();
        String deadline = arguments.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw createDeadlineException("the task description is missing");
        }
        if (deadline.isEmpty()) {
            throw createDeadlineException("the deadline date or time is missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (deadline.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the deadline date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }
        return new Deadline(description, deadline);
    }

    /**
     * Parses an event command containing a description, start, and end value.
     */
    private Task parseEvent(String command) throws AlfredException {
        String arguments = command.substring("event".length()).trim();
        int fromPosition = findMarker(arguments, "/from", 0);
        if (fromPosition < 0) {
            throw createEventException("the /from marker is missing");
        }
        if (findMarker(arguments, "/from", fromPosition + "/from".length()) >= 0) {
            throw createEventException("the /from marker can only be used once");
        }

        int toPosition = findMarker(arguments, "/to", fromPosition + "/from".length());
        if (toPosition < 0) {
            if (findMarker(arguments, "/to", 0) >= 0) {
                throw createEventException("the /to marker must come after the /from marker");
            }
            throw createEventException("the /to marker is missing");
        }
        if (findMarker(arguments, "/to", toPosition + "/to".length()) >= 0) {
            throw createEventException("the /to marker can only be used once");
        }

        String description = arguments.substring(0, fromPosition).trim();
        String start = arguments.substring(fromPosition + "/from".length(), toPosition).trim();
        String end = arguments.substring(toPosition + "/to".length()).trim();
        if (description.isEmpty()) {
            throw createEventException("the task description is missing");
        }
        if (start.isEmpty()) {
            throw createEventException("the start date or time is missing");
        }
        if (end.isEmpty()) {
            throw createEventException("the end date or time is missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createEventException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (start.contains(Storage.DELIMITER)) {
            throw createEventException("the start date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (end.contains(Storage.DELIMITER)) {
            throw createEventException("the end date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }
        return new Event(description, start, end);
    }

    /**
     * Finds the next standalone occurrence of a marker from the specified index.
     */
    private int findMarker(String text, String marker, int fromIndex) {
        int searchFrom = fromIndex;
        while (true) {
            int index = text.indexOf(marker, searchFrom);
            if (index < 0) {
                return -1;
            }

            boolean isPrecededByBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            int afterMarkerIndex = index + marker.length();
            boolean isFollowedByBoundary = afterMarkerIndex == text.length()
                    || Character.isWhitespace(text.charAt(afterMarkerIndex));
            if (isPrecededByBoundary && isFollowedByBoundary) {
                return index;
            }
            searchFrom = index + 1;
        }
    }

    /**
     * Creates a detailed deadline error with the required command format.
     */
    private AlfredException createDeadlineException(String reason) {
        return new AlfredException("Unable to add the deadline: " + reason + ".\n"
                + "Use this format: deadline <description> /by <date or time>\n"
                + "Example: deadline return book /by tomorrow");
    }

    /**
     * Creates a detailed event error with the required command format.
     */
    private AlfredException createEventException(String reason) {
        return new AlfredException("Unable to add the event: " + reason + ".\n"
                + "Use this format: event <description> /from <start> /to <end>\n"
                + "Example: event project meeting /from Mon 2pm /to 4pm");
    }
}

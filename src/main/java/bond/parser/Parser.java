package bond.parser;

import java.time.format.DateTimeParseException;

import bond.command.AddCommand;
import bond.command.Command;
import bond.command.DeleteCommand;
import bond.command.ExitCommand;
import bond.command.FindCommand;
import bond.command.ListCommand;
import bond.command.MarkCommand;
import bond.command.UnmarkCommand;
import bond.exception.BondException;
import bond.task.Deadline;
import bond.task.Event;
import bond.task.Task;
import bond.task.Todo;

/**
 * Interprets user input and creates validated commands ready for execution.
 */
public final class Parser {

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final String DEADLINE_BY_MARKER = "/by";
    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";

    private static final String TODO_CORRECTION =
            "Brief me with: todo <description>.";
    private static final String DEADLINE_CORRECTION =
            "Brief me with: deadline <description> /by <yyyy-MM-dd>.";
    private static final String EVENT_CORRECTION =
            "Brief me with: event <description> /from <start> /to <end>.";
    private static final String FIND_CORRECTION = "Brief me with: find <keyword>.";
    private static final String UNKNOWN_COMMAND_MESSAGE =
            "I don't recognize that command.";
    private static final String UNKNOWN_COMMAND_CORRECTION =
            "Try: todo, deadline, event, find, list, mark, unmark, delete, or bye.";

    private Parser() {
    }

    /**
     * Parses user input into a command without changing the task list.
     * Task selections are validated against the supplied count, so the returned
     * command should execute before the task list changes.
     *
     * @param command Command entered by the user.
     * @param taskCount Number of tasks available for selection in the current session.
     * @return Validated command ready to execute against the current task list.
     * @throws BondException If the command is unknown or its arguments are invalid.
     */
    public static Command parse(String command, int taskCount) throws BondException {
        CommandType commandType = getCommandType(command);
        return switch (commandType) {
            case BYE -> new ExitCommand();
            case LIST -> new ListCommand();
            case FIND -> new FindCommand(getFindKeyword(command));
            case MARK -> new MarkCommand(getTaskIndex(command, CommandType.MARK, taskCount));
            case UNMARK -> new UnmarkCommand(getTaskIndex(command, CommandType.UNMARK, taskCount));
            case DELETE -> new DeleteCommand(getTaskIndex(command, CommandType.DELETE, taskCount));
            case TODO, DEADLINE, EVENT -> new AddCommand(createTask(command, commandType));
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
        };
    }

    /**
     * Returns the type of operation requested by a command.
     *
     * @param command Command entered by the user.
     * @return Type of the requested command.
     */
    private static CommandType getCommandType(String command) {
        if (command.equals(COMMAND_BYE)) {
            return CommandType.BYE;
        }
        if (command.equals(COMMAND_LIST)) {
            return CommandType.LIST;
        }
        if (isCommandWithDetails(command, COMMAND_FIND)) {
            return CommandType.FIND;
        }
        if (isCommandWithDetails(command, COMMAND_MARK)) {
            return CommandType.MARK;
        }
        if (isCommandWithDetails(command, COMMAND_UNMARK)) {
            return CommandType.UNMARK;
        }
        if (isCommandWithDetails(command, COMMAND_DELETE)) {
            return CommandType.DELETE;
        }
        if (isCommandWithDetails(command, COMMAND_TODO)) {
            return CommandType.TODO;
        }
        if (isCommandWithDetails(command, COMMAND_DEADLINE)) {
            return CommandType.DEADLINE;
        }
        if (isCommandWithDetails(command, COMMAND_EVENT)) {
            return CommandType.EVENT;
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Returns the zero-based task index specified by a task-reference command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of the command.
     * @param taskCount Number of missions available for selection.
     * @return Zero-based index of the referenced task.
     * @throws BondException If the mission number is missing, malformed, or outside the task list.
     */
    private static int getTaskIndex(String command, CommandType commandType,
            int taskCount) throws BondException {
        String commandName = switch (commandType) {
            case MARK -> COMMAND_MARK;
            case UNMARK -> COMMAND_UNMARK;
            case DELETE -> COMMAND_DELETE;
            default -> throw new IllegalArgumentException("Command does not reference a task index");
        };
        String correction = "Brief me with: " + commandName + " <mission number>.";
        String taskNumberText = getCommandDetails(command, commandName);
        if (taskNumberText.isEmpty()) {
            throw new BondException(
                    "I need a mission number for that " + commandName + " order.", correction);
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new BondException(
                    "That mission number is not a valid whole number.", correction);
        }

        if (taskNumber < 1) {
            throw new BondException("Mission numbers start at 1, agent.", correction);
        }
        if (taskCount == 0) {
            throw new BondException(
                    "The mission dossier is empty.",
                    "Add a mission before trying to " + commandName + " it.");
        }
        if (taskNumber > taskCount) {
            String rangeCorrection = taskCount == 1
                    ? "Choose mission number 1."
                    : "Choose a mission number from 1 to " + taskCount + ".";
            throw new BondException(
                    "Mission " + taskNumber + " is not in the dossier.", rangeCorrection);
        }

        return taskNumber - 1;
    }

    /**
     * Returns the keyword from a find command after checking that it is present.
     *
     * @param command Find command entered by the user.
     * @return Nonempty keyword to search for.
     * @throws BondException If the keyword is missing.
     */
    private static String getFindKeyword(String command) throws BondException {
        String keyword = getCommandDetails(command, COMMAND_FIND);
        if (keyword.isEmpty()) {
            throw new BondException("This find order needs a keyword.", FIND_CORRECTION);
        }
        return keyword;
    }

    /**
     * Creates the task described by a task-creation command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of the command.
     * @return Task described by the command.
     * @throws BondException If required task details are missing.
     */
    private static Task createTask(String command, CommandType commandType) throws BondException {
        return switch (commandType) {
            case TODO -> createTodo(command);
            case DEADLINE -> createDeadline(command);
            case EVENT -> createEvent(command);
            default -> throw new IllegalArgumentException("Command does not create a task");
        };
    }

    /**
     * Creates a to-do task after validating its description.
     *
     * @param command Todo command entered by the user.
     * @return Todo described by the command.
     * @throws BondException If the description is empty.
     */
    private static Todo createTodo(String command) throws BondException {
        String description = getCommandDetails(command, COMMAND_TODO);
        if (description.isEmpty()) {
            throw new BondException(
                    "This todo mission has no description.", TODO_CORRECTION);
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline task after validating its description and date.
     *
     * @param command Deadline command entered by the user.
     * @return Deadline described by the command.
     * @throws BondException If required deadline details are missing.
     */
    private static Deadline createDeadline(String command) throws BondException {
        String deadlineDetails = getCommandDetails(command, COMMAND_DEADLINE);
        if (deadlineDetails.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no description.", DEADLINE_CORRECTION);
        }

        int byMarkerIndex = findMarkerIndex(deadlineDetails, DEADLINE_BY_MARKER);
        if (byMarkerIndex == -1) {
            throw new BondException(
                    "This deadline mission is missing its /by marker.", DEADLINE_CORRECTION);
        }

        String description = deadlineDetails.substring(0, byMarkerIndex).trim();
        String by = deadlineDetails
                .substring(byMarkerIndex + DEADLINE_BY_MARKER.length())
                .trim();
        if (description.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no description.", DEADLINE_CORRECTION);
        }
        if (by.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no date.", DEADLINE_CORRECTION);
        }
        try {
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new BondException(
                    "This deadline mission needs a valid date in yyyy-MM-dd format.",
                    DEADLINE_CORRECTION);
        }
    }

    /**
     * Creates an event task after validating its description and time period.
     *
     * @param command Event command entered by the user.
     * @return Event described by the command.
     * @throws BondException If required event details are missing.
     */
    private static Event createEvent(String command) throws BondException {
        String eventDetails = getCommandDetails(command, COMMAND_EVENT);
        if (eventDetails.isEmpty()) {
            throw new BondException(
                    "This event mission has no description.", EVENT_CORRECTION);
        }

        int fromMarkerIndex = findMarkerIndex(eventDetails, EVENT_FROM_MARKER);
        if (fromMarkerIndex == -1) {
            throw new BondException(
                    "This event mission is missing its /from marker.", EVENT_CORRECTION);
        }

        String description = eventDetails.substring(0, fromMarkerIndex).trim();
        String eventPeriod = eventDetails
                .substring(fromMarkerIndex + EVENT_FROM_MARKER.length())
                .trim();
        if (description.isEmpty()) {
            throw new BondException(
                    "This event mission has no description.", EVENT_CORRECTION);
        }
        if (eventPeriod.isEmpty()) {
            throw new BondException(
                    "This event mission has no starting date or time.", EVENT_CORRECTION);
        }

        int toMarkerIndex = findMarkerIndex(eventPeriod, EVENT_TO_MARKER);
        if (toMarkerIndex == -1) {
            throw new BondException(
                    "This event mission is missing its /to marker.", EVENT_CORRECTION);
        }

        String from = eventPeriod.substring(0, toMarkerIndex).trim();
        String to = eventPeriod
                .substring(toMarkerIndex + EVENT_TO_MARKER.length())
                .trim();
        if (from.isEmpty()) {
            throw new BondException(
                    "This event mission has no starting date or time.", EVENT_CORRECTION);
        }
        if (to.isEmpty()) {
            throw new BondException(
                    "This event mission has no ending date or time.", EVENT_CORRECTION);
        }
        return new Event(description, from, to);
    }

    /**
     * Returns whether the input is a supported command, with or without details.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the supported command.
     * @return {@code true} if the input begins with the complete command name.
     */
    private static boolean isCommandWithDetails(String command, String commandName) {
        return command.equals(commandName) || command.startsWith(commandName + " ");
    }

    /**
     * Returns the trimmed details following a command name.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the command.
     * @return Trimmed text following the command name.
     */
    private static String getCommandDetails(String command, String commandName) {
        return command.substring(commandName.length()).trim();
    }

    /**
     * Returns the position of a standalone syntax marker in task details.
     *
     * @param details Task details that may contain the marker.
     * @param marker Syntax marker to locate.
     * @return Marker position, or -1 if the marker is not a separate token.
     */
    private static int findMarkerIndex(String details, String marker) {
        int markerIndex = details.indexOf(marker);
        while (markerIndex != -1) {
            int markerEndIndex = markerIndex + marker.length();
            boolean hasLeadingBoundary = markerIndex == 0
                    || Character.isWhitespace(details.charAt(markerIndex - 1));
            boolean hasTrailingBoundary = markerEndIndex == details.length()
                    || Character.isWhitespace(details.charAt(markerEndIndex));
            if (hasLeadingBoundary && hasTrailingBoundary) {
                return markerIndex;
            }
            markerIndex = details.indexOf(marker, markerIndex + 1);
        }
        return -1;
    }
}

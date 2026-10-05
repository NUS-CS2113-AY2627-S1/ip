package ernest.parser;

import ernest.command.AddCommand;
import ernest.command.ClearCommand;
import ernest.command.Command;
import ernest.command.DeleteCommand;
import ernest.command.ExitCommand;
import ernest.command.FindCommand;
import ernest.command.HelpCommand;
import ernest.command.InvalidCommand;
import ernest.command.ListCommand;
import ernest.command.MarkCommand;
import ernest.command.UnmarkCommand;
import ernest.exception.ErnestException;
import ernest.task.Deadline;
import ernest.task.Event;
import ernest.task.Task;
import ernest.task.TaskDateTime;
import ernest.task.ToDo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Converts raw user input into commands that Ernest can execute.
 */
public final class Parser {
    /** Date-time format with compact time accepted for deadlines and events. */
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT_COMPACT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);
    /** Date-time format with colon-separated time accepted for deadlines and events. */
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT_COLON =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String TODO_PREFIX = COMMAND_TODO + " ";
    private static final String DEADLINE_PREFIX = COMMAND_DEADLINE + " ";
    private static final String EVENT_PREFIX = COMMAND_EVENT + " ";
    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";
    private static final String ERROR_INVALID_TASK = "Sorry, please insert a valid task.";
    private static final String ERROR_MISSING_TASK_DESCRIPTION = "Missing task description. Please try again.";
    private static final String ERROR_MISSING_DEADLINE_MARKER = "Deadline must include a /by date.";
    private static final String ERROR_MULTIPLE_DEADLINE_MARKERS = "Deadline may contain only one /by marker.";
    private static final String ERROR_MISSING_DEADLINE_DESCRIPTION =
            "Missing deadline description. Please try again.";
    private static final String ERROR_MISSING_DEADLINE_DATE = "Missing deadline date. Please try again.";
    private static final String ERROR_INVALID_DEADLINE_DATE_TIME =
            "Invalid deadline date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.";
    private static final String ERROR_MISSING_EVENT_FROM_MARKER = "Event must include a /from date.";
    private static final String ERROR_MISSING_EVENT_TO_MARKER = "Event must include a /to date.";
    private static final String ERROR_REVERSED_EVENT_MARKERS = "The /to marker must come after /from.";
    private static final String ERROR_MULTIPLE_EVENT_MARKERS =
            "Event may only contain one /from and one /to marker.";
    private static final String ERROR_MISSING_EVENT_DESCRIPTION = "Missing event description. Please try again.";
    private static final String ERROR_MISSING_EVENT_START = "Missing event start date. Please try again.";
    private static final String ERROR_MISSING_EVENT_END = "Missing event end date. Please try again.";
    private static final String ERROR_INVALID_EVENT_DATE_TIME =
            "Invalid event date or time. Please use yyyy-MM-dd with an optional HHmm or HH:mm time.";
    private static final String ERROR_EVENT_END_BEFORE_START = "Event end cannot be before its start.";
    private static final String ERROR_MISSING_TASK_NUMBER =
            "Missing task number. Please refer to the task list and try again.";
    private static final String ERROR_NON_INTEGER_TASK_NUMBER = "Task number must be an integer.";
    private static final String ERROR_MISSING_SEARCH_KEYWORD = "Missing search keyword. Please try again.";

    private Parser() {
        // Prevent instantiation of this utility class.
    }

    /**
     * Returns the command represented by a line of user input.
     *
     * @param commandLine command entered by the user.
     * @return command represented by the input.
     * @throws ErnestException if a recognized command contains invalid arguments.
     */
    public static Command parse(String commandLine) throws ErnestException {
        ParsedCommand command = parseInput(commandLine);
        if (command.parts().length == 0) {
            return new InvalidCommand();
        }

        String commandWord = command.parts()[0];
        switch (commandWord) {
            case COMMAND_BYE:
                return command.parts().length == 1 ? new ExitCommand() : new InvalidCommand();
            case COMMAND_LIST:
                return command.parts().length == 1 ? new ListCommand() : new InvalidCommand();
            case COMMAND_DELETE:
                // Fallthrough
            case COMMAND_MARK:
                // Fallthrough
            case COMMAND_UNMARK:
                return parseNumberedCommand(command);
            case COMMAND_FIND:
                return parseFindCommand(command);
            case COMMAND_CLEAR:
                return command.parts().length == 1 ? new ClearCommand() : new InvalidCommand();
            case COMMAND_HELP:
                return command.parts().length == 1 ? new HelpCommand() : new InvalidCommand();
            case COMMAND_TODO:
                // Fallthrough
            case COMMAND_DEADLINE:
                // Fallthrough
            case COMMAND_EVENT:
                return parseAddCommand(command);
            default:
                return new InvalidCommand();
        }
    }

    /**
     * Trims and normalizes command text while preserving its original casing.
     *
     * @param commandLine command entered by the user.
     * @return parsed command text and its normalized parts.
     */
    private static ParsedCommand parseInput(String commandLine) {
        String trimmedCommand = commandLine.strip();
        String normalizedCommand = trimmedCommand.toLowerCase(Locale.ROOT);
        String[] commandParts = normalizedCommand.isEmpty()
                ? new String[0] : normalizedCommand.split("\\s+", 2);
        return new ParsedCommand(trimmedCommand, commandParts);
    }

    /**
     * Returns an add command containing the parsed task.
     *
     * @param command parsed task-creation command.
     * @return executable command represented by the input.
     * @throws ErnestException if the task details are invalid.
     */
    private static Command parseAddCommand(ParsedCommand command) throws ErnestException {
        return new AddCommand(parseTask(command));
    }

    /**
     * Returns a find command containing the user's search keyword.
     *
     * @param command parsed find command.
     * @return executable command represented by the input.
     * @throws ErnestException if the search keyword is missing.
     */
    private static Command parseFindCommand(ParsedCommand command) throws ErnestException {
        if (command.parts().length < 2 || command.parts()[1].isEmpty()) {
            throw new ErnestException(ERROR_MISSING_SEARCH_KEYWORD);
        }
        return new FindCommand(command.parts()[1]);
    }

    /**
     * Returns a numbered task command containing the parsed task number.
     *
     * @param command parsed command containing a task number.
     * @return executable command represented by the input.
     * @throws ErnestException if the task number is missing or nonnumeric.
     */
    private static Command parseNumberedCommand(ParsedCommand command) throws ErnestException {
        int taskNumber = parseTaskNumber(command);
        switch (command.parts()[0]) {
            case COMMAND_DELETE:
                return new DeleteCommand(taskNumber);
            case COMMAND_MARK:
                return new MarkCommand(taskNumber);
            case COMMAND_UNMARK:
                return new UnmarkCommand(taskNumber);
            default:
                throw new IllegalArgumentException("Unsupported numbered command: " + command.parts()[0]);
        }
    }

    /**
     * Parses and constructs the task represented by a task command.
     *
     * @param command parsed task command.
     * @return task represented by the command.
     * @throws ErnestException if the task details are invalid.
     */
    private static Task parseTask(ParsedCommand command) throws ErnestException {
        if (command.parts().length < 2) {
            throw new ErnestException(ERROR_MISSING_TASK_DESCRIPTION);
        }

        String taskCommand = command.text().strip().replaceFirst("\\s+", " ");
        String normalizedCommand = taskCommand.toLowerCase(Locale.ROOT);

        if (normalizedCommand.startsWith(TODO_PREFIX)) {
            return parseToDo(taskCommand);
        } else if (normalizedCommand.startsWith(DEADLINE_PREFIX)) {
            return parseDeadline(taskCommand, normalizedCommand);
        } else if (normalizedCommand.startsWith(EVENT_PREFIX)) {
            return parseEvent(taskCommand, normalizedCommand);
        } else {
            throw new ErnestException(ERROR_INVALID_TASK);
        }
    }

    /**
     * Parses the task number argument of a numbered task command.
     *
     * @param command parsed command containing the task number argument.
     * @return parsed task number.
     * @throws ErnestException if the task number is missing or nonnumeric.
     */
    private static int parseTaskNumber(ParsedCommand command) throws ErnestException {
        if (command.parts().length < 2 || command.parts()[1].isEmpty()) {
            throw new ErnestException(ERROR_MISSING_TASK_NUMBER);
        }

        try {
            return Integer.parseInt(command.parts()[1]);
        } catch (NumberFormatException exception) {
            throw new ErnestException(ERROR_NON_INTEGER_TASK_NUMBER);
        }
    }

    /**
     * Parses a to-do task command.
     *
     * @param taskCommand command containing the task description.
     * @return task represented by the command.
     * @throws ErnestException if the task description is missing.
     */
    private static Task parseToDo(String taskCommand) throws ErnestException {
        String taskName = taskCommand.substring(TODO_PREFIX.length()).strip();
        if (taskName.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_TASK_DESCRIPTION);
        }
        return new ToDo(taskName);
    }

    /**
     * Parses a deadline task command.
     *
     * @param taskCommand command containing the original-casing task details.
     * @param normalizedCommand normalized command used to find markers.
     * @return task represented by the command.
     * @throws ErnestException if the deadline details are invalid.
     */
    private static Task parseDeadline(String taskCommand, String normalizedCommand) throws ErnestException {
        int deadlineMarker = findMarker(normalizedCommand, DEADLINE_MARKER);
        if (deadlineMarker < 0) {
            throw new ErnestException(ERROR_MISSING_DEADLINE_MARKER);
        }
        if (findMarker(normalizedCommand, DEADLINE_MARKER,
                deadlineMarker + DEADLINE_MARKER.length()) >= 0) {
            throw new ErnestException(ERROR_MULTIPLE_DEADLINE_MARKERS);
        }

        String taskName = taskCommand.substring(DEADLINE_PREFIX.length(), deadlineMarker).strip();
        String deadline = taskCommand.substring(deadlineMarker + DEADLINE_MARKER.length()).strip();
        if (taskName.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_DEADLINE_DESCRIPTION);
        }
        if (deadline.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_DEADLINE_DATE);
        }
        try {
            return new Deadline(taskName, parseDateTime(deadline));
        } catch (DateTimeParseException exception) {
            throw new ErnestException(ERROR_INVALID_DEADLINE_DATE_TIME);
        }
    }

    /**
     * Parses an event task command.
     *
     * @param taskCommand command containing the original-casing task details.
     * @param normalizedCommand normalized command used to find markers.
     * @return task represented by the command.
     * @throws ErnestException if the event details are invalid.
     */
    private static Task parseEvent(String taskCommand, String normalizedCommand) throws ErnestException {
        int fromMarker = findMarker(normalizedCommand, EVENT_FROM_MARKER);
        int toMarker = findMarker(normalizedCommand, EVENT_TO_MARKER);
        if (fromMarker < 0) {
            throw new ErnestException(ERROR_MISSING_EVENT_FROM_MARKER);
        }
        if (toMarker < 0) {
            throw new ErnestException(ERROR_MISSING_EVENT_TO_MARKER);
        }
        if (toMarker <= fromMarker) {
            throw new ErnestException(ERROR_REVERSED_EVENT_MARKERS);
        }
        if (findMarker(normalizedCommand, EVENT_FROM_MARKER,
                fromMarker + EVENT_FROM_MARKER.length()) >= 0
                || findMarker(normalizedCommand, EVENT_TO_MARKER,
                toMarker + EVENT_TO_MARKER.length()) >= 0) {
            throw new ErnestException(ERROR_MULTIPLE_EVENT_MARKERS);
        }

        String taskName = taskCommand.substring(EVENT_PREFIX.length(), fromMarker).strip();
        String durationStart = taskCommand.substring(fromMarker + EVENT_FROM_MARKER.length(), toMarker)
                .strip();
        String durationEnd = taskCommand.substring(toMarker + EVENT_TO_MARKER.length()).strip();
        if (taskName.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_EVENT_DESCRIPTION);
        }
        if (durationStart.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_EVENT_START);
        }
        if (durationEnd.isEmpty()) {
            throw new ErnestException(ERROR_MISSING_EVENT_END);
        }
        try {
            TaskDateTime startDateTime = parseDateTime(durationStart);
            TaskDateTime endDateTime = parseDateTime(durationEnd);
            if (endDateTime.isBefore(startDateTime)) {
                throw new ErnestException(ERROR_EVENT_END_BEFORE_START);
            }
            return new Event(taskName, startDateTime, endDateTime);
        } catch (DateTimeParseException exception) {
            throw new ErnestException(ERROR_INVALID_EVENT_DATE_TIME);
        }
    }

    /**
     * Returns a required date and optional time parsed from a supported format.
     *
     * @param dateTime date and optional time to parse.
     * @return parsed date and optional time.
     * @throws DateTimeParseException if the value is not valid date-time input.
     */
    private static TaskDateTime parseDateTime(String dateTime) {
        if (!dateTime.contains(" ")) {
            return new TaskDateTime(LocalDate.parse(dateTime));
        }
        DateTimeFormatter inputFormat = dateTime.contains(":")
                ? INPUT_DATE_TIME_FORMAT_COLON : INPUT_DATE_TIME_FORMAT_COMPACT;
        return new TaskDateTime(LocalDateTime.parse(dateTime, inputFormat));
    }

    /**
     * Returns the position of a marker that is separated from surrounding text.
     *
     * @param command normalized command to search.
     * @param marker marker to find.
     * @return marker position, or {@code -1} when no valid marker is present.
     */
    private static int findMarker(String command, String marker) {
        return findMarker(command, marker, 0);
    }

    /**
     * Returns the position of a separated marker after a given position.
     *
     * @param command normalized command to search.
     * @param marker marker to find.
     * @param searchFrom position at which to start searching.
     * @return marker position, or {@code -1} when no valid marker is present.
     */
    private static int findMarker(String command, String marker, int searchFrom) {
        int markerPosition = command.indexOf(marker, searchFrom);
        while (markerPosition >= 0) {
            int markerEnd = markerPosition + marker.length();
            boolean hasWhitespaceBefore = markerPosition > 0
                    && Character.isWhitespace(command.charAt(markerPosition - 1));
            boolean hasWhitespaceAfter = markerEnd == command.length()
                    || Character.isWhitespace(command.charAt(markerEnd));
            if (hasWhitespaceBefore && hasWhitespaceAfter) {
                return markerPosition;
            }
            markerPosition = command.indexOf(marker, markerEnd);
        }
        return -1;
    }

    /**
     * Stores the original-casing command text and its normalized parts.
     *
     * @param text trimmed command text with its original casing.
     * @param parts normalized command parts.
     */
    private record ParsedCommand(String text, String[] parts) {
    }
}

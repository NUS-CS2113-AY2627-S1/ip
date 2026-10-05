package yanny.command;

import java.util.Locale;

import yanny.exception.YannyException;
import yanny.task.Deadline;
import yanny.task.DeadlineDateParser;
import yanny.task.Event;
import yanny.task.Task;
import yanny.task.Todo;

/**
 * Interprets task commands and validates their input values.
 */
public class Parser {
    /** Identifies which existing command handler should process an input line. */
    enum CommandType {
        LIST, FIND, MARK, UNMARK, DELETE, REMOVE, ADD
    }

    private static final String SUPPORTED_COMMANDS =
            "TODO, DEADLINE, EVENT, LIST, FIND, MARK, UNMARK, DELETE, REMOVE, OR BYE";
    private static final String TODO_USAGE = "TODO <DESCRIPTION>";
    private static final String DEADLINE_USAGE =
            "DEADLINE <DESCRIPTION> /BY <YYYY-MM-DD OR D/M/YYYY HHMM>";
    private static final String EVENT_USAGE = "EVENT <DESCRIPTION> /FROM <START> /TO <END>";

    /**
     * Identifies the handler for a command while preserving its original text.
     *
     * @param command the complete command entered by the user.
     * @return the command type used for dispatch.
     * @throws YannyException if the command is null.
     */
    CommandType parseCommandType(String command) throws YannyException {
        if (command == null) {
            throw new YannyException("COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.");
        }
        String trimmedCommand = command.trim();

        if (trimmedCommand.equalsIgnoreCase("list")) {
            return CommandType.LIST;
        }
        if (isCommand(trimmedCommand, "find")) {
            return CommandType.FIND;
        }
        if (isCommand(trimmedCommand, "mark")) {
            return CommandType.MARK;
        }
        if (isCommand(trimmedCommand, "unmark")) {
            return CommandType.UNMARK;
        }
        if (command.equalsIgnoreCase("delete")
                || command.toLowerCase(Locale.ROOT).startsWith("delete ")) {
            return CommandType.DELETE;
        }
        if (command.equalsIgnoreCase("remove")
                || command.toLowerCase(Locale.ROOT).startsWith("remove ")) {
            return CommandType.REMOVE;
        }
        return CommandType.ADD;
    }

    /**
     * Returns the non-blank description keyword from a find command.
     *
     * @param command the complete find command.
     * @return the trimmed keyword or phrase to search for.
     * @throws YannyException if no keyword was supplied.
     */
    public String parseFindKeyword(String command) throws YannyException {
        String keyword = command.substring(4).trim();
        if (keyword.isBlank()) {
            throw new YannyException("FIND KEYWORD CANNOT BE EMPTY. USE: FIND <KEYWORD>");
        }
        return keyword;
    }

    /**
     * Returns whether a command is exactly a keyword or starts with whitespace after it.
     *
     * @param command the command text to inspect.
     * @param keyword the expected command keyword.
     * @return true if the keyword matches at a command boundary.
     */
    private boolean isCommand(String command, String keyword) {
        if (command.length() < keyword.length()
                || !command.regionMatches(true, 0, keyword, 0, keyword.length())) {
            return false;
        }
        return command.length() == keyword.length()
                || Character.isWhitespace(command.charAt(keyword.length()));
    }

    /**
     * Parses a one-based task number from a numbered command.
     *
     * @param command the numbered command.
     * @param commandName the command keyword.
     * @return the zero-based task index.
     * @throws YannyException if the task number is missing or invalid.
     */
    public int parseTaskIndex(String command, String commandName) throws YannyException {
        String taskNumberText = command.length() > commandName.length()
                ? command.substring(commandName.length()).trim() : "";
        String upperCommandName = commandName.toUpperCase(Locale.ROOT);
        if (taskNumberText.isBlank()) {
            throw new YannyException(upperCommandName
                    + " COMMAND REQUIRES A TASK NUMBER. USE: " + upperCommandName + " <NUMBER>");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber <= 0) {
                throw new YannyException(upperCommandName
                        + " TASK NUMBER MUST BE POSITIVE. USE: " + upperCommandName + " <NUMBER>");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new YannyException(upperCommandName
                    + " TASK NUMBER MUST BE AN INTEGER. USE: " + upperCommandName + " <NUMBER>");
        }
    }

    /**
     * Parses a task command into the appropriate task type.
     *
     * @param command the complete command entered by the user.
     * @return the parsed task.
     * @throws YannyException if the command is unrecognized or contains invalid task data.
     */
    public Task parseTaskCommand(String command) throws YannyException {
        if (command == null || command.isBlank()) {
            throw new YannyException("COMMAND CANNOT BE EMPTY. ENTER A SUPPORTED COMMAND.");
        }
        String normalizedCommand = command.trim();

        if (isCommand(normalizedCommand, "todo")) {
            return new Todo(requireDescription(normalizedCommand.substring(4), "TODO"));
        }

        if (isCommand(normalizedCommand, "deadline")) {
            return parseDeadline(normalizedCommand.substring(8));
        }

        if (isCommand(normalizedCommand, "event")) {
            return parseEvent(normalizedCommand.substring(5));
        }

        throw new YannyException("UNKNOWN COMMAND DETECTED. USE: " + SUPPORTED_COMMANDS);
    }

    /**
     * Parses the description and deadline from a deadline command.
     *
     * @param commandContent the part of the command after {@code deadline}.
     * @return the parsed deadline task.
     * @throws YannyException if the description or deadline is invalid.
     */
    private Deadline parseDeadline(String commandContent) throws YannyException {
        int byIndex = findMarker(commandContent, "/by");
        if (byIndex < 0) {
            throw new YannyException("DEADLINE COMMAND REQUIRES: " + DEADLINE_USAGE);
        }

        String description = requireDescription(commandContent.substring(0, byIndex), "DEADLINE");
        String deadline = commandContent.substring(byIndex + 3).trim();
        if (deadline.isBlank()) {
            throw new YannyException("DEADLINE /BY VALUE CANNOT BE EMPTY. USE: " + DEADLINE_USAGE);
        }
        rejectUnsupportedStorageCharacters(deadline, "DEADLINE /BY VALUE");
        try {
            return DeadlineDateParser.parse(description, deadline);
        } catch (IllegalArgumentException exception) {
            throw new YannyException("DEADLINE /BY VALUE MUST BE A VALID DATE OR TIME. USE: " + DEADLINE_USAGE);
        }
    }

    /**
     * Parses the description, start, and end values from an event command.
     *
     * @param commandContent the part of the command after {@code event}.
     * @return the parsed event task.
     * @throws YannyException if the description or event values are invalid.
     */
    private Event parseEvent(String commandContent) throws YannyException {
        int fromIndex = findMarker(commandContent, "/from");
        if (fromIndex < 0) {
            throw new YannyException("EVENT COMMAND REQUIRES: " + EVENT_USAGE);
        }

        String remainingContent = commandContent.substring(fromIndex + 5);
        int toIndex = findMarker(remainingContent, "/to");
        if (toIndex < 0) {
            throw new YannyException("EVENT COMMAND REQUIRES: " + EVENT_USAGE);
        }
        toIndex += fromIndex + 5;

        String description = requireDescription(commandContent.substring(0, fromIndex), "EVENT");
        String start = commandContent.substring(fromIndex + 5, toIndex).trim();
        String end = commandContent.substring(toIndex + 3).trim();
        if (start.isBlank()) {
            throw new YannyException("EVENT /FROM VALUE CANNOT BE EMPTY. USE: " + EVENT_USAGE);
        }
        if (end.isBlank()) {
            throw new YannyException("EVENT /TO VALUE CANNOT BE EMPTY. USE: " + EVENT_USAGE);
        }
        rejectUnsupportedStorageCharacters(start, "EVENT /FROM VALUE");
        rejectUnsupportedStorageCharacters(end, "EVENT /TO VALUE");
        return new Event(description, start, end);
    }

    /**
     * Finds a marker at the start of a value or after whitespace.
     *
     * @param text the text in which to search.
     * @param marker the marker to find.
     * @return the marker index, or {@code -1} when the marker is absent.
     */
    private int findMarker(String text, String marker) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        String lowerMarker = marker.toLowerCase(Locale.ROOT);
        int markerIndex = lowerText.indexOf(lowerMarker);
        while (markerIndex >= 0) {
            boolean startsAtBoundary = markerIndex == 0
                    || Character.isWhitespace(text.charAt(markerIndex - 1));
            int markerEnd = markerIndex + marker.length();
            boolean endsAtBoundary = markerEnd == text.length()
                    || Character.isWhitespace(text.charAt(markerEnd));
            if (startsAtBoundary && endsAtBoundary) {
                return markerIndex;
            }
            markerIndex = lowerText.indexOf(lowerMarker, markerIndex + 1);
        }
        return -1;
    }

    /**
     * Returns a non-blank description or rejects the task command.
     *
     * @param text the candidate task description.
     * @param taskType the task type used in the error message.
     * @return the trimmed description.
     * @throws YannyException if the description is blank.
     */
    private String requireDescription(String text, String taskType) throws YannyException {
        String description = text.trim();
        if (description.isBlank()) {
            String usage = switch (taskType) {
            case "TODO" -> TODO_USAGE;
            case "DEADLINE" -> DEADLINE_USAGE;
            case "EVENT" -> EVENT_USAGE;
            default -> taskType;
            };
            throw new YannyException(taskType + " DESCRIPTION CANNOT BE EMPTY. USE: " + usage);
        }
        rejectUnsupportedStorageCharacters(description, taskType + " DESCRIPTION");
        return description;
    }

    /** Rejects values that cannot be represented safely by the task file format. */
    private void rejectUnsupportedStorageCharacters(String value, String fieldName) throws YannyException {
        if (value.indexOf('|') >= 0) {
            throw new YannyException(fieldName + " CONTAINS AN UNSUPPORTED CHARACTER. REMOVE '|'.");
        }
        if (value.indexOf('\u0000') >= 0 || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            throw new YannyException(fieldName
                    + " CONTAINS AN UNSUPPORTED CONTROL CHARACTER. USE PLAIN TEXT.");
        }
    }
}

package maple.parser;

import maple.exception.MapleException;
import maple.task.Deadline;
import maple.task.Event;
import maple.task.Todo;

/**
 * Parses user input into commands and task data.
 */
public class Parser {
    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_START_MARKER = "/from";
    private static final String EVENT_END_MARKER = "/to";

    /**
     * Parses user input into a command keyword and its details.
     *
     * @param input the user input to parse.
     * @return the parsed command.
     */
    public ParsedCommand parseCommand(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        String keyword = parts[0];
        String detail = parts.length > 1 ? parts[1].trim() : "";
        return new ParsedCommand(keyword, detail);
    }

    /**
     * Parses a one-based task number from command details.
     *
     * @param detail the command details containing the task number.
     * @param action the action that needs the task number.
     * @return the parsed one-based task number.
     * @throws MapleException if the task number is missing or invalid.
     */
    public int parseTaskNumber(String detail, String action) throws MapleException {
        if (detail.isEmpty()) {
            throw new MapleException("Specify the number of the task to " + action + ".");
        }

        try {
            return Integer.parseInt(detail);
        } catch (NumberFormatException exception) {
            throw new MapleException("A task number must be a whole number.");
        }
    }

    /**
     * Parses the keyword of a find command.
     *
     * @param detail the command details containing the keyword.
     * @return the keyword to find.
     * @throws MapleException if the keyword is missing.
     */
    public String parseFindKeyword(String detail) throws MapleException {
        if (detail.isEmpty()) {
            throw new MapleException("A find command needs a keyword.");
        }
        return detail;
    }

    /**
     * Parses a todo from its command details.
     *
     * @param description the todo description.
     * @return the parsed todo.
     * @throws MapleException if the description is empty.
     */
    public Todo parseTodo(String description) throws MapleException {
        if (description.isEmpty()) {
            throw new MapleException("A todo needs a description.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline from its command details.
     *
     * @param input the deadline description and due time.
     * @return the parsed deadline.
     * @throws MapleException if the description or due time is missing.
     */
    public Deadline parseDeadline(String input) throws MapleException {
        int markerIndex = findMarker(input, DEADLINE_MARKER, 0);
        String description = markerIndex < 0 ? input : input.substring(0, markerIndex).trim();
        if (description.isEmpty()) {
            throw new MapleException("A deadline needs a description.");
        }
        if (markerIndex < 0) {
            throw new MapleException("A deadline needs a date or time after /by.");
        }

        String dueTime = input.substring(markerIndex + DEADLINE_MARKER.length()).trim();
        if (dueTime.isEmpty()) {
            throw new MapleException("A deadline needs a date or time after /by.");
        }
        return new Deadline(description, dueTime);
    }

    /**
     * Parses an event from its command details.
     *
     * @param input the event description, start time and end time.
     * @return the parsed event.
     * @throws MapleException if the description or either time is missing.
     */
    public Event parseEvent(String input) throws MapleException {
        int startMarkerIndex = findMarker(input, EVENT_START_MARKER, 0);
        int endMarkerStart = startMarkerIndex < 0
                ? 0
                : startMarkerIndex + EVENT_START_MARKER.length();
        int endMarkerIndex = findMarker(input, EVENT_END_MARKER, endMarkerStart);
        String description = startMarkerIndex < 0 ? input : input.substring(0, startMarkerIndex).trim();
        if (description.isEmpty()) {
            throw new MapleException("An event needs a description.");
        }
        if (startMarkerIndex < 0) {
            throw new MapleException("An event needs a start after /from.");
        }
        if (endMarkerIndex < 0) {
            throw new MapleException("An event needs an end after /to.");
        }

        String startTime = input.substring(
                startMarkerIndex + EVENT_START_MARKER.length(), endMarkerIndex).trim();
        String endTime = input.substring(endMarkerIndex + EVENT_END_MARKER.length()).trim();
        if (startTime.isEmpty()) {
            throw new MapleException("An event needs a start after /from.");
        }
        if (endTime.isEmpty()) {
            throw new MapleException("An event needs an end after /to.");
        }
        return new Event(description, startTime, endTime);
    }

    private static int findMarker(String input, String marker, int startIndex) {
        int markerIndex = input.indexOf(marker, startIndex);
        while (markerIndex >= 0) {
            int markerEndIndex = markerIndex + marker.length();
            boolean hasLeadingBoundary = markerIndex == 0 || Character.isWhitespace(input.charAt(markerIndex - 1));
            boolean hasTrailingBoundary = markerEndIndex == input.length()
                    || Character.isWhitespace(input.charAt(markerEndIndex));
            if (hasLeadingBoundary && hasTrailingBoundary) {
                return markerIndex;
            }
            markerIndex = input.indexOf(marker, markerEndIndex);
        }
        return -1;
    }
}

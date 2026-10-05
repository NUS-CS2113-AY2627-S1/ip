package goatbot.command;

import goatbot.exception.GoatBotException;
import goatbot.task.Deadline;
import goatbot.task.Event;
import goatbot.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into task objects.
 */
public class Parser {
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please try again.";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String TODO_COMMAND = "todo";
    private static final String BY_MARKER = "/by";
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";
    private static final String FIND_COMMAND = "find";
    private static final DateTimeFormatter DATE_TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Parses user input into a deadline task.
     *
     * @param userInput user command that describes the deadline
     * @return deadline task represented by the command
     * @throws GoatBotException if the command cannot be parsed
     */
    public static Deadline parseDeadline(String userInput) throws GoatBotException {
        String deadlineDetails = userInput.substring(DEADLINE_COMMAND.length()).trim();
        int deadlineIndex = deadlineDetails.indexOf(BY_MARKER);
        if (deadlineIndex == -1) {
            throw new GoatBotException("Invalid deadline format. Please try again.");
        }

        String deadlineString = deadlineDetails.substring(0, deadlineIndex).trim();
        String deadlineDate = deadlineDetails.substring(deadlineIndex + BY_MARKER.length()).trim();
        if (deadlineString.isEmpty() || deadlineDate.isEmpty()) {
            throw new GoatBotException("Invalid deadline format. Please try again.");
        }
        try {
            LocalDateTime dateTime =
                    LocalDateTime.parse(deadlineDate, DATE_TIME_INPUT_FORMAT);
            return new Deadline(deadlineString, dateTime);
        } catch (DateTimeParseException e) {
            throw new GoatBotException(
                    "Invalid date. Use d/M/yyyy HHmm, e.g. 2/12/2019 1800."
            );
        }
    }

    /**
     * Parses user input into an event task.
     *
     * @param userInput user command that describes the event
     * @return event task represented by the command
     * @throws GoatBotException if the command cannot be parsed
     */
    public static Event parseEvent(String userInput) throws GoatBotException {
        String eventDetails = userInput.substring(EVENT_COMMAND.length()).trim();
        int fromIndex = eventDetails.indexOf(FROM_MARKER);
        int toIndex = eventDetails.indexOf(TO_MARKER);
        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
            throw new GoatBotException("Invalid event format. Please try again.");
        }

        String eventString = eventDetails.substring(0, fromIndex).trim();
        String eventStartTime = eventDetails
                .substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String eventEndTime = eventDetails.substring(toIndex + TO_MARKER.length()).trim();
        if (eventString.isEmpty() || eventStartTime.isEmpty() || eventEndTime.isEmpty()) {
            throw new GoatBotException("Invalid event format. Please try again.");
        }
        try {
            LocalDateTime from = LocalDateTime.parse(eventStartTime, DATE_TIME_INPUT_FORMAT);
            LocalDateTime to = LocalDateTime.parse(eventEndTime, DATE_TIME_INPUT_FORMAT);
            if (to.isBefore(from)) {
                throw new GoatBotException(
                        "Event end time cannot be before its start time."
                );
            }
            return new Event(eventString, from, to);
        } catch (DateTimeParseException e) {
            throw new GoatBotException(
                    "Invalid date. Use d/M/yyyy HHmm, e.g. 2/12/2019 1800."
            );
        }
    }

    /**
     * Parses user input into a todo task.
     *
     * @param userInput user command that describes the todo
     * @return todo task represented by the command
     * @throws GoatBotException if the todo description is empty
     */
    public static Todo parseTodo(String userInput) throws GoatBotException {
        String todoString = userInput.substring(TODO_COMMAND.length()).trim();
        if (todoString.isEmpty()) {
            throw new GoatBotException("Todo Format: 'todo xxx'");
        }
        return new Todo(todoString);
    }

    /**
     * Parses the keyword supplied with a find command.
     *
     * @param userInput complete find command
     * @return non-empty keyword to search for
     * @throws GoatBotException if the keyword is missing
     */
    public static String parseFindKeyword(String userInput) throws GoatBotException {
        String keyword = userInput.substring(FIND_COMMAND.length()).trim();
        if (keyword.isEmpty()) {
            throw new GoatBotException("find format: 'find xxx'");
        }
        return keyword;
    }

    /**
     * Parses and validates the task number supplied with a task command.
     *
     * @param userInput complete command entered by the user
     * @param command command word preceding the task number
     * @param taskCounter number of tasks currently stored
     * @return valid one-based task number
     * @throws GoatBotException if the task number is missing, invalid, or out of range
     */
    public static int parseTaskNumber(String userInput, String command, int taskCounter)
            throws GoatBotException {
        try {
            int taskNumber = Integer.parseInt(userInput.substring(command.length()).trim());
            if (taskNumber < 1 || taskNumber > taskCounter) {
                throw new GoatBotException(INVALID_INPUT_MESSAGE);
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new GoatBotException(INVALID_INPUT_MESSAGE);
        }
    }

    /**
     * Parses the date supplied with an on command.
     *
     * @param userInput complete on command
     * @return parsed search date
     * @throws GoatBotException if the date is missing or invalid
     */
    public static LocalDate parseSearchDate(String userInput)
            throws GoatBotException {
        String dateText = userInput.substring("on".length()).trim();

        try {
            return LocalDate.parse(dateText, DATE_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new GoatBotException(
                    "Invalid date. Use d/M/yyyy, e.g. 2/12/2019."
            );
        }
    }
}

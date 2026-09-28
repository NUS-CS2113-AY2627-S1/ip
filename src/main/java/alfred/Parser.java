package alfred;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import alfred.command.AddCommand;
import alfred.command.Command;
import alfred.command.DeleteCommand;
import alfred.command.ExitCommand;
import alfred.command.FindCommand;
import alfred.command.ListCommand;
import alfred.command.MarkCommand;
import alfred.command.ScheduleCommand;
import alfred.command.UnmarkCommand;
import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.ScheduledDateTime;
import alfred.task.Task;
import alfred.task.Todo;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public class Parser {

    /**
     * Parses user input into an executable command.
     *
     * @param fullCommand Full command entered by the user.
     * @return Command represented by the user input.
     * @throws AlfredException If the command or its arguments are invalid.
     */
    public Command parse(String fullCommand) throws AlfredException {
        String command = fullCommand.trim();
        if (command.equals("bye")) {
            return new ExitCommand();
        } else if (command.equals("list")) {
            return new ListCommand();
        } else if (isCommand(command, "delete")) {
            return new DeleteCommand(parseDeleteTaskNumber(command));
        } else if (isCommand(command, "unmark")) {
            return new UnmarkCommand(parseStatusTaskNumber(command, false));
        } else if (isCommand(command, "mark")) {
            return new MarkCommand(parseStatusTaskNumber(command, true));
        } else if (isCommand(command, "schedule")) {
            return new ScheduleCommand(parseScheduleDate(command));
        } else if (isCommand(command, "find")) {
            return new FindCommand(parseFindSearchText(command));
        } else if (isCommand(command, "todo")) {
            return new AddCommand(parseTodo(command));
        } else if (isCommand(command, "deadline")) {
            return new AddCommand(parseDeadline(command));
        } else if (isCommand(command, "event")) {
            return new AddCommand(parseEvent(command));
        }
        throw new AlfredException("Pardon me Master Wayne, I did not quite get that.");
    }

    /**
     * Parses the task number from a delete command.
     */
    private int parseDeleteTaskNumber(String command) throws AlfredException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length != 2) {
            throw new AlfredException("Please provide a task number to delete.");
        }

        return parseTaskNumber(parts[1]);
    }

    /**
     * Parses the task number from a mark or unmark command.
     */
    private int parseStatusTaskNumber(String command, boolean isMarkCommand) throws AlfredException {
        String[] parts = command.trim().split("\\s+");
        validateStatusTaskNumberArguments(parts, isMarkCommand);

        return parseTaskNumber(parts[1]);
    }

    /**
     * Parses a numeric task-number argument.
     */
    private int parseTaskNumber(String taskNumber) throws AlfredException {
        try {
            return Integer.parseInt(taskNumber);
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Parses the date from a schedule command.
     */
    private LocalDate parseScheduleDate(String command) throws AlfredException {
        String[] parts = command.split("\\s+");
        if (parts.length < 2) {
            throw createScheduleException("the date is missing");
        }
        if (parts.length > 2) {
            throw createScheduleException("only one date can be provided");
        }

        try {
            return LocalDate.parse(parts[1]);
        } catch (DateTimeParseException exception) {
            throw createScheduleException("the date must use yyyy-MM-dd");
        }
    }

    /**
     * Parses the search text from a find command.
     */
    private String parseFindSearchText(String command) throws AlfredException {
        String searchText = command.substring("find".length()).trim();
        if (searchText.isEmpty()) {
            throw new AlfredException("Please provide a keyword using this format: find <keyword>.");
        }
        return searchText;
    }

    /**
     * Returns whether the text is the specified command or starts with that command and an argument.
     */
    private boolean isCommand(String text, String commandWord) {
        return text.equals(commandWord) || text.startsWith(commandWord + " ");
    }

    /**
     * Validates the number of arguments supplied to a mark or unmark command.
     */
    private void validateStatusTaskNumberArguments(String[] parts, boolean isMarkCommand)
            throws AlfredException {
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
        String deadlineText = arguments.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw createDeadlineException("the task description is missing");
        }
        if (deadlineText.isEmpty()) {
            throw createDeadlineException("the deadline date and optional time are missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (deadlineText.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the deadline date or time cannot contain \""
                    + Storage.DELIMITER + "\"");
        }

        try {
            return new Deadline(description, ScheduledDateTime.parseUserInput(deadlineText));
        } catch (DateTimeParseException exception) {
            throw createDeadlineException("the date and optional time are invalid");
        }
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
        String startText = arguments.substring(fromPosition + "/from".length(), toPosition).trim();
        String endText = arguments.substring(toPosition + "/to".length()).trim();
        if (description.isEmpty()) {
            throw createEventException("the task description is missing");
        }
        if (startText.isEmpty()) {
            throw createEventException("the start date and optional time are missing");
        }
        if (endText.isEmpty()) {
            throw createEventException("the end date and optional time are missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createEventException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (startText.contains(Storage.DELIMITER)) {
            throw createEventException("the start date or time cannot contain \""
                    + Storage.DELIMITER + "\"");
        }
        if (endText.contains(Storage.DELIMITER)) {
            throw createEventException("the end date or time cannot contain \""
                    + Storage.DELIMITER + "\"");
        }

        try {
            ScheduledDateTime start = ScheduledDateTime.parseUserInput(startText);
            ScheduledDateTime end = ScheduledDateTime.parseUserInput(endText);
            if (start.hasTime() != end.hasTime()) {
                throw createEventException("both endpoints must either include a time or omit it");
            }
            if (end.isBefore(start)) {
                throw createEventException("the end cannot be before the start");
            }
            return new Event(description, start, end);
        } catch (DateTimeParseException exception) {
            throw createEventException("the dates and optional times are invalid");
        }
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
                + "Use either: deadline <description> /by <yyyy-MM-dd>\n"
                + "        or: deadline <description> /by <yyyy-MM-dd HH:mm>\n"
                + "Examples: deadline return book /by 2019-10-15\n"
                + "          deadline submit report /by 2019-10-15 17:30");
    }

    /**
     * Creates a detailed schedule error with the required command format.
     */
    private AlfredException createScheduleException(String reason) {
        return new AlfredException("Unable to show the schedule: " + reason + ".\n"
                + "Use this format: schedule <yyyy-MM-dd>\n"
                + "Example: schedule 2019-10-15");
    }

    /**
     * Creates a detailed event error with the required command format.
     */
    private AlfredException createEventException(String reason) {
        return new AlfredException("Unable to add the event: " + reason + ".\n"
                + "Use either: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>\n"
                + "        or: event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>\n"
                + "Examples: event conference /from 2019-10-15 /to 2019-10-16\n"
                + "          event meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00");
    }
}

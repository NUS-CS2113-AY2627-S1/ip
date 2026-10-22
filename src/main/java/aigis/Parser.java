package aigis;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import aigis.task.Deadline;
import aigis.task.Event;
import aigis.task.Task;
import aigis.task.Todo;

/**
 * Interprets user input and creates commands for Aigis to execute.
 */
public class Parser {
    private static final String LIST_COMMAND = "list";
    private static final String FIND_PREFIX = "find ";
    private static final String BYE_COMMAND = "bye";
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String BY_MARKER = "/by";
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";
    private static final String DEADLINE_USAGE = "Please use: deadline <description> /by <date>";
    private static final String EVENT_USAGE = "Please use: event <description> /from <start> /to <end>";
    private static final String UNKNOWN_COMMAND_MESSAGE = "I don't understand that command.";
    /**
     * Parses a line of user input into an executable command.
     *
     * @param input The input line to parse.
     * @return The command represented by the input.
     */
    public Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new MessageCommand(UNKNOWN_COMMAND_MESSAGE);
        }
        if (input.equals(BYE_COMMAND)) {
            return new ExitCommand();
        }
        if (input.equals(LIST_COMMAND)) {
            return new ListCommand();
        }
        if (hasCommandPrefix(input, FIND_PREFIX)) {
            return parseFind(input);
        }
        if (hasCommandPrefix(input, MARK_PREFIX)) {
            return parseTaskNumber(input, MARK_PREFIX);
        }
        if (hasCommandPrefix(input, UNMARK_PREFIX)) {
            return parseTaskNumber(input, UNMARK_PREFIX);
        }
        if (hasCommandPrefix(input, DELETE_PREFIX)) {
            return parseTaskNumber(input, DELETE_PREFIX);
        }
        if (hasCommandPrefix(input, TODO_PREFIX)) {
            return parseTodo(input);
        }
        if (hasCommandPrefix(input, DEADLINE_PREFIX)) {
            return parseDeadline(input);
        }
        if (hasCommandPrefix(input, EVENT_PREFIX)) {
            return parseEvent(input);
        }
        return new MessageCommand(UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Checks whether a user input starts with the required command prefix.
     *
     * @param input The full command string.
     * @param prefix The expected command prefix.
     * @return {@code true} if the command begins with the given prefix.
     */
    private static boolean hasCommandPrefix(String input, String prefix) {
        return input.startsWith(prefix);
    }

    /**
     * Finds the next occurrence of the given marker after the provided index.
     *
     * @param input The command text being scanned.
     * @param marker The marker to locate.
     * @param startIndex The index where the search begins.
     * @return The position of the marker, or {@code -1} if it is not found.
     */
    private static int findNextMarker(String input, String marker, int startIndex) {
        return input.indexOf(marker, startIndex);
    }

    /**
     * Parses a find command and extracts its search keyword.
     *
     * @param input The complete find command.
     * @return The parsed find command or an invalid command with an explanation.
     */
    private Command parseFind(String input) {
        String keyword = input.substring(FIND_PREFIX.length()).trim();
        if (keyword.isEmpty()) {
            return new MessageCommand("Please provide a search keyword.");
        }
        return new FindCommand(keyword);
    }

    /**
     * Parses the task number in a status or delete command.
     *
     * @param input The complete command input.
     * @param commandPrefix The prefix before the task number.
     * @return The parsed command or an invalid command with an explanation.
     */
    private Command parseTaskNumber(String input, String commandPrefix) {
        String taskNumber = input.substring(commandPrefix.length()).trim();
        try {
            int number = Integer.parseInt(taskNumber);
            if (commandPrefix.equals(MARK_PREFIX)) {
                return new MarkCommand(number);
            }
            if (commandPrefix.equals(UNMARK_PREFIX)) {
                return new UnmarkCommand(number);
            }
            return new DeleteCommand(number);
        } catch (NumberFormatException exception) {
            return new MessageCommand("Please provide a valid task number.");
        }
    }

    /**
     * Parses a todo command and constructs its task.
     *
     * @param input The complete todo command.
     * @return The parsed add command.
     */
    private Command parseTodo(String input) {
        String description = input.substring(TODO_PREFIX.length()).trim();
        try {
            Task task = new Todo(description);
            return new AddCommand(task, "New objective: " + description);
        } catch (IllegalArgumentException exception) {
            return new AddCommand(null, exception.getMessage());
        }
    }

    /**
     * Parses a deadline command and constructs its task.
     *
     * @param input The complete deadline command.
     * @return The parsed add command.
     */
    private Command parseDeadline(String input) {
        String deadlineInput = input.substring(DEADLINE_PREFIX.length());
        int byIndex = deadlineInput.indexOf(BY_MARKER);
        int secondByIndex = findNextMarker(deadlineInput, BY_MARKER, byIndex + BY_MARKER.length());
        if (byIndex <= 0 || secondByIndex >= 0) {
            return new AddCommand(null, DEADLINE_USAGE);
        }

        String description = deadlineInput.substring(0, byIndex).trim();
        String due = deadlineInput.substring(byIndex + BY_MARKER.length()).trim();
        if (description.isEmpty() || due.isEmpty()) {
            return new AddCommand(null, DEADLINE_USAGE);
        }

        return buildDeadlineCommand(description, due);
    }

    /**
     * Builds a deadline command when the user input is valid.
     *
     * @param description The task description.
     * @param due The due date text entered by the user.
     * @return The parsed command, or a validation error command.
     */
    private Command buildDeadlineCommand(String description, String due) {
        try {
            LocalDate dueDate = DateFormats.parseDate(due);
            Task task = new Deadline(description, dueDate);
            String message = "New objective: " + description + " ( by: "
                    + DateFormats.formatDate(dueDate) + " )";
            return new AddCommand(task, message);
        } catch (DateTimeParseException exception) {
            return new AddCommand(null, DEADLINE_USAGE);
        } catch (IllegalArgumentException exception) {
            return new AddCommand(null, exception.getMessage());
        }
    }

    /**
     * Parses an event command and constructs its task.
     *
     * @param input The complete event command.
     * @return The parsed add command.
     */
    private Command parseEvent(String input) {
        String eventInput = input.substring(EVENT_PREFIX.length());
        int fromIndex = eventInput.indexOf(FROM_MARKER);
        int toIndex = eventInput.indexOf(TO_MARKER);
        int secondFromIndex = findNextMarker(eventInput, FROM_MARKER, fromIndex + FROM_MARKER.length());
        int secondToIndex = findNextMarker(eventInput, TO_MARKER, toIndex + TO_MARKER.length());
        if (fromIndex <= 0 || toIndex <= fromIndex + FROM_MARKER.length()
                || secondFromIndex >= 0 || secondToIndex >= 0) {
            return new AddCommand(null, EVENT_USAGE);
        }

        String description = eventInput.substring(0, fromIndex).trim();
        String from = eventInput.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String till = eventInput.substring(toIndex + TO_MARKER.length()).trim();
        if (description.isEmpty() || from.isEmpty() || till.isEmpty()) {
            return new AddCommand(null, EVENT_USAGE);
        }

        return buildEventCommand(description, from, till);
    }

    /**
     * Builds an event command when the user input is valid.
     *
     * @param description The task description.
     * @param from The event start time.
     * @param till The event end time.
     * @return The parsed command, or a validation error command.
     */
    private Command buildEventCommand(String description, String from, String till) {
        try {
            Task task = new Event(description, from, till);
            String message = "New objective: " + description
                    + "( from: " + from + " to: " + till + " )";
            return new AddCommand(task, message);
        } catch (IllegalArgumentException exception) {
            return new AddCommand(null, exception.getMessage());
        }
    }
}

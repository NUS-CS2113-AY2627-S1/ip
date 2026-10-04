package luke.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import luke.command.AddCommand;
import luke.command.Command;
import luke.command.DeleteCommand;
import luke.command.ExitCommand;
import luke.command.FindCommand;
import luke.command.ListCommand;
import luke.command.MarkCommand;
import luke.command.UnmarkCommand;
import luke.exception.LukeException;
import luke.task.Deadline;
import luke.task.Event;
import luke.task.Todo;
import luke.util.DateTimeUtil;

/**
 * Parses user commands into task objects and command parameters.
 */
public class Parser {
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_EXIT = "bye";

    private static final String COMMAND_SEPARATOR = " ";
    private static final String DEADLINE_SEPARATOR = "/by";
    private static final String EVENT_FROM_SEPARATOR = "/from";
    private static final String EVENT_TO_SEPARATOR = "/to";

    private static final int SPLIT_LIMIT = 2;

    private Parser() {
    }

    /**
     * Parses user input into a command that Luke can execute.
     *
     * @param input User input.
     * @return Parsed command.
     * @throws LukeException If the command or its arguments are invalid.
     */
    public static Command parse(String input) throws LukeException {
        if (input.equals(COMMAND_EXIT)) {
            return new ExitCommand();
        }

        String commandWord = parseCommandWord(input);

        switch (commandWord) {
        case COMMAND_LIST:
            return new ListCommand();
        case COMMAND_TODO:
            return new AddCommand(parseTodo(input));
        case COMMAND_DEADLINE:
            return new AddCommand(parseDeadline(input));
        case COMMAND_EVENT:
            return new AddCommand(parseEvent(input));
        case COMMAND_MARK:
            return new MarkCommand(parseTaskNumber(input, COMMAND_MARK));
        case COMMAND_UNMARK:
            return new UnmarkCommand(parseTaskNumber(input, COMMAND_UNMARK));
        case COMMAND_DELETE:
            return new DeleteCommand(parseTaskNumber(input, COMMAND_DELETE));
        case COMMAND_FIND:
            return new FindCommand(parseFindKeyword(input));
        default:
            throw new LukeException("Sorry, your command is unrecognized.");
        }
    }

    /**
     * Extracts the command word from a user command.
     *
     * @param command User command.
     * @return Command word.
     * @throws LukeException If the command is empty.
     */
    public static String parseCommandWord(String command) throws LukeException {
        String trimmedCommand = command.trim();

        if (trimmedCommand.isEmpty()) {
            throw new LukeException("Please enter a command.");
        }

        return trimmedCommand.split(COMMAND_SEPARATOR, SPLIT_LIMIT)[0];
    }

    /**
     * Parses a todo command.
     *
     * @param command User command.
     * @return Parsed todo.
     * @throws LukeException If the todo description is empty.
     */
    public static Todo parseTodo(String command) throws LukeException {
        String description = command.substring(COMMAND_TODO.length()).trim();

        if (description.isEmpty()) {
            throw new LukeException(
                    "The description of a todo cannot be empty.");
        }

        return new Todo(description);
    }

    /**
     * Parses a deadline command.
     *
     * @param command User command.
     * @return Parsed deadline.
     * @throws LukeException If the description or deadline is missing.
     */
    public static Deadline parseDeadline(String command) throws LukeException {
        String commandDetails = command.substring(
                COMMAND_DEADLINE.length()).trim();

        if (commandDetails.isEmpty()) {
            throw new LukeException(
                    "The description of a deadline cannot be empty.");
        }

        String[] deadlineParts = commandDetails.split(
                DEADLINE_SEPARATOR, SPLIT_LIMIT);

        if (deadlineParts.length < SPLIT_LIMIT || deadlineParts[1].trim().isEmpty()) {
            throw new LukeException(
                    "The deadline must include a /by date or time.");
        }

        String description = deadlineParts[0].trim();
        String byText = deadlineParts[1].trim();

        if (description.isEmpty()) {
            throw new LukeException(
                    "The description of a deadline cannot be empty.");
        }

        try {
            LocalDate by = DateTimeUtil.parseDate(byText);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new LukeException(
                    "The deadline date must be in yyyy-MM-dd format.");
        }
    }

    /**
     * Parses an event command.
     *
     * @param command User command.
     * @return Parsed event.
     * @throws LukeException If the description, start time, or end time is missing.
     */
    public static Event parseEvent(String command) throws LukeException {
        String commandDetails = command.substring(
                COMMAND_EVENT.length()).trim();

        if (commandDetails.isEmpty()) {
            throw new LukeException(
                    "The description of an event cannot be empty.");
        }

        String[] eventParts = commandDetails.split(
                EVENT_FROM_SEPARATOR, SPLIT_LIMIT);

        if (eventParts.length < SPLIT_LIMIT) {
            throw new LukeException(
                    "The event must include a /from start time.");
        }

        String description = eventParts[0].trim();
        String eventTimeDetails = eventParts[1].trim();

        if (description.isEmpty()) {
            throw new LukeException(
                    "The description of an event cannot be empty.");
        }

        String[] timeParts = eventTimeDetails.split(
                EVENT_TO_SEPARATOR, SPLIT_LIMIT);

        if (timeParts.length < SPLIT_LIMIT) {
            throw new LukeException(
                    "The event must include a /to end time.");
        }

        String fromText = timeParts[0].trim();
        String toText = timeParts[1].trim();

        if (fromText.isEmpty()) {
            throw new LukeException(
                    "The event start time cannot be empty.");
        }

        if (toText.isEmpty()) {
            throw new LukeException(
                    "The event end time cannot be empty.");
        }

        try {
            LocalDateTime from = DateTimeUtil.parseDateTime(fromText);
            LocalDateTime to = DateTimeUtil.parseDateTime(toText);

            if (!to.isAfter(from)) {
                throw new LukeException(
                        "The event end time must be after its start time.");
            }

            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            throw new LukeException(
                    "The event date and time must be in yyyy-MM-dd HH:mm format.");
        }
    }

    /**
     * Extracts a task number from a command.
     *
     * @param command User command.
     * @param commandWord Command word, such as {@code mark} or {@code unmark}.
     * @return Parsed task number.
     * @throws LukeException If the task number is missing or is not a valid integer.
     */
    public static int parseTaskNumber(String command, String commandWord)
            throws LukeException {
        String taskNumberText = command.substring(
                commandWord.length()).trim();

        if (taskNumberText.isEmpty()) {
            throw new LukeException(
                    "Please specify a task number.");
        }

        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new LukeException(
                    "The task number must be a valid number.");
        }
    }

    /**
     * Extracts the search keyword from a find command.
     *
     * @param command User command.
     * @return Search keyword.
     * @throws LukeException If the keyword is empty.
     */
    private static String parseFindKeyword(String command) throws LukeException {
        String keyword = command.substring(COMMAND_FIND.length()).trim();

        if (keyword.isEmpty()) {
            throw new LukeException("Please specify a search keyword.");
        }

        return keyword;
    }
}

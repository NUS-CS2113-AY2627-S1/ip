package wenwen.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import wenwen.command.AddCommand;
import wenwen.command.Command;
import wenwen.command.DeleteCommand;
import wenwen.command.ExitCommand;
import wenwen.command.FindCommand;
import wenwen.command.ListCommand;
import wenwen.command.MarkCommand;
import wenwen.command.OnDateCommand;
import wenwen.exception.WenwenException;
import wenwen.task.Deadline;
import wenwen.task.Event;
import wenwen.task.Todo;

/**
 * Converts raw user input into executable commands.
 */
public final class Parser {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final String BY_SEPARATOR = " /by ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses one complete user command.
     *
     * @param fullCommand command entered by the user
     * @return command that represents the requested action
     * @throws WenwenException if the command is unknown or malformed
     */
    public static Command parse(String fullCommand) throws WenwenException {
        String input = fullCommand.trim();
        String commandWord = getCommandWord(input);

        switch (commandWord) {
            case "bye":
                ensureNoDetails(input, commandWord);
                return new ExitCommand();
            case "list":
                ensureNoDetails(input, commandWord);
                return new ListCommand();
            case "mark":
                return new MarkCommand(parseTaskNumber(input, commandWord), true);
            case "unmark":
                return new MarkCommand(parseTaskNumber(input, commandWord), false);
            case "delete":
                return new DeleteCommand(parseTaskNumber(input, commandWord));
            case "on":
                return new OnDateCommand(parseDate(getCommandDetails(input, commandWord)));
            case "find":
                return parseFind(input);
            case "todo":
                return parseTodo(input);
            case "deadline":
                return parseDeadline(input);
            case "event":
                return parseEvent(input);
            default:
                throw new WenwenException("Sorry, I don't know that command yet.");
        }
    }

    /**
     * Parses a todo command and its required description.
     *
     * @param input complete todo command
     * @return command that adds the todo
     * @throws WenwenException if the description is empty
     */
    private static Command parseTodo(String input) throws WenwenException {
        String description = getCommandDetails(input, "todo");
        ensureNotEmpty(description, "The description of a todo cannot be empty.");
        return new AddCommand(new Todo(description));
    }

    /**
     * Parses a find command and its required keyword.
     *
     * @param input complete find command
     * @return command that searches task descriptions
     * @throws WenwenException if the keyword is empty
     */
    private static Command parseFind(String input) throws WenwenException {
        String keyword = getCommandDetails(input, "find");
        ensureNotEmpty(keyword, "Please give me a keyword to find.");
        return new FindCommand(keyword);
    }

    /**
     * Parses a deadline command, including its description and due date.
     *
     * @param input complete deadline command
     * @return command that adds the deadline
     * @throws WenwenException if required details are missing or invalid
     */
    private static Command parseDeadline(String input) throws WenwenException {
        int byIndex = input.indexOf(BY_SEPARATOR);
        if (byIndex < 0) {
            throw new WenwenException("Please use: deadline DESCRIPTION /by DATE_OR_TIME");
        }

        String description = input.substring("deadline".length(), byIndex).trim();
        String byText = input.substring(byIndex + BY_SEPARATOR.length()).trim();
        ensureNotEmpty(description, "The description of a deadline cannot be empty.");
        ensureNotEmpty(byText, "The deadline needs a date after /by.");
        return new AddCommand(new Deadline(description, parseDate(byText)));
    }

    /**
     * Parses an event command, including its description and time range.
     *
     * @param input complete event command
     * @return command that adds the event
     * @throws WenwenException if required details are missing or invalid
     */
    private static Command parseEvent(String input) throws WenwenException {
        int fromIndex = input.indexOf(FROM_SEPARATOR);
        int toIndex = input.indexOf(TO_SEPARATOR);
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new WenwenException("Please use: event DESCRIPTION /from START /to END");
        }

        String description = input.substring("event".length(), fromIndex).trim();
        String fromText = input.substring(fromIndex + FROM_SEPARATOR.length(), toIndex).trim();
        String toText = input.substring(toIndex + TO_SEPARATOR.length()).trim();
        ensureNotEmpty(description, "The description of an event cannot be empty.");
        ensureNotEmpty(fromText, "The event needs a start date and time after /from.");
        ensureNotEmpty(toText, "The event needs an end date and time after /to.");
        LocalDateTime from = parseDateTime(fromText);
        LocalDateTime to = parseDateTime(toText);
        if (to.isBefore(from)) {
            throw new WenwenException("The event end must not be before its start.");
        }
        return new AddCommand(new Event(description, from, to));
    }

    /**
     * Parses the one-based task number following a command word.
     *
     * @param input complete command
     * @param commandWord command that requires a task number
     * @return parsed task number
     * @throws WenwenException if the number is missing or invalid
     */
    private static int parseTaskNumber(String input, String commandWord) throws WenwenException {
        String taskNumberText = getCommandDetails(input, commandWord);
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new WenwenException("Please give me a valid task number for '" + commandWord + "'.");
        }
    }

    /**
     * Parses a strict ISO-style calendar date.
     *
     * @param text date in yyyy-MM-dd format
     * @return parsed date
     * @throws WenwenException if the date is invalid
     */
    private static LocalDate parseDate(String text) throws WenwenException {
        try {
            return LocalDate.parse(text, DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new WenwenException("Please use a valid date in yyyy-MM-dd format.");
        }
    }

    /**
     * Parses a strict date and 24-hour time.
     *
     * @param text date and time in yyyy-MM-dd HHmm format
     * @return parsed date and time
     * @throws WenwenException if the date or time is invalid
     */
    private static LocalDateTime parseDateTime(String text) throws WenwenException {
        try {
            return LocalDateTime.parse(text, DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new WenwenException("Please use a valid date and time in yyyy-MM-dd HHmm format.");
        }
    }

    /**
     * Returns the first space-delimited word in a command.
     *
     * @param input complete command
     * @return command word
     */
    private static String getCommandWord(String input) {
        int firstSpace = input.indexOf(' ');
        return firstSpace < 0 ? input : input.substring(0, firstSpace);
    }

    /**
     * Returns the trimmed text following a command word.
     *
     * @param input complete command
     * @param commandWord command word at the start of the input
     * @return command details, or an empty string when there are none
     */
    private static String getCommandDetails(String input, String commandWord) {
        if (input.length() == commandWord.length()) {
            return "";
        }
        return input.substring(commandWord.length()).trim();
    }

    /**
     * Rejects extra text after a command that takes no details.
     *
     * @param input complete command
     * @param commandWord command that should stand alone
     * @throws WenwenException if extra text follows the command word
     */
    private static void ensureNoDetails(String input, String commandWord) throws WenwenException {
        if (!getCommandDetails(input, commandWord).isEmpty()) {
            throw new WenwenException("The '" + commandWord + "' command does not take extra details.");
        }
    }

    /**
     * Rejects a required command field when it contains no text.
     *
     * @param text field to validate
     * @param message error message to show for an empty field
     * @throws WenwenException if the field is empty
     */
    private static void ensureNotEmpty(String text, String message) throws WenwenException {
        if (text.isEmpty()) {
            throw new WenwenException(message);
        }
    }
}

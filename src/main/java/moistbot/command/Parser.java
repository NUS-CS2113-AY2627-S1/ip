package moistbot.command;

import moistbot.exception.MoistBotException;
import moistbot.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into a {@link Command} object understood by MoistBot.
 * The parser normalizes whitespace, identifies command keywords, and validates
 * required arguments before constructing the corresponding command object.
 */
public final class Parser {
    private static final String DEADLINE_USAGE = "deadline <desc> /by <yyyy-MM-dd> [HHmm]";
    private static final String EVENT_USAGE = "event <desc> /from <date> [HHmm] /to <date> [HHmm]";
    private static final String DEADLINE_SEPARATOR = "/by";
    private static final String EVENT_FROM_SEPARATOR = "/from";
    private static final String EVENT_TO_SEPARATOR = "/to";
    private static final String INPUT_ERROR = "Please enter a command, such as 'list' or 'todo buy milk'.";
    private static final String UNKNOWN_COMMAND = "My apologies, but I do not recognise the command '%s'. "
            + "Available commands are: bye, list, find, todo, deadline, event, mark, unmark, and delete.";

    /**
     * Prevents instantiation because parsing operations do not require object state.
     */
    private Parser() {
    }

    /**
     * Parses a user-entered line into a command object.
     * Recognizes command keywords (bye, list, find, mark, unmark, delete, todo, deadline, event)
     * and delegates to specialized parsing methods for complex commands.
     *
     * @param inputString The raw text entered by the user
     * @return A parsed command object
     * @throws MoistBotException if the input is blank, has an unknown command,
     *                                  is missing required arguments, or is malformed
     */
    public static Command parseInput(String inputString) throws MoistBotException {
        String trimmedInput = validateInput(inputString);
        String[] inputArray = trimmedInput.split("\\s+", 2);
        String commandText = inputArray[0];

        return parseCommand(commandText, inputArray);
    }

    /**
     * Validates that the input string is not null or empty.
     *
     * @param inputString The raw text to validate
     * @return The trimmed input string
     * @throws MoistBotException if the input is null or blank
     */
    private static String validateInput(String inputString) throws MoistBotException {
        if (inputString == null) {
            throw new MoistBotException(INPUT_ERROR);
        }

        String trimmedInput = inputString.trim();
        if (trimmedInput.isEmpty()) {
            throw new MoistBotException(INPUT_ERROR);
        }

        return trimmedInput;
    }

    /**
     * Parses a command based on its keyword and arguments.
     *
     * @param commandText The command keyword
     * @param inputArray The array containing the command and its arguments
     * @return A parsed command object
     * @throws MoistBotException if the command arguments are invalid
     */
    private static Command parseCommand(String commandText, String[] inputArray) throws MoistBotException {
        switch (commandText) {
            case "bye":
                return parseCommandWithoutArguments("bye", inputArray);
            case "list":
                return parseList(inputArray);
            case "find":
                return parseFind(inputArray);
            case "mark":
                return parseTaskNumber("mark", inputArray);
            case "unmark":
                return parseTaskNumber("unmark", inputArray);
            case "delete":
                return parseTaskNumber("delete", inputArray);
            case "todo":
                return parseTodo(inputArray);
            case "deadline":
                return parseDeadlineCommand(inputArray);
            case "event":
                return parseEventCommand(inputArray);
            default:
                throw new MoistBotException(String.format(UNKNOWN_COMMAND, commandText));
        }
    }

    /**
     * Parses a command that must not have arguments.
     *
     * @param commandName The command name to create
     * @param inputArray The array containing the command and any supplied text
     * @return A parsed command without arguments
     * @throws MoistBotException if unexpected text follows the command
     */
    private static Command parseCommandWithoutArguments(String commandName, String[] inputArray)
            throws MoistBotException {
        if (inputArray.length > 1) {
            throw new MoistBotException("The '" + commandName + "' command does not accept arguments. Please "
                    + "enter only '" + commandName + "'.");
        }
        if (commandName.equals("bye")) {
            return new ExitCommand();
        }
        return new ListCommand();
    }

    /**
     * Parses an unfiltered list command or one with an inclusive cutoff date.
     */
    private static Command parseList(String[] inputArray) throws MoistBotException {
        if (inputArray.length == 1) {
            return new ListCommand();
        }
        try {
            LocalDate cutoffDate = DateTimeUtil.parseDate(inputArray[1]);
            return new ListCommand(cutoffDate);
        } catch (DateTimeParseException e) {
            throw new MoistBotException("Please enter a valid list date as yyyy-MM-dd or d/M/yyyy, for example "
                    + "'list 2019-12-03'.");
        }
    }

    /**
     * Parses a command that searches task descriptions for a required term.
     */
    private static Command parseFind(String[] inputArray) throws MoistBotException {
        if (inputArray.length < 2 || inputArray[1].trim().isEmpty()) {
            throw new MoistBotException("Please provide text to find. Usage: find <search term>, for example "
                    + "'find book'.");
        }
        return new FindCommand(inputArray[1].trim());
    }

    /**
     * Parses task commands that require an integer task number.
     *
     * @param commandName The command name (mark, unmark, or delete)
     * @param inputArray The array containing the command and its arguments
     * @return A parsed command containing a task number
     * @throws MoistBotException if the argument is missing or not an integer
     */
    private static Command parseTaskNumber(String commandName, String[] inputArray)
            throws MoistBotException {
        if (inputArray.length < 2) {
            throw new MoistBotException("Please provide a task number. Usage: " + commandName + " <task number>, "
                    + "for example '" + commandName + " 1'.");
        }
        try {
            int taskNumber = Integer.parseInt(inputArray[1]);
            return createTaskNumberCommand(commandName, taskNumber);
        } catch (NumberFormatException e) {
            throw new MoistBotException("My apologies, but '" + inputArray[1] + "' is not a valid task number. "
                    + "Please enter one whole number, for example '" + commandName + " 1'.");
        }
    }

    /**
     * Creates a task-number command after its shared numeric validation.
     *
     * @param commandName The command name
     * @param taskNumber The parsed 1-based task number
     * @return The concrete command for the supplied name
     * @throws MoistBotException if the command name is unsupported
     */
    private static Command createTaskNumberCommand(String commandName, int taskNumber) throws MoistBotException {
        switch (commandName) {
            case "mark":
                return new MarkCommand(taskNumber);
            case "unmark":
                return new UnmarkCommand(taskNumber);
            case "delete":
                return new DeleteCommand(taskNumber);
            default:
                throw new MoistBotException("My apologies, but that command is not supported.");
        }
    }

    /**
     * Parses a todo command.
     *
     * @param inputArray The array containing the command and its arguments
     * @return A parsed TODO command
     * @throws MoistBotException if the description is missing
     */
    private static Command parseTodo(String[] inputArray) throws MoistBotException {
        if (inputArray.length < 2 || inputArray[1].trim().isEmpty()) {
            throw new MoistBotException("Please provide a description for the todo task. Usage: todo <description>, "
                    + "for example 'todo buy milk'.");
        }
        return new TodoCommand(inputArray[1].trim());
    }

    /**
     * Parses a deadline command.
     *
     * @param inputArray The array containing the command and its arguments
     * @return A parsed DEADLINE command
     * @throws MoistBotException if the description is missing
     */
    private static Command parseDeadlineCommand(String[] inputArray) throws MoistBotException {
        if (inputArray.length < 2 || inputArray[1].trim().isEmpty()) {
            throw new MoistBotException("Please provide a deadline description and time. Usage: " + DEADLINE_USAGE
                    + ", for example 'deadline return book /by 2019-12-02 1800'.");
        }
        return parseDeadline(inputArray[1]);
    }

    /**
     * Parses an event command.
     *
     * @param inputArray The array containing the command and its arguments
     * @return A parsed EVENT command
     * @throws MoistBotException if the description is missing
     */
    private static Command parseEventCommand(String[] inputArray) throws MoistBotException {
        if (inputArray.length < 2 || inputArray[1].trim().isEmpty()) {
            throw new MoistBotException("Please provide an event description and dates. Usage: " + EVENT_USAGE
                    + ", for example 'event meeting /from 2019-12-02 1400 /to 2019-12-02 1600'.");
        }
        return parseEvent(inputArray[1]);
    }


    /**
     * Parses a deadline command string in the format "description /by deadline".
     *
     * @param inputString The deadline command arguments (without the "deadline" keyword)
     * @return A parsed DEADLINE command with description and deadline
     * @throws MoistBotException if the format is invalid or required fields are missing
     */
    public static Command parseDeadline(String inputString) throws MoistBotException {
        int separatorIndex = findSeparatorIndex(inputString, DEADLINE_SEPARATOR, 0);
        if (separatorIndex < 0) {
            throw new MoistBotException("Please include the '/by' separator. Usage: " + DEADLINE_USAGE + ".");
        }
        if (findSeparatorIndex(inputString, DEADLINE_SEPARATOR,
                separatorIndex + DEADLINE_SEPARATOR.length()) >= 0) {
            throw new MoistBotException("A deadline may contain only one '/by' separator. Usage: "
                    + DEADLINE_USAGE + ".");
        }

        String description = inputString.substring(0, separatorIndex).trim();
        String by = inputString.substring(separatorIndex + DEADLINE_SEPARATOR.length()).trim();

        if (description.isEmpty()) {
            throw new MoistBotException("Please provide a deadline description before '/by'. Usage: "
                    + DEADLINE_USAGE + ".");
        }
        if (by.isEmpty()) {
            throw new MoistBotException("Please provide a deadline time after '/by'. Usage: " + DEADLINE_USAGE + ".");
        }

        try {
            DateTimeUtil.ParsedDateTime deadline = DateTimeUtil.parse(by);
            return new DeadlineCommand(description, deadline.date(), deadline.time());
        } catch (DateTimeParseException e) {
            throw new MoistBotException("Please enter a valid deadline as yyyy-MM-dd or d/M/yyyy, with an "
                    + "optional 24-hour HHmm time, for example '2019-12-02 1800'.");
        }
    }

    /**
     * Parses an event command string in the format "description /from start /to end".
     *
     * @param inputString The event command arguments (without the "event" keyword)
     * @return A parsed EVENT command with description, start time, and end time
     * @throws MoistBotException if the format is invalid or required fields are missing
     */
    public static Command parseEvent(String inputString) throws MoistBotException {
        int fromIndex = findSeparatorIndex(inputString, EVENT_FROM_SEPARATOR, 0);
        int toIndex = findSeparatorIndex(inputString, EVENT_TO_SEPARATOR, 0);
        if (fromIndex < 0) {
            throw new MoistBotException("Please include the '/from' separator. Usage: " + EVENT_USAGE + ".");
        }
        if (toIndex < 0) {
            throw new MoistBotException("Please include the '/to' separator. Usage: " + EVENT_USAGE + ".");
        }
        if (fromIndex > toIndex) {
            throw new MoistBotException("Please place '/from' before '/to'. Usage: " + EVENT_USAGE + ".");
        }
        if (findSeparatorIndex(inputString, EVENT_FROM_SEPARATOR,
                fromIndex + EVENT_FROM_SEPARATOR.length()) >= 0
                || findSeparatorIndex(inputString, EVENT_TO_SEPARATOR,
                toIndex + EVENT_TO_SEPARATOR.length()) >= 0) {
            throw new MoistBotException("An event must contain exactly one '/from' and one '/to' separator. "
                    + "Usage: " + EVENT_USAGE + ".");
        }

        String description = inputString.substring(0, fromIndex).trim();
        String from = inputString.substring(fromIndex + EVENT_FROM_SEPARATOR.length(), toIndex).trim();
        String to = inputString.substring(toIndex + EVENT_TO_SEPARATOR.length()).trim();

        if (description.isEmpty()) {
            throw new MoistBotException("Please provide an event description before '/from'. Usage: "
                    + EVENT_USAGE + ".");
        }
        if (from.isEmpty()) {
            throw new MoistBotException("Please provide an event start date after '/from'. Usage: "
                    + EVENT_USAGE + ".");
        }
        if (to.isEmpty()) {
            throw new MoistBotException("Please provide an event end date after '/to'. Usage: " + EVENT_USAGE + ".");
        }

        DateTimeUtil.ParsedDateTime start = parseEventDateTime(from, "start");
        DateTimeUtil.ParsedDateTime end = parseEventDateTime(to, "end");
        validateEventRange(start, end);
        return new EventCommand(description, start.date(), start.time(), end.date(), end.time());
    }

    /**
     * Parses one event endpoint and provides an endpoint-specific correction.
     */
    private static DateTimeUtil.ParsedDateTime parseEventDateTime(String input, String endpoint)
            throws MoistBotException {
        try {
            return DateTimeUtil.parse(input);
        } catch (DateTimeParseException e) {
            throw new MoistBotException("Please enter a valid event " + endpoint + " as yyyy-MM-dd or d/M/yyyy, "
                    + "with an optional 24-hour HHmm time.");
        }
    }

    /**
     * Rejects ambiguous or backwards event ranges before they reach the task list.
     */
    private static void validateEventRange(DateTimeUtil.ParsedDateTime start, DateTimeUtil.ParsedDateTime end)
            throws MoistBotException {
        if ((start.time() == null) != (end.time() == null)) {
            throw new MoistBotException("Please provide times for both event endpoints, or omit both times.");
        }

        boolean hasBackwardsDates = start.date().isAfter(end.date());
        boolean hasBackwardsTimes = start.date().equals(end.date()) && start.time() != null
                && start.time().isAfter(end.time());
        if (hasBackwardsDates || hasBackwardsTimes) {
            throw new MoistBotException("Please ensure the event end is not before its start.");
        }
    }

    /**
     * Finds a separator only when it appears as a distinct whitespace-delimited token.
     * This prevents text such as "/bypass" from being mistaken for the "/by" separator.
     *
     * @param inputString The command arguments to search
     * @param separator The separator token to find
     * @param startIndex The index at which to begin searching
     * @return The separator index, or -1 if no complete separator token exists
     */
    private static int findSeparatorIndex(String inputString, String separator, int startIndex) {
        int separatorIndex = inputString.indexOf(separator, startIndex);
        while (separatorIndex >= 0) {
            int afterSeparator = separatorIndex + separator.length();
            boolean hasValidStart = separatorIndex == 0
                    || Character.isWhitespace(inputString.charAt(separatorIndex - 1));
            boolean hasValidEnd = afterSeparator == inputString.length()
                    || Character.isWhitespace(inputString.charAt(afterSeparator));
            if (hasValidStart && hasValidEnd) {
                return separatorIndex;
            }
            separatorIndex = inputString.indexOf(separator, separatorIndex + separator.length());
        }
        return -1;
    }
}

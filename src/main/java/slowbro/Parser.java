package slowbro;

/** Parses user input into structured commands. */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String TODO_COMMAND_PREFIX = "todo ";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline ";
    private static final String EVENT_COMMAND_PREFIX = "event ";
    private static final String DELETE_COMMAND_PREFIX = "delete ";
    private static final String FIND_COMMAND_PREFIX = "find ";
    private static final String BY_SEPARATOR = " /by ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";
    private static final String EMPTY_COMMAND_MESSAGE = " Please enter a command.";
    private static final String EMPTY_TASK_MESSAGE = " Task descriptions and details cannot be empty.";
    private static final String DEADLINE_USAGE_MESSAGE =
            " Usage: deadline <description> /by <date or time>.";
    private static final String EVENT_USAGE_MESSAGE =
            " Usage: event <description> /from <start> /to <end>.";
    private static final String COMMAND_USAGE_MESSAGE =
            " Available Commands: todo, deadline, event, list, mark, delete, find, bye.";
    private static final String DELETE_USAGE_MESSAGE = " Usage: delete <task number>";
    private static final String POSITIVE_TASK_NUMBER_MESSAGE =
            " Please provide a positive task number.";

    /** Represents the supported command types. */
    public enum CommandType { BYE, LIST, MARK, TODO, DEADLINE, EVENT, DELETE, FIND }

    /** Represents a parsed command and its arguments. */
    public record Command(CommandType type, String description, String firstDetail,
            String secondDetail, int taskIndex, boolean shouldUnmark) { }

    /** Returns whether the input is the exit command. */
    public static boolean isBye(String input) {
        return input != null && BYE_COMMAND.equals(input.trim());
    }

    /** Parses the given input into a command. */
    public static Command parse(String input) throws InvalidCommandException {
        if (input == null) {
            throw new InvalidCommandException(EMPTY_COMMAND_MESSAGE);
        }

        input = input.trim();
        if (input.isEmpty()) {
            throw new InvalidCommandException(EMPTY_COMMAND_MESSAGE);
        }
        if (isBye(input)) {
            return new Command(CommandType.BYE, null, null, null, -1, false);
        }
        if (input.equals(LIST_COMMAND)) {
            return new Command(CommandType.LIST, null, null, null, -1, false);
        }
        if (input.startsWith(MARK_COMMAND_PREFIX) || input.startsWith(UNMARK_COMMAND_PREFIX)) {
            return parseMark(input);
        }
        if (input.startsWith(TODO_COMMAND_PREFIX)) {
            return parseTodo(input);
        }
        if (input.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadline(input);
        }
        if (input.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEvent(input);
        }
        if (input.startsWith(DELETE_COMMAND_PREFIX)) {
            return parseDelete(input);
        }
        if (input.startsWith(FIND_COMMAND_PREFIX)) {
            return parseFind(input);
        }
        throw new InvalidCommandException(COMMAND_USAGE_MESSAGE);
    }

    /** Parses a mark or unmark command. */
    private static Command parseMark(String input) throws InvalidCommandException {
        boolean shouldUnmark = input.startsWith(UNMARK_COMMAND_PREFIX);
        String prefix = shouldUnmark ? UNMARK_COMMAND_PREFIX : MARK_COMMAND_PREFIX;
        int index = parseTaskIndex(input.substring(prefix.length()).trim());
        return new Command(CommandType.MARK, null, null, null, index, shouldUnmark);
    }

    /** Parses a todo command. */
    private static Command parseTodo(String input) throws InvalidCommandException {
        String description = input.substring(TODO_COMMAND_PREFIX.length()).trim();
        validateTaskFields(description);
        return new Command(CommandType.TODO, description, null, null, -1, false);
    }

    /** Parses a delete command. */
    private static Command parseDelete(String input) throws InvalidCommandException {
        String[] words = input.split("\\s+");
        if (words.length != 2) {
            throw new InvalidCommandException(DELETE_USAGE_MESSAGE);
        }
        int index = parseTaskIndex(words[1]);
        return new Command(CommandType.DELETE, null, null, null, index, false);
    }

    /** Parses a find command. */
    private static Command parseFind(String input) throws InvalidCommandException {
        String keyword = input.substring(FIND_COMMAND_PREFIX.length()).trim();
        validateTaskFields(keyword);
        return new Command(CommandType.FIND, keyword, null, null, -1, false);
    }

    /**
     * Parses a deadline command into a structured command.
     *
     * @param input the user input containing the deadline command
     * @return the parsed deadline command
     * @throws InvalidCommandException if the command format is invalid
     */
    private static Command parseDeadline(String input) throws InvalidCommandException {
        String command = input.substring(DEADLINE_COMMAND_PREFIX.length());
        int byIndex = command.indexOf(BY_SEPARATOR);
        if (byIndex < 0) {
            throw new InvalidCommandException(DEADLINE_USAGE_MESSAGE);
        }
        String description = command.substring(0, byIndex).trim();
        String by = command.substring(byIndex + BY_SEPARATOR.length()).trim();
        validateTaskFields(description, by);
        return new Command(CommandType.DEADLINE, description, by, null, -1, false);
    }

    /**
     * Parses an event command into a structured command.
     *
     * @param input the user input containing the event command
     * @return the parsed event command
     * @throws InvalidCommandException if the command format is invalid
     */
    private static Command parseEvent(String input) throws InvalidCommandException {
        String command = input.substring(EVENT_COMMAND_PREFIX.length());
        int fromIndex = command.indexOf(FROM_SEPARATOR);
        int toIndex = command.indexOf(TO_SEPARATOR);
        if (fromIndex < 0 || toIndex <= fromIndex) {
            throw new InvalidCommandException(EVENT_USAGE_MESSAGE);
        }
        String description = command.substring(0, fromIndex).trim();
        String from = command.substring(fromIndex + FROM_SEPARATOR.length(), toIndex).trim();
        String to = command.substring(toIndex + TO_SEPARATOR.length()).trim();
        validateTaskFields(description, from, to);
        return new Command(CommandType.EVENT, description, from, to, -1, false);
    }

    /**
     * Ensures that all command fields are non-empty.
     *
     * @param fields the command fields to validate
     * @throws InvalidCommandException if any field is empty
     */
    private static void validateTaskFields(String... fields) throws InvalidCommandException {
        for (String field : fields) {
            if (field.isEmpty()) {
                throw new InvalidCommandException(EMPTY_TASK_MESSAGE);
            }
        }
    }

    /** Converts a one-based task number into a zero-based index. */
    private static int parseTaskIndex(String taskNumber) throws InvalidCommandException {
        int parsedNumber = Integer.parseInt(taskNumber);
        if (parsedNumber <= 0) {
            throw new InvalidCommandException(POSITIVE_TASK_NUMBER_MESSAGE);
        }
        return parsedNumber - 1;
    }
}

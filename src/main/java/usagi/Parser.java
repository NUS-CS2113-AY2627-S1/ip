package usagi;

/**
 * Makes sense of the commands typed by the user.
 *
 * Each method turns raw text into something the rest of Usagi can use
 * directly, such as a command word, a task, or a task index, and rejects text
 * that does not fit the expected format with a message explaining the right
 * format. The methods are static because parsing needs no state of its own.
 */
public class Parser {
    /** Separates a deadline's description from its due date, e.g. "return book /by Sunday". */
    private static final String DEADLINE_BY_DELIMITER = " /by ";

    /** Separates an event's description from its start time. */
    private static final String EVENT_FROM_DELIMITER = " /from ";

    /** Separates an event's start time from its end time. */
    private static final String EVENT_TO_DELIMITER = " /to ";

    /** Reminder of the expected deadline format, shown whenever a deadline cannot be read. */
    private static final String DEADLINE_FORMAT_HINT =
            "hAA, HAAA->(A deadline needs a description and a due date, "
            + "e.g. \"deadline return book /by Sunday\".)";

    /** Reminder of the expected event format, shown whenever an event cannot be read. */
    private static final String EVENT_FORMAT_HINT =
            "eeeeeYAHA->(An event needs a description, a start and an end, "
            + "e.g. \"event project meeting /from Mon 2pm /to 4pm\".)";

    /**
     * Returns the first word of the input, which identifies the command.
     *
     * @param input Full line of input entered by the user.
     * @return The command word, or an empty string if the input is empty.
     */
    public static String getCommandWord(String input) {
        return input.split(" ", 2)[0];
    }

    /**
     * Returns everything after the command word.
     *
     * @param input Full line of input entered by the user.
     * @return The arguments, or an empty string if there are none.
     */
    public static String getCommandArguments(String input) {
        String[] parts = input.split(" ", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /**
     * Creates a todo from the text following the "todo" command word.
     *
     * @param arguments Text following the "todo" command word.
     * @return The todo described by the arguments.
     * @throws UsagiException If no description was given.
     */
    public static Todo parseTodo(String arguments) throws UsagiException {
        if (arguments.isEmpty()) {
            throw new UsagiException("Unana yaha->(A todo needs a description, e.g. \"todo borrow book\".)");
        }
        return new Todo(arguments);
    }

    /**
     * Creates a deadline from arguments of the form "description /by date".
     *
     * @param arguments Text following the "deadline" command word.
     * @return The deadline described by the arguments.
     * @throws UsagiException If the "/by" separator is missing, or either the
     *                       description or the due date is empty.
     */
    public static Deadline parseDeadline(String arguments) throws UsagiException {
        String[] parts = arguments.split(DEADLINE_BY_DELIMITER, 2);
        if (parts.length < 2) {
            throw new UsagiException(DEADLINE_FORMAT_HINT);
        }

        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new UsagiException(DEADLINE_FORMAT_HINT);
        }
        return new Deadline(description, by);
    }

    /**
     * Creates an event from arguments of the form
     * "description /from start /to end".
     *
     * @param arguments Text following the "event" command word.
     * @return The event described by the arguments.
     * @throws UsagiException If either separator is missing or out of order, or
     *                       any of the three parts is empty.
     */
    public static Event parseEvent(String arguments) throws UsagiException {
        String[] descriptionAndTimes = arguments.split(EVENT_FROM_DELIMITER, 2);
        if (descriptionAndTimes.length < 2) {
            throw new UsagiException(EVENT_FORMAT_HINT);
        }

        String[] times = descriptionAndTimes[1].split(EVENT_TO_DELIMITER, 2);
        if (times.length < 2) {
            throw new UsagiException(EVENT_FORMAT_HINT);
        }

        String description = descriptionAndTimes[0].trim();
        String from = times[0].trim();
        String to = times[1].trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new UsagiException(EVENT_FORMAT_HINT);
        }
        return new Event(description, from, to);
    }

    /**
     * Returns the keyword to search for, given the text following the "find"
     * command word.
     *
     * @param arguments Text following the "find" command word.
     * @return The keyword, which may contain spaces, e.g. "return book".
     * @throws UsagiException If no keyword was given.
     */
    public static String parseFindKeyword(String arguments) throws UsagiException {
        if (arguments.isEmpty()) {
            throw new UsagiException("Yahaaa?->(What should I look for? e.g. \"find book\".)");
        }
        return arguments;
    }

    /**
     * Converts a task number typed by the user into an index into the task list.
     *
     * Whether a task exists at that index is checked by {@link TaskList}, as
     * only the list knows how many tasks it holds.
     *
     * @param arguments Text following the command word, holding the task number
     *                  as shown by the "list" command (starting from 1).
     * @return The matching index into the task list (starting from 0).
     * @throws UsagiException If the text is missing or is not a number.
     */
    public static int parseTaskIndex(String arguments) throws UsagiException {
        if (arguments.isEmpty()) {
            throw new UsagiException("Huunnn->(Which task? Give me its number, e.g. \"mark 2\" or \"delete 2\".)");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new UsagiException("Prrurururur->(\"" + arguments + "\" is not a task number. Try \"mark 2\".)");
        }
        return taskNumber - 1;
    }
}

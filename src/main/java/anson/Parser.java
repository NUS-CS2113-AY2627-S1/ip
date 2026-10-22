package anson;

/**
 * Deals with making sense of the user command.
 */
public class Parser {
    private static final String DEADLINE_BY_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    /**
     * Returns the command word of a raw input line, e.g., "todo".
     *
     * @param input Raw line entered by the user.
     * @return The first word of the trimmed input.
     * @throws AnsonException If the input is blank.
     */
    public static String parseCommandWord(String input) throws AnsonException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new AnsonException("Please enter a command.");
        }
        return trimmed.split(" ", 2)[0];
    }

    /**
     * Returns the text after the command word, trimmed.
     *
     * @param input Raw line entered by the user.
     * @return The arguments, or an empty string if there are none.
     * @throws AnsonException If the arguments contain the '|' character.
     */
    public static String parseArguments(String input) throws AnsonException {
        String[] split = input.trim().split(" ", 2);
        String arguments = split.length > 1 ? split[1].trim() : "";

        if (arguments.contains("|")) {
            throw new AnsonException("Sorry, the '|' character can't be used in a command!");
        }
        return arguments;
    }

    /**
     * Parses and validates a task number given as an argument to
     * "mark"/"unmark"/"delete".
     *
     * @param arguments Raw text expected to be a single task number.
     * @param tasksCount Current number of tasks stored.
     * @return The validated, 1-based task number.
     * @throws AnsonException If the argument is missing, non-numeric, or out of range.
     */
    public static int parseTaskNumber(String arguments, int tasksCount) throws AnsonException {
        if (arguments.isEmpty()) {
            throw new AnsonException("Please specify the task number, e.g., mark 2.");
        }

        int taskNum;
        try {
            taskNum = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new AnsonException("The task number must be a whole number, e.g., mark 2.");
        }

        if (tasksCount == 0) {
            throw new AnsonException("Your task list is empty, so there is no task " + taskNum + ".");
        }

        if (taskNum <= 0 || taskNum > tasksCount) {
            throw new AnsonException("The task number must be between 1 and " + tasksCount + " inclusive.");
        }

        return taskNum;
    }

    /**
     * Parses the arguments of a "todo" command.
     *
     * @param arguments Text after the "todo" command word.
     * @return The new todo task.
     * @throws AnsonException If the description is empty.
     */
    public static Todo parseTodo(String arguments) throws AnsonException {
        if (arguments.isEmpty()) {
            throw new AnsonException("The description of a todo cannot be empty.");
        }
        return new Todo(arguments);
    }

    /**
     * Parses the arguments of a "deadline" command.
     *
     * @param arguments Text after the "deadline" command word.
     * @return The new deadline task.
     * @throws AnsonException If the description or "/by" date/time is missing/empty.
     */
    public static Deadline parseDeadline(String arguments) throws AnsonException {
        if (arguments.isEmpty()) {
            throw new AnsonException("The description of a deadline cannot be empty.");
        }

        if (!arguments.contains(DEADLINE_BY_SEPARATOR)) {
            throw new AnsonException(
                    "Please include '/by' followed by a date/time, e.g., deadline return book /by Sunday.");
        }

        String[] parts = arguments.split(DEADLINE_BY_SEPARATOR, 2);
        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new AnsonException("The description of a deadline cannot be empty.");
        }

        if (by.isEmpty()) {
            throw new AnsonException("Please specify a date/time after '/by'.");
        }

        return new Deadline(description, by);
    }

    /**
     * Parses the arguments of an "event" command.
     *
     * @param arguments Text after the "event" command word.
     * @return The new event task.
     * @throws AnsonException If the description, "/from", or "/to" date/time is missing/empty.
     */
    public static Event parseEvent(String arguments) throws AnsonException {
        if (arguments.isEmpty()) {
            throw new AnsonException("The description of an event cannot be empty.");
        }

        if (!arguments.contains(EVENT_FROM_SEPARATOR)) {
            throw new AnsonException(
                    "Please include '/from' followed by a start date/time, "
                            + "e.g., event meeting /from Mon 2pm /to 4pm.");
        }

        String[] fromSplit = arguments.split(EVENT_FROM_SEPARATOR, 2);
        String description = fromSplit[0].trim();

        if (description.isEmpty()) {
            throw new AnsonException("The description of an event cannot be empty.");
        }

        String remainder = fromSplit[1];
        if (!remainder.contains(EVENT_TO_SEPARATOR)) {
            throw new AnsonException(
                    "Please include '/to' followed by an end date/time, "
                            + "e.g., event meeting /from Mon 2pm /to 4pm.");
        }

        String[] toSplit = remainder.split(EVENT_TO_SEPARATOR, 2);
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();

        if (from.isEmpty()) {
            throw new AnsonException("Please specify a start date/time after '/from'.");
        }

        if (to.isEmpty()) {
            throw new AnsonException("Please specify an end date/time after '/to'.");
        }

        return new Event(description, from, to);
    }

    /**
     * Parses the arguments of a "find" command.
     *
     * @param arguments Text after the "find" command word.
     * @return The keyword to search for.
     * @throws AnsonException If the keyword is empty.
     */
    public static String parseFindKeyword(String arguments) throws AnsonException {
        if (arguments.isEmpty()) {
            throw new AnsonException("Please specify a keyword to search for, e.g., find book.");
        }
        return arguments;
    }
}
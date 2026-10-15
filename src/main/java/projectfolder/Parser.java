package projectfolder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * Parses user commands and converts command details into tasks and indexes.
 */
public class Parser {
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String FIND_PREFIX = "find ";

    /**
     * Creates a todo from a user command.
     *
     * @param input user command
     * @return created todo
     * @throws DinoException if the command is incomplete
     */
    public Todo parseTodo(String input) throws DinoException {
        if (input.equals("todo")) {
            throw new DinoException(
                    "OOPS!!! Please give your todo a description."
            );
        }

        String description = input.substring(TODO_PREFIX.length()).trim();

        if (description.isEmpty()) {
            throw new DinoException(
                    "OOPS!!! Please give your todo a description."
            );
        }

        return new Todo(description);
    }

    /**
     * Creates a deadline from a user command.
     *
     * @param input user command
     * @return created deadline
     * @throws DinoException if the command is incomplete or the date is invalid
     */
    public Deadline parseDeadline(String input) throws DinoException {
        if (input.equals("deadline")) {
            throw new DinoException(
                    "OOPS!!! A deadline needs a description and /by date."
            );
        }

        String taskDetails = input.substring(DEADLINE_PREFIX.length());
        int byIndex = taskDetails.indexOf(" /by ");

        if (byIndex < 0) {
            throw new DinoException(
                    "OOPS!!! A deadline must contain /by."
            );
        }

        String description = taskDetails.substring(0, byIndex).trim();
        String byText = taskDetails.substring(
                byIndex + " /by ".length()).trim();

        if (description.isEmpty()) {
            throw new DinoException(
                    "OOPS!!! Please give your deadline a description."
            );
        }

        if (byText.isEmpty()) {
            throw new DinoException(
                    "OOPS!!! Please give your deadline a /by date."
            );
        }

        try {
            LocalDate by = LocalDate.parse(byText);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new DinoException(
                    "OOPS!!! Please use yyyy-MM-dd for the deadline date."
            );
        }
    }

    /**
     * Creates an event from a user command.
     *
     * @param input user command
     * @return created event
     * @throws DinoException if the command is incomplete
     */
    public Event parseEvent(String input) throws DinoException {
        if (input.equals("event")) {
            throw new DinoException(
                    "OOPS!!! An event needs a description, /from, and /to."
            );
        }

        String taskDetails = input.substring(EVENT_PREFIX.length());
        int fromIndex = taskDetails.indexOf(" /from ");
        int toIndex = taskDetails.indexOf(" /to ");

        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
            throw new DinoException(
                    "OOPS!!! An event must contain /from followed by /to."
            );
        }

        String description = taskDetails.substring(0, fromIndex).trim();
        String from = taskDetails.substring(
                fromIndex + " /from ".length(), toIndex).trim();
        String to = taskDetails.substring(
                toIndex + " /to ".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DinoException(
                    "OOPS!!! Please complete all parts of your event."
            );
        }

        return new Event(description, from, to);
    }

    /**
     * Returns the zero-based task index from a user command.
     *
     * @param input user command
     * @param prefix command prefix
     * @param taskCount number of tasks
     * @param command command name
     * @return zero-based task index
     * @throws DinoException if the task number is invalid
     */
    public int parseTaskIndex(String input, String prefix,
                              int taskCount, String command)
            throws DinoException {
        if (input.equals(command)) {
            throw new DinoException(
                    "OOPS!!! Please specify a task number."
            );
        }

        String taskNumberText = input.substring(prefix.length()).trim();

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            validateTaskNumber(taskNumber, taskCount);
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new DinoException(
                    "OOPS!!! The task number must be a number."
            );
        }
    }

    private void validateTaskNumber(int taskNumber, int taskCount)
            throws DinoException {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new DinoException(
                    "OOPS!!! That task number does not exist."
            );
        }
    }

    /**
     * Returns the keyword from a find command.
     *
     * @param input user command
     * @return keyword to search for
     * @throws DinoException if no keyword is provided
     */
    public String parseFindKeyword(String input) throws DinoException {
        if (input.equals("find")) {
            throw new DinoException(
                    "OOPS!!! Please give me a keyword to find."
            );
        }

        String keyword = input.substring(FIND_PREFIX.length()).trim();

        if (keyword.isEmpty()) {
            throw new DinoException(
                    "OOPS!!! Please give me a keyword to find."
            );
        }

        return keyword;
    }
}
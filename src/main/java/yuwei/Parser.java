package yuwei;

import yuwei.task.Deadline;
import yuwei.task.Event;
import yuwei.task.Task;
import yuwei.task.ToDo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Makes sense of the text the user types, e.g. splitting
 * {@code deadline return book /by 2019-10-15} into its parts.
 *
 * <p>All methods are static because a Parser has no state of its own: the result depends
 * only on the text passed in. (If parsing ever needed settings, such as a preferred date
 * format, it would make sense to turn this into an object that stores them.)
 */
public class Parser {
    private static final String MISSING_TASK_NUMBER_MESSAGE = "Please tell me which task number.";
    private static final String EVENT_FORMAT_MESSAGE =
            "An event needs a start and an end. Try: event <description> /from <start> /to <end>";

    /** Returns the first word of {@code fullCommand}, e.g. {@code "todo"} for {@code "todo read book"}. */
    public static String getCommandWord(String fullCommand) {
        return fullCommand.split(" ", 2)[0];
    }

    /**
     * Returns everything after the command word, e.g. {@code "read book"} for {@code "todo read book"}.
     *
     * @throws YuWeiException with {@code errorMessage} if there is nothing after the command word
     */
    public static String getArgument(String fullCommand, String errorMessage) throws YuWeiException {
        String[] commandAndArgument = fullCommand.split(" ", 2);
        if (commandAndArgument.length < 2 || commandAndArgument[1].isBlank()) {
            throw new YuWeiException(errorMessage);
        }
        return commandAndArgument[1];
    }

    /**
     * Returns the 0-based index of the task number given in a command such as {@code "mark 2"}.
     * Whether a task with that number exists is checked later, by {@link TaskList}.
     *
     * @throws YuWeiException if no task number was given, or it is not a number
     */
    public static int parseTaskIndex(String fullCommand) throws YuWeiException {
        String taskNumber = getArgument(fullCommand, MISSING_TASK_NUMBER_MESSAGE);
        try {
            return Integer.parseInt(taskNumber.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new YuWeiException("'" + taskNumber + "' is not a task number.");
        }
    }

    /**
     * Creates the task described by {@code argument}, of the type named by {@code commandWord}.
     *
     * @param commandWord one of {@code "todo"}, {@code "deadline"} or {@code "event"}
     * @param argument the text after the command word
     * @throws YuWeiException if {@code argument} is not in the format that task type needs
     */
    public static Task parseTask(String commandWord, String argument) throws YuWeiException {
        return switch (commandWord) {
            case "todo" -> new ToDo(argument);
            case "deadline" -> parseDeadline(argument);
            case "event" -> parseEvent(argument);
            // Unreachable: YuWei only calls this for the three types above.
            default -> throw new IllegalArgumentException("Unknown task type: " + commandWord);
        };
    }

    /**
     * Converts text such as {@code 2019-10-15} into a date.
     *
     * @throws YuWeiException if the text is not a valid date in {@code yyyy-mm-dd} format
     */
    public static LocalDate parseDate(String text) throws YuWeiException {
        try {
            // LocalDate.parse reads the ISO format yyyy-mm-dd and rejects impossible dates like 2019-02-30.
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException e) {
            throw new YuWeiException("'" + text + "' is not a date I understand. "
                    + "Please use yyyy-mm-dd, e.g. 2019-10-15.");
        }
    }

    /** Creates a Deadline from an argument of the form {@code <description> /by <yyyy-mm-dd>}. */
    private static Deadline parseDeadline(String argument) throws YuWeiException {
        String[] descriptionAndBy = argument.split(" /by ", 2);
        if (descriptionAndBy.length < 2) {
            throw new YuWeiException(
                    "A deadline needs a date. Try: deadline <description> /by <yyyy-mm-dd>");
        }
        return new Deadline(descriptionAndBy[0], parseDate(descriptionAndBy[1]));
    }

    /** Creates an Event from an argument of the form {@code <description> /from <start> /to <end>}. */
    private static Event parseEvent(String argument) throws YuWeiException {
        String[] descriptionAndTimes = argument.split(" /from ", 2);
        if (descriptionAndTimes.length < 2) {
            throw new YuWeiException(EVENT_FORMAT_MESSAGE);
        }
        String[] fromAndTo = descriptionAndTimes[1].split(" /to ", 2);
        if (fromAndTo.length < 2) {
            throw new YuWeiException(EVENT_FORMAT_MESSAGE);
        }
        return new Event(descriptionAndTimes[0], fromAndTo[0], fromAndTo[1]);
    }
}

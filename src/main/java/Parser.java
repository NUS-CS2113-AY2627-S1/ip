/**
 * Makes sense of the user's input: splits a line into its command word and
 * arguments, and converts arguments into task numbers or new tasks.
 */
public class Parser {
    /** Returns the first word of {@code input} in lower case, e.g. "deadline" for "DEADLINE x /by y". */
    public static String getCommandWord(String input) {
        return input.split(" ", 2)[0].toLowerCase();
    }

    /** Returns the text after the command word, or an empty string if there is none. */
    public static String getArguments(String input) {
        String[] parts = input.split(" ", 2);
        return parts.length < 2 ? "" : parts[1].trim();
    }

    /**
     * Converts the one-based task number in {@code args} into a zero-based list index.
     *
     * @param commandWord the command being run, used to show an example of correct input.
     * @throws ShaheerException if the number is missing or not a whole number.
     */
    public static int parseTaskIndex(String args, String commandWord) throws ShaheerException {
        if (args.isEmpty()) {
            throw new ShaheerException("Please specify the task number, e.g.: " + commandWord + " 2");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            throw new ShaheerException("The task number must be a whole number, e.g.: " + commandWord + " 2");
        }
        return taskNumber - 1;
    }

    /**
     * Returns the keyword to search for in a find command.
     *
     * @throws ShaheerException if the keyword is blank.
     */
    public static String parseFindKeyword(String args) throws ShaheerException {
        if (args.isEmpty()) {
            throw new ShaheerException("Please specify a keyword to search for, e.g.: find book");
        }
        return args;
    }

    /**
     * Builds a Todo whose description is the whole of {@code args}.
     *
     * @throws ShaheerException if the description is blank.
     */
    public static Todo parseTodo(String args) throws ShaheerException {
        if (args.isEmpty()) {
            throw new ShaheerException("The description of a todo cannot be empty.");
        }
        return new Todo(args);
    }

    /**
     * Builds a Deadline from arguments of the form "{@code <description> /by <when>}".
     *
     * @throws ShaheerException if the description, "/by", or the time is missing.
     */
    public static Deadline parseDeadline(String args) throws ShaheerException {
        String[] deadlineParts = args.split(" /by ", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new ShaheerException(
                "A deadline needs a description and a /by time, e.g.: deadline return book /by Sunday");
        }
        return new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
    }

    /**
     * Builds an Event from arguments of the form "{@code <description> /from <start> /to <end>}".
     *
     * @throws ShaheerException if the description, "/from", or "/to" part is missing.
     */
    public static Event parseEvent(String args) throws ShaheerException {
        String[] eventParts = args.split(" /from ", 2);
        if (eventParts.length < 2 || eventParts[0].trim().isEmpty()) {
            throw new ShaheerException(
                "An event needs a description, a /from time and a /to time, e.g.: "
                    + "event project meeting /from Mon 2pm /to 4pm");
        }
        String[] eventTimeParts = eventParts[1].split(" /to ", 2);
        if (eventTimeParts.length < 2 || eventTimeParts[0].trim().isEmpty() || eventTimeParts[1].trim().isEmpty()) {
            throw new ShaheerException(
                "An event needs both a /from time and a /to time, e.g.: "
                    + "event project meeting /from Mon 2pm /to 4pm");
        }
        return new Event(eventParts[0].trim(), eventTimeParts[0].trim(), eventTimeParts[1].trim());
    }
}

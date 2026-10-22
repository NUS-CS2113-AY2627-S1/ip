package duke;

/**
 * Makes sense of raw user input: parses commands into the pieces
 * needed to create tasks or identify which task a command refers to.
 */
public class Parser {
    /**
     * Parses a todo command into a Todo task.
     *
     * @param input The full "todo ..." command.
     * @return The parsed Todo task.
     * @throws DukeExceptions If the description is empty.
     */
    public static Todo parseTodo(String input) throws DukeExceptions {
        String description = input.length() > 4 ? input.substring(5).trim() : "";
        if (description.isEmpty()) {
            throw new DukeExceptions("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }
    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param input The full "deadline ..." command.
     * @return The parsed Deadline task.
     * @throws DukeExceptions If the description is empty.
     */
    public static Deadline parseDeadline(String input) throws DukeExceptions {
        String remainder = input.length() > 8 ? input.substring(9).trim() : "";
        if (remainder.isEmpty()) {
            throw new DukeExceptions("The description of a deadline cannot be empty.");
        }
        String[] parts = remainder.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DukeExceptions("A deadline needs a description and a '/by' date, "
                    + "e.g. deadline return book /by Sunday");
        }
        return new Deadline(parts[0].trim(), parts[1].trim());
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param input The full "event ..." command.
     * @return The parsed Event task.
     * @throws DukeExceptions If the description is empty.
     */
    public static Event parseEvent(String input) throws DukeExceptions {
        String remainder = input.length() > 5 ? input.substring(6).trim() : "";
        if (remainder.isEmpty()) {
            throw new DukeExceptions("The description of an event cannot be empty.");
        }
        String[] fromSplit = remainder.split(" /from ", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new DukeExceptions("An event needs a '/from' time, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
        }
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new DukeExceptions("An event needs a '/to' time, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
        }
        return new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
    }

    /**
     * Extracts the 0-based task index from a command like "mark 2".
     *
     * @param input  The full command.
     * @param prefix The command keyword including its trailing space, e.g. "mark ".
     * @return The 0-based index into the task list.
     */
    public static int parseTaskNumber(String input, String prefix) {
        return Integer.parseInt(input.substring(prefix.length()).trim()) - 1;
    }
}
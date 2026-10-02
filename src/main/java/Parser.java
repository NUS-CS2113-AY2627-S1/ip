/**
 * Parses user input into commands and command arguments.
 */
public class Parser {
    /**
     * Represents the commands supported by Exia.
     */
    public enum CommandType {
        BYE, LIST, DONE, DELETE, FIND, TODO, DEADLINE, EVENT, UNKNOWN
    }

    /**
     * Identifies the type of a user command.
     *
     * @param input complete user input
     * @return type of the command
     */
    public static CommandType getCommandType(String input) {
        if (input.equals("bye")) {
            return CommandType.BYE;
        }
        if (input.equals("list")) {
            return CommandType.LIST;
        }
        if (input.equals("done") || input.startsWith("done ")) {
            return CommandType.DONE;
        }
        if (input.equals("delete") || input.startsWith("delete ")) {
            return CommandType.DELETE;
        }
        if (input.equals("find") || input.startsWith("find ")) {
            return CommandType.FIND;
        }
        if (input.equals("todo") || input.startsWith("todo ")) {
            return CommandType.TODO;
        }
        if (input.equals("deadline") || input.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        }
        if (input.equals("event") || input.startsWith("event ")) {
            return CommandType.EVENT;
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Extracts a task number from a command.
     *
     * @param input complete user input
     * @param command command word preceding the task number
     * @return parsed task number
     * @throws ExiaException if the task number is missing or invalid
     */
    public static int parseTaskNumber(String input, String command)
            throws ExiaException {
        String taskNumberText = input.substring(command.length()).trim();

        if (taskNumberText.isEmpty()) {
            if (command.equals("done")) {
                throw new ExiaException(
                        "Please tell me which task number to mark as done.");
            }
            throw new ExiaException(
                    "Please tell me which task number to delete.");
        }

        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new ExiaException("Task number must be a number.");
        }
    }

    /**
     * Extracts the search keyword from a find command.
     *
     * @param input complete user input
     * @return search keyword
     * @throws ExiaException if the keyword is missing
     */
    public static String parseFindKeyword(String input)
            throws ExiaException {
        String keyword = getContent(input, "find");

        if (keyword.isEmpty()) {
            throw new ExiaException(
                    "Please tell me what to find.");
        }

        return keyword;
    }

    /**
     * Creates a task from an add-task command.
     *
     * @param input complete user input
     * @return task represented by the command
     * @throws ExiaException if the command is invalid
     */
    public static Task createTask(String input) throws ExiaException {
        return switch (getCommandType(input)) {
            case TODO -> createToDo(input);
            case DEADLINE -> createDeadline(input);
            case EVENT -> createEvent(input);
            default -> throw new ExiaException(
                    "I'm sorry, but I don't know what that means :-(");
        };
    }

    private static ToDo createToDo(String input) throws ExiaException {
        String description = getContent(input, "todo");

        if (description.isEmpty()) {
            throw new ExiaException(
                    "The description of a todo cannot be empty.");
        }

        return new ToDo(description);
    }

    private static Deadline createDeadline(String input)
            throws ExiaException {
        String content = getContent(input, "deadline");

        if (content.isEmpty()) {
            throw new ExiaException(
                    "The description of a deadline cannot be empty.");
        }

        String[] parts = content.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty()) {
            throw new ExiaException(
                    "Please use: deadline DESCRIPTION /by TIME");
        }

        return new Deadline(parts[0].trim(), parts[1].trim());
    }

    private static Event createEvent(String input) throws ExiaException {
        String content = getContent(input, "event");

        if (content.isEmpty()) {
            throw new ExiaException(
                    "The description of an event cannot be empty.");
        }

        String[] fromSplit = content.split(" /from ", 2);

        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new ExiaException(
                    "Please use: event DESCRIPTION /from START /to END");
        }

        String[] toSplit = fromSplit[1].split(" /to ", 2);

        if (toSplit.length < 2 || toSplit[0].trim().isEmpty()
                || toSplit[1].trim().isEmpty()) {
            throw new ExiaException(
                    "Please use: event DESCRIPTION /from START /to END");
        }

        return new Event(
                fromSplit[0].trim(),
                toSplit[0].trim(),
                toSplit[1].trim());
    }

    private static String getContent(String input, String command) {
        return input.substring(command.length()).trim();
    }
}

package gpt;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands and their arguments.
 */
public class Parser {

    /**
     * Returns the command word at the start of the given input.
     *
     * @param input full user input
     * @return command word
     */
    public static String getCommandWord(String input) {
        return input.split(" ")[0];
    }

    /**
     * Returns a task parsed from a task-creation command.
     *
     * @param input full user input
     * @return task created from the command
     * @throws GptException if the command or its arguments are invalid
     */
    public static Task parseTask(String input) throws GptException {
        String commandWord = getCommandWord(input);
        if (commandWord.equals("todo")) {
            return parseTodo(input);
        } else if (commandWord.equals("deadline")) {
            return parseDeadline(input);
        } else if (commandWord.equals("event")) {
            return parseEvent(input);
        } else {
            throw new GptException("OOPS!!! I don't know what that command means.");
        }
    }

    /**
     * Returns the zero-based task index in the given command.
     *
     * @param input full user input
     * @param taskCount number of tasks currently stored
     * @return zero-based task index
     * @throws GptException if the task number is missing, invalid, or out of range
     */
    public static int parseTaskIndex(String input, int taskCount) throws GptException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new GptException("OOPS!!! Please tell me which task number to update.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new GptException("OOPS!!! Task numbers must be whole numbers.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new GptException("OOPS!!! That task number does not exist.");
        }
        return taskNumber - 1;
    }

    /**
     * Returns the keyword in a find command.
     *
     * @param input full user input
     * @return keyword to search for
     * @throws GptException if the keyword is missing
     */
    public static String parseFindKeyword(String input) throws GptException {
        String keyword = input.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new GptException("OOPS!!! Please enter a keyword to find.");
        }
        return keyword;
    }

    private static Task parseTodo(String input) throws GptException {
        String description = input.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new GptException("OOPS!!! The description of a todo cannot be empty.");
        }

        return new Todo(description);
    }

    private static Task parseDeadline(String input) throws GptException {
        String arguments = input.substring("deadline".length()).trim();
        if (arguments.isEmpty()) {
            throw new GptException("OOPS!!! The description of a deadline cannot be empty.");
        }

        int byMarkerIndex = findMarkerIndex(arguments, "/by");
        if (byMarkerIndex < 0) {
            throw new GptException("OOPS!!! A deadline needs a /by date or time.");
        }
        String description = arguments.substring(0, byMarkerIndex).trim();
        String byText = arguments.substring(byMarkerIndex + getMarkerLength(byMarkerIndex, "/by")).trim();

        if (description.isEmpty()) {
            throw new GptException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (byText.isEmpty()) {
            throw new GptException("OOPS!!! The deadline date or time cannot be empty.");
        }

        try {
            LocalDate by = LocalDate.parse(byText);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new GptException("OOPS!!! Please enter the deadline date as yyyy-MM-dd.");
        }
    }

    private static Task parseEvent(String input) throws GptException {
        String arguments = input.substring("event".length()).trim();
        if (arguments.isEmpty()) {
            throw new GptException("OOPS!!! The description of an event cannot be empty.");
        }

        int fromMarkerIndex = findMarkerIndex(arguments, "/from");
        int toMarkerIndex = findMarkerIndex(arguments, "/to");
        if (fromMarkerIndex < 0) {
            throw new GptException("OOPS!!! An event needs a /from date or time.");
        }
        if (toMarkerIndex < 0 || toMarkerIndex < fromMarkerIndex) {
            throw new GptException("OOPS!!! An event needs a /to date or time.");
        }

        String description = arguments.substring(0, fromMarkerIndex).trim();
        String from = arguments.substring(fromMarkerIndex + getMarkerLength(fromMarkerIndex, "/from"),
                toMarkerIndex).trim();
        String to = arguments.substring(toMarkerIndex + getMarkerLength(toMarkerIndex, "/to")).trim();

        if (description.isEmpty()) {
            throw new GptException("OOPS!!! The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new GptException("OOPS!!! The event start date or time cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new GptException("OOPS!!! The event end date or time cannot be empty.");
        }
        return new Event(description, from, to);
    }

    private static int findMarkerIndex(String text, String marker) {
        if (text.startsWith(marker)) {
            return 0;
        }
        return text.indexOf(" " + marker);
    }

    private static int getMarkerLength(int markerIndex, String marker) {
        return markerIndex == 0 ? marker.length() : marker.length() + 1;
    }
}

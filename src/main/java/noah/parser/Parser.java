package noah.parser;

import noah.exception.NoahException;
import noah.task.Deadline;
import noah.task.Event;
import noah.task.Task;
import noah.task.TaskDateTime;
import noah.task.Todo;

/**
 * Interprets user commands without changing tasks or displaying messages.
 */
public final class Parser {
    private static final String UNKNOWN_COMMAND_MESSAGE =
            "That command isn't in my adventurer's handbook!\nTry: list, todo read book, clear, bye... :)";

    /**
     * Prevents instantiation because parsing does not require stored state.
     */
    private Parser() {
    }

    /**
     * Identifies a supported command word.
     * The list, clear, and bye commands must be entered without arguments.
     * Normalizes yes/y and no/n confirmation replies regardless of case or surrounding spaces.
     *
     * @param userCommand Full command entered by the user.
     * @return Recognized command word.
     * @throws NoahException If the command is not supported.
     */
    public static String parseCommandWord(String userCommand) throws NoahException {
        String response = userCommand.trim();
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
            return "yes";
        }
        if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
            return "no";
        }
        int spaceIndex = userCommand.indexOf(' ');
        String command = spaceIndex == -1 ? userCommand : userCommand.substring(0, spaceIndex);
        switch (command) {
        case "list":
        case "clear":
        case "bye":
            if (!userCommand.equals(command)) {
                throw new NoahException(UNKNOWN_COMMAND_MESSAGE);
            }
            return command;
        case "todo":
        case "deadline":
        case "event":
        case "find":
        case "mark":
        case "unmark":
        case "delete":
            return command;
        default:
            throw new NoahException(UNKNOWN_COMMAND_MESSAGE);
        }
    }

    /**
     * Extracts the nonblank text to look for in task descriptions.
     *
     * @param userCommand Full find command entered by the user.
     * @return Search text with surrounding spaces removed.
     * @throws NoahException If the command is not find or no keyword is supplied.
     */
    public static String parseFindKeyword(String userCommand) throws NoahException {
        String command = parseCommandWord(userCommand);
        if (!command.equals("find")) {
            throw new NoahException(UNKNOWN_COMMAND_MESSAGE);
        }
        String keyword = userCommand.substring(command.length()).trim();
        if (keyword.isEmpty()) {
            throw new NoahException("What are we looking for, Traveler? Add a keyword. Try: find book");
        }
        return keyword;
    }

    /**
     * Parses a positive task number for a mark, unmark, or delete command.
     * The task list checks whether a task with that number exists.
     *
     * @param userCommand Full command entered by the user.
     * @return Positive task number entered by the user.
     * @throws NoahException If the command or task number is invalid.
     */
    public static int parseTaskNumber(String userCommand) throws NoahException {
        String command = parseCommandWord(userCommand);
        if (!command.equals("mark") && !command.equals("unmark") && !command.equals("delete")) {
            throw new NoahException(UNKNOWN_COMMAND_MESSAGE);
        }
        String taskNumberText = userCommand.substring(command.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new NoahException("Which task, Traveler? Add its number. Try: " + command + " 1");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new NoahException("Oops, task numbers start at 1! Use a whole number from list.");
        }
        if (taskNumber <= 0) {
            throw new NoahException("Oops, task numbers start at 1! Use a whole number from list.");
        }
        return taskNumber;
    }

    /**
     * Creates a task described by a todo, deadline, or event command.
     *
     * @param userCommand Full command entered by the user.
     * @return New task that has not yet been added to the task list.
     * @throws NoahException If the command or task details are invalid or contain a reserved vertical bar.
     */
    public static Task parseTask(String userCommand) throws NoahException {
        String command = parseCommandWord(userCommand);
        String description = userCommand.substring(command.length()).trim();
        if (description.contains("|")) {
            throw new NoahException("That symbol is reserved for saving quests!\n"
                    + "Please replace | in task descriptions and time labels.");
        }
        return switch (command) {
        case "todo" -> parseTodo(description);
        case "deadline" -> parseDeadline(description);
        case "event" -> parseEvent(description);
        default -> throw new NoahException(UNKNOWN_COMMAND_MESSAGE);
        };
    }

    /**
     * Creates a todo task from the text following the command word.
     *
     * @param description Task description entered by the user.
     * @return New todo task.
     * @throws NoahException If the description is blank.
     */
    private static Todo parseTodo(String description) throws NoahException {
        if (description.isBlank()) {
            throw new NoahException("Every quest needs a name! Add a description. Try: todo read book");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline task with an optional numeric date/time or free-text date label.
     *
     * @param description Text following the deadline command word.
     * @return New deadline task.
     * @throws NoahException If required details are missing or an explicit numeric date/time is invalid.
     */
    private static Deadline parseDeadline(String description) throws NoahException {
        if (description.isBlank()) {
            throw new NoahException(
                    "What's the mission? Add a description. Try: deadline return book /by Sunday");
        }

        int byIndex = findDelimiter(description, "/by");
        if (byIndex == -1) {
            return new Deadline(description);
        }

        String taskDescription = description.substring(0, byIndex).trim();
        String by = description.substring(byIndex + "/by".length()).trim();
        if (taskDescription.isBlank() || by.isBlank()) {
            throw new NoahException(
                    "This deadline is missing a piece! Add a description and date.\n"
                            + "Try: deadline return book /by Sunday");
        }
        return new Deadline(taskDescription, TaskDateTime.parse(by));
    }

    /**
     * Creates an event task from its description and optional time period.
     *
     * @param description Text following the event command word.
     * @return New event task.
     * @throws NoahException If required event details are missing.
     */
    private static Event parseEvent(String description) throws NoahException {
        if (description.isBlank()) {
            throw new NoahException(
                    "What's the occasion? Add a description. Try: event meeting /from 2pm /to 4pm");
        }

        int fromIndex = findDelimiter(description, "/from");
        int toIndex = findDelimiter(description, "/to");
        if (fromIndex == -1 && toIndex == -1) {
            return new Event(description);
        }
        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            throw new NoahException(
                    "Let's fill in the blanks! An event needs a description, start, and end.\n"
                            + "Try: event meeting /from 2pm /to 4pm");
        }

        String taskDescription = description.substring(0, fromIndex).trim();
        String from = description.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = description.substring(toIndex + "/to".length()).trim();
        if (taskDescription.isBlank() || from.isBlank() || to.isBlank()) {
            throw new NoahException(
                    "Let's fill in the blanks! An event needs a description, start, and end.\n"
                            + "Try: event meeting /from 2pm /to 4pm");
        }
        return new Event(taskDescription, from, to);
    }

    /**
     * Finds a complete delimiter token instead of matching text inside a word or URL.
     * A delimiter must be bounded by whitespace or the beginning/end of the input.
     *
     * @param text Task details following the command word.
     * @param delimiter Timing marker to find, such as /by.
     * @return Index of the first complete delimiter, or -1 if none is present.
     */
    private static int findDelimiter(String text, String delimiter) {
        int index = text.indexOf(delimiter);
        while (index != -1) {
            int endIndex = index + delimiter.length();
            boolean hasStartBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            boolean hasEndBoundary = endIndex == text.length() || Character.isWhitespace(text.charAt(endIndex));
            if (hasStartBoundary && hasEndBoundary) {
                return index;
            }
            index = text.indexOf(delimiter, endIndex);
        }
        return -1;
    }
}

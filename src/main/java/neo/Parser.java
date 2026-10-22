package neo;

import neo.exception.NeoException;
import neo.task.Deadline;
import neo.task.Event;
import neo.task.Todo;

/**
 * Parses user input to extract commands, task descriptions, and relevant parameters.
 * Provides utility methods to translate raw string inputs into actionable task objects
 * or list indices.
 */
public class Parser {
    public static final String EXIT_COMMAND = "bye";
    public static final String LIST_COMMAND = "list";
    public static final String MARK_PREFIX = "mark ";
    public static final String UNMARK_PREFIX = "unmark ";
    public static final String TODO_PREFIX = "todo ";
    public static final String DEADLINE_PREFIX = "deadline ";
    public static final String EVENT_PREFIX = "event ";
    public static final String DELETE_PREFIX = "delete ";
    public static final String FIND_PREFIX = "find ";

    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";

    /**
     * Parses the task index from a user command.
     *
     * @param userInput The full string input provided by the user.
     * @param commandPrefix The command prefix to be removed from the input.
     * @param listSize The current number of tasks in the list.
     * @return The zero-based index of the target task.
     * @throws NeoException If the index is missing, not a valid number, or out of bounds.
     */
    public static int parseIndex(String userInput, String commandPrefix, int listSize)
            throws NeoException {
        String indexString = userInput.substring(commandPrefix.length()).trim();
        if (indexString.isEmpty()) {
            throw new NeoException("The task index is missing. Try typing: "
                    + commandPrefix.trim() + " <index>.");
        }

        try {
            int taskIndex = Integer.parseInt(indexString) - 1;
            if (taskIndex < 0 || taskIndex >= listSize) {
                throw new NeoException("Task number " + (taskIndex + 1) + " does not exist. "
                        + "There are " + listSize + " tasks in the list.");
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            throw new NeoException("The task index is not a valid number.");
        }
    }

    /**
     * Parses a todo command and creates a new Todo task.
     *
     * @param userInput The full string input provided by the user.
     * @return A new {@code Todo} task containing the parsed description.
     * @throws NeoException If the todo description is empty.
     */
    public static Todo parseTodo(String userInput) throws NeoException {
        String description = userInput.substring(TODO_PREFIX.length()).trim();
        if (description.isEmpty()) {
            throw new NeoException("The todo description is empty. "
                    + "Try typing: todo <description>.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command and creates a new Deadline task.
     *
     * @param userInput The full string input provided by the user.
     * @return A new {@code Deadline} task containing the parsed description and deadline date.
     * @throws NeoException If the description or date is missing, or if the delimiter is absent.
     */
    public static Deadline parseDeadline(String userInput) throws NeoException {
        String payload = userInput.substring(DEADLINE_PREFIX.length()).trim();
        if (payload.isEmpty()) {
            throw new NeoException("The deadline description is missing. "
                    + "Try typing: " + DEADLINE_PREFIX.trim() + " <description> /by <date>");
        }

        String[] parts = payload.split(DEADLINE_DELIMITER);
        if (parts.length < 2) {
            throw new NeoException("The deadline date is missing. "
                    + "Try typing: " + DEADLINE_PREFIX.trim() + " <description> /by <date>");
        }

        return new Deadline(parts[0], parts[1]);
    }

    /**
     * Parses an event command and creates a new Event task.
     *
     * @param userInput The full string input provided by the user.
     * @return A new {@code Event} task containing the parsed description, start date, and end date.
     * @throws NeoException If the description, start date, or end date is missing or malformed.
     */
    public static Event parseEvent(String userInput) throws NeoException {
        String payload = userInput.substring(EVENT_PREFIX.length());
        if (payload.isBlank() || payload.stripLeading().startsWith(EVENT_FROM_DELIMITER.stripLeading())) {
            throw new NeoException("The event description is missing. "
                    + "Try typing: " + EVENT_PREFIX.trim()
                    + " <description> /from <start date> /to <end date>");
        }

        String[] fromSplit = payload.split(EVENT_FROM_DELIMITER, 2);
        if (fromSplit.length < 2) {
            throw new NeoException("The event start date is missing. "
                    + "Try typing: " + EVENT_PREFIX.trim()
                    + " <description> /from <start date> /to <end date>");
        }

        String[] toSplit = fromSplit[1].split(EVENT_TO_DELIMITER, 2);
        if (toSplit[0].isBlank()) {
            throw new NeoException("The event start date is missing. "
                    + "Try typing: " + EVENT_PREFIX.trim()
                    + " <description> /from <start date> /to <end date>");
        }
        if (toSplit.length < 2 || toSplit[1].isBlank()) {
            throw new NeoException("The event end date is missing. "
                    + "Try typing: " + EVENT_PREFIX.trim()
                    + " <description> /from <start date> /to <end date>");
        }

        return new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
    }

    /**
     * Parses a find command to extract the search keyword.
     *
     * @param userInput The full string input provided by the user.
     * @return The keyword to search for.
     * @throws NeoException If the search keyword is missing.
     */
    public static String parseFind(String userInput) throws NeoException {
        String keyword = userInput.substring(FIND_PREFIX.length()).trim();
        if (keyword.isEmpty()) {
            throw new NeoException("The search keyword is missing. "
                    + "Try typing: " + FIND_PREFIX.trim() + " <keyword>");
        }
        return keyword;
    }
}

package dandelion.parser;

import dandelion.task.Deadline;
import dandelion.task.Event;
import dandelion.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands into application data.
 */
public class Parser {

    /**
     * Creates a command parser.
     */
    public Parser() {
    }

    /**
     * Extracts the first word from a command.
     *
     * @param input User command.
     * @return Command word, or an empty string for empty input.
     */
    public String getCommandWord(String input) {
        if (input.isEmpty()) {
            return "";
        }
        return input.split(" ", 2)[0];
    }

    /**
     * Extracts the argument after a command such as {@code mark 2}.
     *
     * @param input Command with an argument.
     * @return Command argument.
     */
    public String getArgument(String input) {
        int separatorIndex = input.indexOf(' ');
        if (separatorIndex == -1) {
            return "";
        }
        return input.substring(separatorIndex + 1).trim();
    }

    /**
     * Creates a todo from user input.
     *
     * @param input Todo command.
     * @return Parsed todo.
     */
    public Todo parseTodo(String input) {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Todo description cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline from input such as {@code deadline return book /by 2019-12-02}.
     *
     * @param input Deadline command.
     * @return Parsed deadline.
     * @throws IllegalArgumentException If the command is missing a description or deadline,
     *                                  or the deadline is not in {@code yyyy-MM-dd} format.
     */
    public Deadline parseDeadline(String input) {
        String content = input.substring("deadline".length()).trim();

        int byIndex = content.indexOf("/by");

        if (byIndex == -1) {
            throw new IllegalArgumentException("/by is missing.");
        }

        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + "/by".length()).trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new IllegalArgumentException("Both description and deadline are required.");
        }

        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Deadline must use yyyy-MM-dd format.", exception);
        }
    }

    /**
     * Creates an event from input containing {@code /from} and {@code /to} markers.
     *
     * @param input Event command.
     * @return Parsed event.
     * @throws IllegalArgumentException If the command is missing a required field.
     */
    public Event parseEvent(String input) {
        String content = input.substring("event".length()).trim();

        int fromIndex = content.indexOf("/from");
        int toIndex = content.indexOf("/to");

        if (fromIndex == -1) {
            throw new IllegalArgumentException("Event must include /from.");
        }
        if (toIndex == -1) {
            throw new IllegalArgumentException("Event must include /to.");
        }
        if (toIndex <= fromIndex) {
            throw new IllegalArgumentException("Event must place /from before /to.");
        }

        String description = content.substring(0, fromIndex).trim();
        String from = content.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = content.substring(toIndex + "/to".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Description, from, and to cannot be empty.");
        }

        return new Event(description, from, to);
    }
}

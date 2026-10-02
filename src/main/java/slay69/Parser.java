package slay69;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import slay69.command.AddCommand;
import slay69.command.Command;
import slay69.command.DeleteCommand;
import slay69.command.ExitCommand;
import slay69.command.FindCommand;
import slay69.command.ListCommand;
import slay69.command.MarkCommand;
import slay69.command.UnmarkCommand;
import slay69.task.Deadline;
import slay69.task.Event;
import slay69.task.Todo;

/**
 * Converts raw user input into executable commands.
 */
public final class Parser {
    private Parser() {
        // This utility class should not be instantiated.
    }

    /**
     * Parses a complete line of user input.
     *
     * @param input command text entered by the user
     * @return the command to execute
     * @throws Slay69Exception if the command or its arguments are invalid
     */
    public static Command parse(String input) throws Slay69Exception {
        if (input.isEmpty()) {
            throw new Slay69Exception("Please enter a command.");
        }

        if (input.contains("|")) {
            throw new Slay69Exception(
                    "Please avoid '|'; it is reserved for saving tasks.");
        }

        String[] inputParts = input.split("\\s+", 2);
        String commandWord = inputParts[0];
        String arguments = inputParts.length > 1
                ? inputParts[1].trim()
                : "";

        switch (commandWord) {
        case "list":
            requireNoArguments(commandWord, arguments);
            return new ListCommand();
        case "find":
            if (arguments.isBlank()) {
                throw new Slay69Exception("Please provide a keyword. Try: find book");
            }
            return new FindCommand(arguments);
        case "mark":
            return new MarkCommand(parseTaskNumber(arguments, commandWord));
        case "unmark":
            return new UnmarkCommand(parseTaskNumber(arguments, commandWord));
        case "delete":
            return new DeleteCommand(parseTaskNumber(arguments, commandWord));
        case "todo":
            return new AddCommand(createTodo(arguments));
        case "deadline":
            return new AddCommand(createDeadline(arguments));
        case "event":
            return new AddCommand(createEvent(arguments));
        case "bye":
            requireNoArgumentsForBye(arguments);
            return new ExitCommand();
        default:
            throw new Slay69Exception(
                    "What is this command?! "
                            + "Try: todo, deadline, event, list, mark, "
                            + "unmark, delete, find, or bye.");
        }
    }

    /**
     * Validates the description before creating a todo task.
     *
     * @throws Slay69Exception if the description is missing
     */
    private static Todo createTodo(String description)
            throws Slay69Exception {
        if (description.isBlank()) {
            throw new Slay69Exception(
                    "A todo needs a description laaaa. Try: todo read book");
        }
        return new Todo(description);
    }

    /**
     * Separates a deadline description from its /by value and checks both fields.
     *
     * @throws Slay69Exception if the description, marker, or deadline is missing
     */
    private static Deadline createDeadline(String arguments)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "A deadline needs a description laaaa. "
                            + "Try: deadline return book /by 2019-12-02");
        }

        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2) {
            throw new Slay69Exception(
                    "Please laaaaa, a deadline needs '/by'. "
                            + "Try: deadline return book /by 2019-12-02");
        }

        String description = parts[0].trim();
        String dateText = parts[1].trim();
        if (description.isEmpty()) {
            throw new Slay69Exception(
                    "Hello, a deadline needs a description before '/by'.");
        }
        if (dateText.isEmpty()) {
            throw new Slay69Exception(
                    "???, a deadline needs a date or time after '/by' right?!");
        }

        try {
            return new Deadline(description, LocalDate.parse(dateText));
        } catch (DateTimeParseException e) {
            throw new Slay69Exception(
                    "Invalid deadline date. Use yyyy-MM-dd, for example 2019-12-02.");
        }
    }

    /**
     * Separates an event description from its /from and /to values.
     *
     * @throws Slay69Exception if any required marker or field is missing
     */
    private static Event createEvent(String arguments)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "Okay lor, an event don't need a description hor. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String[] fromParts = arguments.split("/from", 2);
        if (fromParts.length < 2) {
            throw new Slay69Exception(
                    "Huh, An event needs '/from'. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String description = fromParts[0].trim();
        if (description.isEmpty()) {
            throw new Slay69Exception(
                    "Haiz, An event needs a description before '/from'.");
        }

        String[] toParts = fromParts[1].split("/to", 2);
        if (toParts.length < 2) {
            throw new Slay69Exception(
                    "Bruh, An event needs '/to'. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String from = toParts[0].trim();
        String to = toParts[1].trim();
        if (from.isEmpty()) {
            throw new Slay69Exception(
                    "Srsly, an event needs a start time after '/from'.");
        }
        if (to.isEmpty()) {
            throw new Slay69Exception(
                    "Walao eh, an event needs an end time after '/to'.");
        }
        return new Event(description, from, to);
    }

    /**
     * Parses the one-based task number supplied to a task command.
     */
    private static int parseTaskNumber(String arguments, String command)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "Please provide a task number. Try: " + command + " 1");
        }

        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new Slay69Exception(
                    "'" + arguments + "' is not a valid task number.");
        }
    }

    private static void requireNoArguments(String command, String arguments)
            throws Slay69Exception {
        if (!arguments.isEmpty()) {
            throw new Slay69Exception(
                    "The " + command + " command does not accept arguments.");
        }
    }

    private static void requireNoArgumentsForBye(String arguments)
            throws Slay69Exception {
        if (!arguments.isEmpty()) {
            throw new Slay69Exception(
                    "The bye command don't have arguments okay?! Try: bye");
        }
    }
}

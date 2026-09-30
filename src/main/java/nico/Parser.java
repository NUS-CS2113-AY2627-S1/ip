package nico;

import nico.command.AddCommand;
import nico.command.Command;
import nico.command.DeleteCommand;
import nico.command.ExitCommand;
import nico.command.FindCommand;
import nico.command.ListCommand;
import nico.command.MarkCommand;
import nico.command.OnCommand;
import nico.tasks.Deadline;
import nico.tasks.Event;
import nico.tasks.TaskDateTime;
import nico.tasks.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Makes sense of the text the user types, turning it into a Command.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String ON_COMMAND = "on";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private static final String BY_KEYWORD = "/by";
    private static final String FROM_KEYWORD = "/from";
    private static final String TO_KEYWORD = "/to";

    private static final String DEADLINE_FORMAT = "deadline <description> /by <date>";
    private static final String EVENT_FORMAT = "event <description> /from <start> /to <end>";

    /**
     * Converts a line of user input into the matching Command.
     *
     * @param fullCommand The full line the user typed, e.g. <code>mark 2</code>.
     * @return The command to execute.
     * @throws NicoException If the command is unknown or its details are missing or invalid.
     */
    public static Command parse(String fullCommand) throws NicoException {
        String[] words = fullCommand.trim().split(" ", 2);
        String commandWord = words[0].toLowerCase();
        String arguments = (words.length > 1) ? words[1].trim() : "";

        switch (commandWord) {
            case BYE_COMMAND:
                return new ExitCommand();
            case LIST_COMMAND:
                return new ListCommand();
            case MARK_COMMAND:
                return new MarkCommand(parseTaskIndex(arguments, MARK_COMMAND), true);
            case UNMARK_COMMAND:
                return new MarkCommand(parseTaskIndex(arguments, UNMARK_COMMAND), false);
            case DELETE_COMMAND:
                return new DeleteCommand(parseTaskIndex(arguments, DELETE_COMMAND));
            case FIND_COMMAND:
                return new FindCommand(parseKeyword(arguments));
            case ON_COMMAND:
                return new OnCommand(parseDate(arguments));
            case TODO_COMMAND:
                return new AddCommand(parseTodo(arguments));
            case DEADLINE_COMMAND:
                return new AddCommand(parseDeadline(arguments));
            case EVENT_COMMAND:
                return new AddCommand(parseEvent(arguments));
            default:
                throw new NicoException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Converts the task number typed by the user (1-based) into a 0-based index.
     */
    private static int parseTaskIndex(String arguments, String commandWord) throws NicoException {
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new NicoException("Please enter a valid task number, e.g. " + commandWord + " 2");
        }
    }

    private static String parseKeyword(String arguments) throws NicoException {
        if (arguments.isEmpty()) {
            throw new NicoException("Please tell me what to search for, e.g. find book");
        }
        return arguments;
    }

    private static LocalDate parseDate(String arguments) throws NicoException {
        try {
            return LocalDate.parse(arguments);
        } catch (DateTimeParseException e) {
            throw new NicoException("Please give a date as yyyy-mm-dd, e.g. on 2026-10-15");
        }
    }

    private static Todo parseTodo(String arguments) throws NicoException {
        if (arguments.isEmpty()) {
            throw new NicoException("The description of a todo cannot be empty.");
        }
        return new Todo(arguments);
    }

    private static Deadline parseDeadline(String arguments) throws NicoException {
        int byIndex = arguments.indexOf(BY_KEYWORD);
        if (byIndex == -1) {
            throw new NicoException("Please specify a deadline using: " + DEADLINE_FORMAT);
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + BY_KEYWORD.length()).trim();
        if (description.isEmpty()) {
            throw new NicoException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new NicoException("The /by date of a deadline cannot be empty.");
        }
        return new Deadline(description, TaskDateTime.parse(by));
    }

    private static Event parseEvent(String arguments) throws NicoException {
        int fromIndex = arguments.indexOf(FROM_KEYWORD);
        int toIndex = arguments.indexOf(TO_KEYWORD);
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new NicoException("Please specify an event using: " + EVENT_FORMAT);
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + FROM_KEYWORD.length(), toIndex).trim();
        String to = arguments.substring(toIndex + TO_KEYWORD.length()).trim();
        if (description.isEmpty()) {
            throw new NicoException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new NicoException("The /from and /to times of an event cannot be empty.");
        }
        TaskDateTime start = TaskDateTime.parse(from);
        TaskDateTime end = TaskDateTime.parse(to);
        if (end.isBefore(start)) {
            throw new NicoException("An event cannot end before it starts.");
        }
        return new Event(description, start, end);
    }
}
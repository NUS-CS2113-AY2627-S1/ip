package seedu.joey;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import seedu.joey.task.Deadline;
import seedu.joey.task.Event;
import seedu.joey.task.Task;
import seedu.joey.task.Todo;
/**
 * Makes sense of the user's input by extracting keywords, task indices,
 * and building Task objects from typed commands.
 */
public class Parser {
    /**
     * Returns the first word of the command in lowercase, used as the
     * command keyword by the main dispatcher.
     *
     * @param command The full command line entered by the user.
     * @return The first word of the command, lowercased.
     */
    public static String getKeyword(String command) {
        return command.split(" ")[0].toLowerCase();
    }

    /**
     * Parses the 1-based task number from the command and returns it as a
     * 0-based index into the task list.
     *
     * @param command  The full command line (e.g. "mark 2").
     * @param listSize The current size of the task list, used for bounds checking.
     * @return The 0-based task index.
     * @throws JoeyException If the task number is missing, not a number,
     *                       or out of range.
     */
    public static int parseTaskIndex(String command, int listSize) throws JoeyException {
        String[] words = command.split(" ");
        if(words.length < 2) {
            throw new JoeyException("Sorry, task number is missing. Please try again");
        }
        int index;
        try {
            index = Integer.parseInt(words[1]) - 1;
        } catch (NumberFormatException e) {
            throw new JoeyException(words[1] + " is not a number. Please try again. ");
        }
        if (index < 0 || index >= listSize) {
            throw new JoeyException("Sorry. There is no task with that number. Please try again.");
        }
        return index;
    }

    /**
     * Builds a Todo, Deadline or Event from the given command. The command
     * type (todo/deadline/event) is inferred from its first word.
     *
     * @param command The full command line (e.g. "deadline return book /by 2019-10-15").
     * @return A newly created Task matching the requested type.
     * @throws JoeyException If the description or any required date/time
     *                       component is missing or malformed.
     */
    public static Task parseTask(String command) throws JoeyException {
        String[] parts = command.split(" ", 2);
        String type = parts[0].toLowerCase();

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new JoeyException("Hmm, that " + type + " is missing a description. Please try again. ");
        }
        String details = parts[1].trim();

        if (type.equals("todo")) {
            return new Todo(details);
        } else if (type.equals("deadline")) {
            int byIndex = details.indexOf("/by");
            if (byIndex == -1) {
                throw new JoeyException("Sorry, you need a /by input. Please try again. ");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + "/by".length()).trim();
            if (description.isEmpty()) {
                throw new JoeyException("Sorry, your description is missing. Please try again.");
            }
            if (by.isEmpty()) {
                throw new JoeyException("Sorry, when is your task due?");
            }
            LocalDate byDate;
            try {
                byDate = LocalDate.parse(by);
            } catch (DateTimeParseException e) {
                throw new JoeyException("Sorry, use yyyy-mm-dd for the date (e.g. 2019-10-15). Please try again.");
            }
            return new Deadline(description, byDate);
        } else {
            int fromIndex = details.indexOf("/from");
            int toIndex = details.indexOf("/to");
            if (fromIndex == -1) {
                throw new JoeyException("Sorry, you need a /from input. PLease try again. ");
            }
            if (toIndex == -1) {
                throw new JoeyException("Sorry, you need a /to input. Please try again. ");
            }
            if (fromIndex > toIndex) {
                throw new JoeyException("/from must come before /to. PLease try again. ");
            }
            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
            String to = details.substring(toIndex + "/to".length()).trim();
            if (description.isEmpty()) {
                throw new JoeyException("Sorry, your description is missing. Please try again. ");
            }
            if (from.isEmpty()) {
                throw new JoeyException("Sorry, your start time is missing. Please try again. ");
            }
            if(to.isEmpty()) {
                throw new JoeyException("Sorry, your end time is missing. Please try again. ");
            }
            return new Event(description, from, to);
        }
         

    }

    /**
     * Returns the search keyword that follows the "find" command.
     *
     * @param command The full command line (e.g. "find book").
     * @return The keyword to search for, trimmed of whitespace.
     * @throws JoeyException If no keyword is supplied after "find".
     */
    public static String parseKeyword(String command) throws JoeyException {
        String[] parts = command.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new JoeyException("Please give me a keyword to find. ");
        }
        return parts[1].trim();
    }

}


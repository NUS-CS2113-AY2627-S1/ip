package glados;

import glados.task.Deadline;
import glados.task.Event;
import glados.task.TaskDateTime;
import glados.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Makes sense of the commands the user enters.
 *
 * <p>Works out which command was entered, and turns its arguments into task
 * numbers or new tasks, rejecting input that is not in the expected format.
 */
public class Parser {

    /** Command that ends the conversation. */
    public static final String COMMAND_BYE = "bye";

    /** Command that lists every stored task. */
    public static final String COMMAND_LIST = "list";

    /** Command word that marks a task as done, e.g. "mark 2". */
    public static final String COMMAND_MARK = "mark";

    /** Command word that marks a task as not done, e.g. "unmark 2". */
    public static final String COMMAND_UNMARK = "unmark";

    /** Command word that removes a task from the list, e.g. "delete 2". */
    public static final String COMMAND_DELETE = "delete";

    /** Command word that adds a todo task, e.g. "todo read book". */
    public static final String COMMAND_TODO = "todo";

    /** Command word that adds a deadline task, e.g. "deadline return book /by 2019-10-15". */
    public static final String COMMAND_DEADLINE = "deadline";

    /** Command word that adds an event task, e.g. "event camp /from 2019-10-15 /to 2019-10-17". */
    public static final String COMMAND_EVENT = "event";

    /** Command word that lists the tasks whose description contains a keyword, e.g. "find book". */
    public static final String COMMAND_FIND = "find";

    /** Command word that lists the tasks falling on a date, e.g. "on 2019-10-15". */
    public static final String COMMAND_ON = "on";

    /** Command words that are followed by arguments, in the order they are checked. */
    private static final String[] COMMANDS_WITH_ARGUMENTS = {
        COMMAND_MARK, COMMAND_UNMARK, COMMAND_DELETE, COMMAND_TODO, COMMAND_DEADLINE, COMMAND_EVENT,
        COMMAND_FIND, COMMAND_ON
    };

    /** Separates a deadline's description from its due date. */
    private static final String DELIMITER_BY = "/by";

    /** Separates an event's description from its start. */
    private static final String DELIMITER_FROM = "/from";

    /** Separates an event's start from its end. */
    private static final String DELIMITER_TO = "/to";

    /** Separates fields in the data file, so it cannot appear inside a task. */
    private static final String RESERVED_CHARACTER = "|";

    /**
     * Date formats the user may type, e.g. "2019-10-15" or "2/12/2019" (day first).
     * STRICT rejects dates that do not exist, such as 2019-02-30, and needs "uuuu" for the year.
     */
    private static final DateTimeFormatter[] DATE_INPUT_FORMATS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT)
    };

    /** Date and 24-hour time formats the user may type, e.g. "2019-10-15 0930" or "2/12/2019 1800". */
    private static final DateTimeFormatter[] DATE_TIME_INPUT_FORMATS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT),
        DateTimeFormatter.ofPattern("d/M/uuuu HHmm").withResolverStyle(ResolverStyle.STRICT)
    };

    /**
     * Prevents creating Parser objects. Every method is static, since parsing keeps
     * no state between commands, so methods are called as e.g. Parser.parseTodo(input).
     */
    private Parser() {
    }

    /**
     * Returns true if the input is the command that ends the conversation.
     */
    public static boolean isExit(String input) {
        return input.equals(COMMAND_BYE);
    }

    /**
     * Returns the command word of the given input, e.g. "mark" for "mark 2".
     *
     * <p>"list" must be entered on its own. Other command words must be the whole
     * input or be followed by a space, so "todox" is not taken as "todo".
     *
     * @param input the full command entered by the user.
     * @return one of the COMMAND_ constants, other than COMMAND_BYE.
     * @throws GLaDOSException if the input does not start with a known command.
     */
    public static String parseCommandWord(String input) throws GLaDOSException {
        if (input.equals(COMMAND_LIST)) {
            return COMMAND_LIST;
        }
        for (String commandWord : COMMANDS_WITH_ARGUMENTS) {
            if (input.equals(commandWord) || input.startsWith(commandWord + " ")) {
                return commandWord;
            }
        }
        throw new GLaDOSException("I have no idea what that was. Try one of: "
                + "list, todo, deadline, event, mark, unmark, delete, find, on, bye.");
    }

    /**
     * Parses the task number after a command word, e.g. "mark 2", into a 0-based index.
     *
     * @param input the full command entered by the user.
     * @param commandWord the command word to strip off, e.g. "mark".
     * @param taskCount how many tasks exist, used to check the number is in range.
     * @return the 0-based index of the task.
     * @throws GLaDOSException if no number was given, it is not a number, or no such task exists.
     */
    public static int parseTaskIndex(String input, String commandWord, int taskCount)
            throws GLaDOSException {
        String argument = input.substring(commandWord.length()).trim();
        if (argument.isEmpty()) {
            throw new GLaDOSException("Which task? Give me a number, like " + commandWord + " 2.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new GLaDOSException("\"" + argument + "\" is not a task number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new GLaDOSException("There is no task " + taskNumber
                    + ". Your list has " + taskCount + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Parses a todo command into a Todo task.
     *
     * @param input the full command, e.g. "todo read book".
     * @return the new todo.
     * @throws GLaDOSException if no description was given, or it contains a reserved character.
     */
    public static Todo parseTodo(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String description = input.substring(COMMAND_TODO.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("A todo with no description. Try again, with words this time.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param input the full command, e.g. "deadline return book /by 2/12/2019 1800".
     * @return the new deadline.
     * @throws GLaDOSException if the /by is missing, either part around it is empty,
     *         the due date is not a valid date, or the input contains a reserved character.
     */
    public static Deadline parseDeadline(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String details = input.substring(COMMAND_DEADLINE.length()).trim();
        int byIndex = details.indexOf(DELIMITER_BY);
        if (byIndex == -1) {
            throw new GLaDOSException("A deadline needs a /by. "
                    + "Try: deadline return book /by 2019-10-15.");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + DELIMITER_BY.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("A deadline with no description. What am I meant to track?");
        }
        if (by.isEmpty()) {
            throw new GLaDOSException("You left the /by empty. When is this due?");
        }
        return new Deadline(description, parseDateTime(by));
    }

    /**
     * Parses the keyword after the "find" command word, e.g. "book" in "find book".
     *
     * <p>Everything after the command word is the keyword, so it may contain
     * spaces, e.g. "find return book".
     *
     * @param input the full command entered by the user.
     * @return the keyword, without surrounding spaces.
     * @throws GLaDOSException if no keyword was given.
     */
    public static String parseFindKeyword(String input) throws GLaDOSException {
        String keyword = input.substring(COMMAND_FIND.length()).trim();
        if (keyword.isEmpty()) {
            throw new GLaDOSException("Find what? Give me a keyword, like find book.");
        }
        return keyword;
    }

    /**
     * Parses the date after the "on" command word, e.g. "on 2019-10-15".
     *
     * <p>Only a date is accepted, since the command asks about a whole day.
     *
     * @param input the full command entered by the user.
     * @return the date asked about.
     * @throws GLaDOSException if no date was given, or it is not in an accepted form.
     */
    public static LocalDate parseOnDate(String input) throws GLaDOSException {
        String argument = input.substring(COMMAND_ON.length()).trim();
        if (argument.isEmpty()) {
            throw new GLaDOSException("Which date? Try: on 2019-10-15.");
        }
        for (DateTimeFormatter format : DATE_INPUT_FORMATS) {
            try {
                return LocalDate.parse(argument, format);
            } catch (DateTimeParseException e) {
                // Not in this format, so try the next one.
            }
        }
        throw new GLaDOSException("\"" + argument + "\" is not a date I understand. "
                + "Try 2019-10-15 or 2/12/2019, without a time.");
    }

    /**
     * Parses a date the user typed, with an optional 24-hour time after it.
     *
     * <p>Accepted forms are "2019-10-15" and "2/12/2019" (day first), each optionally
     * followed by a time such as "1800".
     *
     * @param text the date as typed, e.g. "2/12/2019 1800".
     * @return the date, with the time if one was given.
     * @throws GLaDOSException if the text is not in an accepted form, or is not a real date or time.
     */
    public static TaskDateTime parseDateTime(String text) throws GLaDOSException {
        for (DateTimeFormatter format : DATE_TIME_INPUT_FORMATS) {
            try {
                return new TaskDateTime(LocalDateTime.parse(text, format));
            } catch (DateTimeParseException e) {
                // Not in this format, so try the next one.
            }
        }
        for (DateTimeFormatter format : DATE_INPUT_FORMATS) {
            try {
                return new TaskDateTime(LocalDate.parse(text, format));
            } catch (DateTimeParseException e) {
                // Not in this format, so try the next one.
            }
        }
        throw new GLaDOSException("\"" + text + "\" is not a date I understand. "
                + "Try 2019-10-15 or 2/12/2019, with an optional time like 1800.");
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param input the full command, e.g. "event camp /from 2019-10-15 /to 2019-10-17".
     * @return the new event.
     * @throws GLaDOSException if /from or /to is missing or out of order, any part is empty,
     *         either is not a valid date, the event ends before it starts,
     *         or the input contains a reserved character.
     */
    public static Event parseEvent(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String details = input.substring(COMMAND_EVENT.length()).trim();
        int fromIndex = details.indexOf(DELIMITER_FROM);
        int toIndex = details.indexOf(DELIMITER_TO);
        if (fromIndex == -1 || toIndex == -1) {
            throw new GLaDOSException("An event needs both a /from and a /to. "
                    + "Try: event camp /from 2019-10-15 /to 2019-10-17.");
        }
        if (toIndex < fromIndex) {
            throw new GLaDOSException("The /to has to come after the /from.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = details.substring(toIndex + DELIMITER_TO.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("An event with no description. What am I meant to track?");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new GLaDOSException("An event needs both a start and an end date.");
        }

        TaskDateTime start = parseDateTime(from);
        TaskDateTime end = parseDateTime(to);
        if (end.isBefore(start)) {
            throw new GLaDOSException("This event ends before it starts. Check your /from and /to.");
        }
        return new Event(description, start, end);
    }

    /**
     * Rejects task input containing the character used to separate fields in the data file.
     *
     * @throws GLaDOSException if input contains that character.
     */
    private static void checkNoReservedCharacter(String input) throws GLaDOSException {
        if (input.contains(RESERVED_CHARACTER)) {
            throw new GLaDOSException("The " + RESERVED_CHARACTER
                    + " character is reserved for my records. Leave it out.");
        }
    }
}

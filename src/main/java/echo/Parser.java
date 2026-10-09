package echo;

import java.time.LocalDate;

/**
 * Turns raw user input into a {@link Command} ready to execute. Every method
 * either returns the parsed result or throws {@link EchoException} with a
 * user-facing explanation -- this class never prints anything itself.
 */
public class Parser {

    /** Prevents instantiation: this class only has static methods. */
    private Parser() {
    }

    /**
     * Parses one line of user input into the command it describes.
     *
     * @param fullCommand the full line the user typed, or {@code null} if input has ended.
     * @return the command to execute; end of input is treated as {@code bye}.
     * @throws EchoException if the line is not a recognized command, or its arguments are invalid.
     */
    public static Command parse(String fullCommand) throws EchoException {
        if (fullCommand == null || fullCommand.equals("bye")) {
            return new ExitCommand();
        } else if (fullCommand.equals("list")) {
            return new ListCommand();
        } else if (fullCommand.equals("find") || fullCommand.startsWith("find ")) {
            return new FindCommand(parseKeyword(fullCommand));
        } else if (fullCommand.equals("on") || fullCommand.startsWith("on ")) {
            return new OnCommand(parseOnDate(fullCommand));
        } else if (fullCommand.equals("mark") || fullCommand.startsWith("mark ")) {
            return new MarkCommand(parseTaskNumber(fullCommand, "mark"));
        } else if (fullCommand.equals("unmark") || fullCommand.startsWith("unmark ")) {
            return new UnmarkCommand(parseTaskNumber(fullCommand, "unmark"));
        } else if (fullCommand.equals("delete") || fullCommand.startsWith("delete ")) {
            return new DeleteCommand(parseTaskNumber(fullCommand, "delete"));
        } else if (fullCommand.equals("todo") || fullCommand.startsWith("todo ")) {
            return new AddCommand(new Todo(requireDescription(fullCommand, "todo", "a todo")));
        } else if (fullCommand.equals("deadline") || fullCommand.startsWith("deadline ")) {
            return new AddCommand(parseDeadline(fullCommand));
        } else if (fullCommand.equals("event") || fullCommand.startsWith("event ")) {
            return new AddCommand(parseEvent(fullCommand));
        } else {
            throw new EchoException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Returns the text after a command word (e.g. {@code "todo "}), rejecting
     * it if empty or if it contains {@code |}, the field separator of the save
     * file -- a {@code |} inside a task would split it into the wrong fields
     * when the file is loaded again.
     *
     * @param userInput      the full command line.
     * @param commandWord    the command word without a trailing space, e.g. {@code "todo"}.
     * @param taskNounPhrase the task kind with its article, e.g. {@code "a todo"}, {@code "an event"}.
     * @return the trimmed, non-empty description.
     * @throws EchoException if no description follows the command word, or it contains {@code |}.
     */
    private static String requireDescription(String userInput, String commandWord, String taskNounPhrase)
            throws EchoException {
        String description = userInput.equals(commandWord)
                ? ""
                : userInput.substring(commandWord.length() + 1).trim();
        if (description.isEmpty()) {
            throw new EchoException("The description of " + taskNounPhrase + " cannot be empty.");
        }
        if (description.contains("|")) {
            throw new EchoException("Tasks cannot contain the \"|\" character, "
                    + "because Echo uses it to separate fields in its save file.");
        }
        return description;
    }

    /**
     * Parses the text after {@code deadline }, of the form
     * {@code <description> /by <yyyy-mm-dd>}.
     *
     * @param userInput the full command line starting with {@code deadline}.
     * @return the parsed deadline.
     * @throws EchoException if the description or the {@code /by} date is missing or invalid.
     */
    private static Deadline parseDeadline(String userInput) throws EchoException {
        String description = requireDescription(userInput, "deadline", "a deadline");
        String[] parts = description.split(" /by ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EchoException("A deadline needs a due date, e.g. "
                    + "\"deadline return book /by 2019-10-15\".");
        }
        if (parts[0].trim().isEmpty()) {
            throw new EchoException("The description of a deadline cannot be empty.");
        }
        return new Deadline(parts[0].trim(), Dates.parse(parts[1].trim()));
    }

    /**
     * Parses the text after {@code event }, of the form
     * {@code <description> /from <yyyy-mm-dd> /to <yyyy-mm-dd>}.
     *
     * @param userInput the full command line starting with {@code event}.
     * @return the parsed event.
     * @throws EchoException if the description, {@code /from} or {@code /to} part is missing,
     *                       a date is invalid, or the event ends before it starts.
     */
    private static Event parseEvent(String userInput) throws EchoException {
        String description = requireDescription(userInput, "event", "an event");
        String[] fromParts = description.split(" /from ", 2);
        if (fromParts.length < 2 || fromParts[1].trim().isEmpty()) {
            throw new EchoException("An event needs a start and end date, e.g. "
                    + "\"event meeting /from 2019-10-15 /to 2019-10-16\".");
        }
        if (fromParts[0].trim().isEmpty()) {
            throw new EchoException("The description of an event cannot be empty.");
        }
        String[] toParts = fromParts[1].split(" /to ", 2);
        if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
            throw new EchoException("An event needs both /from and /to, e.g. "
                    + "\"event meeting /from 2019-10-15 /to 2019-10-16\".");
        }
        LocalDate from = Dates.parse(toParts[0].trim());
        LocalDate to = Dates.parse(toParts[1].trim());
        if (to.isBefore(from)) {
            throw new EchoException("An event cannot end before it starts.");
        }
        return new Event(fromParts[0].trim(), from, to);
    }

    /**
     * Returns the date after {@code on}, rejecting it if empty or invalid.
     *
     * @param userInput the full command line starting with {@code on}.
     * @return the parsed date.
     * @throws EchoException if no date follows {@code on}, or it is not a valid {@code yyyy-mm-dd} date.
     */
    private static LocalDate parseOnDate(String userInput) throws EchoException {
        String dateText = userInput.equals("on")
                ? ""
                : userInput.substring("on ".length()).trim();
        if (dateText.isEmpty()) {
            throw new EchoException("Tell me which date to check, e.g. \"on 2019-10-15\".");
        }
        return Dates.parse(dateText);
    }

    /**
     * Returns the keyword after {@code find}, rejecting it if empty.
     *
     * @param userInput the full command line starting with {@code find}.
     * @return the trimmed, non-empty keyword.
     * @throws EchoException if no keyword follows {@code find}.
     */
    private static String parseKeyword(String userInput) throws EchoException {
        String keyword = userInput.equals("find")
                ? ""
                : userInput.substring("find ".length()).trim();
        if (keyword.isEmpty()) {
            throw new EchoException("Tell me what to search for, e.g. \"find book\".");
        }
        return keyword;
    }

    /**
     * Parses the task number after {@code mark}/{@code unmark}/{@code delete}.
     * Whether a task with that number exists is checked later by the command,
     * which has access to the task list.
     *
     * @param userInput the full command line, e.g. {@code "mark 2"}.
     * @param command   the command word, e.g. {@code "mark"}, {@code "unmark"} or {@code "delete"}.
     * @return the 1-based task number as typed.
     * @throws EchoException if the number is missing or not a number.
     */
    private static int parseTaskNumber(String userInput, String command) throws EchoException {
        String argument = userInput.equals(command)
                ? ""
                : userInput.substring(command.length() + 1).trim();
        if (argument.isEmpty()) {
            throw new EchoException("Tell me which task number to " + command
                    + ", e.g. \"" + command + " 2\".");
        }
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new EchoException("\"" + argument + "\" is not a task number.");
        }
    }
}

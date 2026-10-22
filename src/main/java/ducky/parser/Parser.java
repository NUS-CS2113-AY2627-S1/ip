package ducky.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import ducky.chore.Chore;
import ducky.chore.Deadline;
import ducky.chore.Event;
import ducky.chore.ToDo;
import ducky.command.AddChoreCommand;
import ducky.command.Command;
import ducky.command.DeleteCommand;
import ducky.command.ExitCommand;
import ducky.command.FindCommand;
import ducky.command.ListCommand;
import ducky.command.MarkCommand;
import ducky.command.UnmarkCommand;

/**
 * Makes sense of the command lines typed by the user: recognising the
 * command keyword, pulling out the arguments that follow it, and turning
 * each command line into the {@link Command} that carries it out. Reports a
 * {@link DuckyException} when a command is malformed. This is a utility
 * class of static methods and is never instantiated.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Parses one full command line into the command that carries it out.
     *
     * @param input Full command line entered by the user.
     * @return The command the input line describes.
     * @throws DuckyException If the input is not a recognised command, or is a recognised
     *         command that is malformed (e.g. a missing description or separator).
     */
    public static Command parse(String input) throws DuckyException {
        if (input.equals("bai")) {
            return new ExitCommand();
        } else if (input.equals("list")) {
            return new ListCommand();
        } else if (input.startsWith("mark")) {
            return new MarkCommand(parseChoreIndex(input, "mark"));
        } else if (input.startsWith("unmark")) {
            return new UnmarkCommand(parseChoreIndex(input, "unmark"));
        } else if (input.startsWith("delete")) {
            return new DeleteCommand(parseChoreIndex(input, "delete"));
        } else if (input.startsWith("find")) {
            return new FindCommand(parseKeyword(input));
        } else if (input.startsWith("todo")) {
            return new AddChoreCommand(parseToDo(input));
        } else if (input.startsWith("deadline")) {
            return new AddChoreCommand(parseDeadline(input));
        } else if (input.startsWith("event")) {
            return new AddChoreCommand(parseEvent(input));
        } else {
            throw new DuckyException("QUACK?! I don't know \"" + input + "\". "
                    + "Try: list, find, todo, deadline, event, mark, unmark, delete, bai");
        }
    }

    /**
     * Parses the rank following a "mark", "unmark" or "delete" keyword into
     * a zero-based chore index. Only the syntax is checked here (the rank
     * must be a whole number); whether that index actually exists in the
     * current chore list is checked later, once the command executes and
     * has access to the chore list.
     *
     * @param input Full command line entered by the user.
     * @param keyword The command keyword ("mark", "unmark" or "delete") preceding the rank.
     * @return The zero-based chore index the rank refers to.
     * @throws DuckyException If the rank is missing or not a number.
     */
    private static int parseChoreIndex(String input, String keyword) throws DuckyException {
        String rankText = input.substring(keyword.length()).trim();
        try {
            return Integer.parseInt(rankText) - 1;
        } catch (NumberFormatException e) {
            throw new DuckyException("QUACK?! \"" + rankText + "\" isn't a chore number.");
        }
    }

    /**
     * Parses the keyword following a "find" command.
     *
     * @param input Full command line entered by the user.
     * @return The keyword to search chore descriptions for.
     * @throws DuckyException If the keyword is missing.
     */
    private static String parseKeyword(String input) throws DuckyException {
        String keyword = input.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new DuckyException("QUACK?! What should I find? e.g. find book");
        }
        return keyword;
    }

    /**
     * Parses a to-do chore from a "todo &lt;description&gt;" command.
     *
     * @param input Full command line entered by the user.
     * @return The to-do chore described by the command.
     * @throws DuckyException If the description is missing.
     */
    private static Chore parseToDo(String input) throws DuckyException {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new DuckyException("QUACK?! A todo needs a description, e.g. todo read book");
        }
        return new ToDo(description);
    }

    /**
     * Parses a deadline chore from a "deadline &lt;description&gt; /by &lt;dd-mm-yyyy&gt;" command.
     *
     * @param input Full command line entered by the user.
     * @return The deadline chore described by the command.
     * @throws DuckyException If the "/by" separator, the description or the due date is missing,
     *         or the due date is not a valid date in dd-mm-yyyy form.
     */
    private static Chore parseDeadline(String input) throws DuckyException {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split("/by", 2);
        if (parts.length < 2) {
            throw new DuckyException("QUACK?! A deadline needs a /by, e.g. deadline return book /by 15-10-2019");
        }
        String description = parts[0].trim();
        String byText = parts[1].trim();
        if (description.isEmpty()) {
            throw new DuckyException("QUACK?! A deadline needs a description, "
                    + "e.g. deadline return book /by 15-10-2019");
        }
        if (byText.isEmpty()) {
            throw new DuckyException("QUACK?! A deadline needs a date after /by, "
                    + "e.g. deadline return book /by 15-10-2019");
        }
        return new Deadline(description, parseDate(byText));
    }

    /**
     * Parses a date typed by the user in dd-mm-yyyy form (e.g. 15-10-2019).
     *
     * @param dateText The date text typed by the user.
     * @return The date the text describes.
     * @throws DuckyException If the text is not a valid date in dd-mm-yyyy form.
     */
    private static LocalDate parseDate(String dateText) throws DuckyException {
        try {
            // INPUT_DATE_FORMAT resolves strictly, so impossible dates like 30-02-2019 are rejected.
            return LocalDate.parse(dateText, Deadline.INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new DuckyException("QUACK?! \"" + dateText + "\" isn't a date I understand. "
                    + "Use dd-mm-yyyy, e.g. 15-10-2019");
        }
    }

    /**
     * Parses an event chore from an
     * "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;" command.
     *
     * @param input Full command line entered by the user.
     * @return The event chore described by the command.
     * @throws DuckyException If the "/from" or "/to" separator, the description, the start time or
     *         the end time is missing.
     */
    private static Chore parseEvent(String input) throws DuckyException {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split("/from", 2);
        if (parts.length < 2) {
            throw new DuckyException("QUACK?! An event needs a /from, e.g. event meeting /from Mon 2pm /to 4pm");
        }
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new DuckyException("QUACK?! An event needs a description, e.g. event meeting /from Mon 2pm /to 4pm");
        }
        String[] timeParts = parts[1].split("/to", 2);
        if (timeParts.length < 2) {
            throw new DuckyException("QUACK?! An event needs a /to, e.g. event meeting /from Mon 2pm /to 4pm");
        }
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        if (from.isEmpty()) {
            throw new DuckyException("QUACK?! An event needs a start time after /from, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
        }
        if (to.isEmpty()) {
            throw new DuckyException("QUACK?! An event needs an end time after /to, "
                    + "e.g. event meeting /from Mon 2pm /to 4pm");
        }
        return new Event(description, from, to);
    }
}

package turing;

import java.time.LocalDate;

import turing.command.AddCommand;
import turing.command.Command;
import turing.command.CommandWord;
import turing.command.DeleteCommand;
import turing.command.ExitCommand;
import turing.command.FindCommand;
import turing.command.ListCommand;
import turing.command.MarkCommand;
import turing.command.OnCommand;
import turing.task.Deadline;
import turing.task.Event;
import turing.task.TaskTime;
import turing.task.Todo;

/**
 * Makes sense of the lines the user types, turning each one into the command
 * it asks for. Keeping the shape of every command here means the rest of the
 * chatbot never has to pick a line of text apart, and a change to the
 * accepted syntax touches only this class.
 */
public class Parser {
    // The separators below are regular expressions rather than plain text, so that
    // "(?i)" can make them case insensitive and "\\s*" can absorb any spaces around
    // them. That way "/BY Sunday" and "book/by Sunday" are understood too.

    /** Pattern matching the "/by" separator of a deadline command. */
    private static final String BY_SEPARATOR_PATTERN = "(?i)\\s*/by\\s*";

    /** Pattern matching the "/from" separator of an event command. */
    private static final String FROM_SEPARATOR_PATTERN = "(?i)\\s*/from\\s*";

    /** Pattern matching the "/to" separator of an event command. */
    private static final String TO_SEPARATOR_PATTERN = "(?i)\\s*/to\\s*";

    /** Reminder of the shape an event command has to take. */
    private static final String EVENT_USAGE =
            "Please use: event <task> /from <start> /to <end>, "
                    + "e.g. event meeting /from 2019-10-15 1400 /to 2019-10-15 1600";

    /** Parsing needs no state of its own, so the class is never instantiated. */
    private Parser() {
    }

    /**
     * Returns the given text with surrounding whitespace removed and every run
     * of internal whitespace collapsed into a single space. Cleaning the input
     * up front lets the rest of the chatbot treat "  mark    2  " exactly like
     * "mark 2", so no other code has to worry about stray spaces or tabs.
     *
     * @param text Raw line typed by the user.
     * @return Text with normalized whitespace.
     */
    private static String normalizeWhitespace(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns the command one line of user input asks for, already carrying
     * whatever that command needs to know.
     *
     * @param rawInput One line of input, exactly as the user typed it.
     * @return Command ready to be carried out.
     * @throws TuringException If the input names no known command, or names one
     *         but does not give it what it needs.
     */
    public static Command parse(String rawInput) throws TuringException {
        String input = normalizeWhitespace(rawInput);
        CommandWord commandWord = parseCommandWord(input);
        String argument = parseArgument(input);

        // A switch expression over the enum means the compiler, rather than a
        // reader, checks that every command word can be built into a command.
        return switch (commandWord) {
        case TODO -> new AddCommand(parseTodo(argument));
        case DEADLINE -> new AddCommand(parseDeadline(argument));
        case EVENT -> new AddCommand(parseEvent(argument));
        case LIST -> new ListCommand();
        case FIND -> new FindCommand(parseSearchText(argument));
        case ON -> new OnCommand(parseDate(argument, commandWord));
        case MARK -> new MarkCommand(parseTaskNumber(argument, commandWord), true);
        case UNMARK -> new MarkCommand(parseTaskNumber(argument, commandWord), false);
        case DELETE -> new DeleteCommand(parseTaskNumber(argument, commandWord));
        case BYE -> new ExitCommand();
        };
    }

    /**
     * Returns the command word named by the first word of the input.
     *
     * @param input One line of input, with its whitespace already normalized.
     * @return Command word the user typed.
     * @throws TuringException If the input is blank or names no known command.
     */
    private static CommandWord parseCommandWord(String input) throws TuringException {
        // A blank line is almost certainly a stray Enter, so ask again
        // instead of treating it as a command.
        if (input.isEmpty()) {
            throw new TuringException("Please type something so I know what to do.", suggestKeywords());
        }

        String keyword = splitIntoKeywordAndArgument(input)[0];
        CommandWord commandWord = CommandWord.fromKeyword(keyword);
        if (commandWord == null) {
            throw new TuringException("Sorry, I don't know what \"" + keyword + "\" means.", suggestKeywords());
        }
        return commandWord;
    }

    /**
     * Returns everything after the command word, which is what the command
     * works on.
     *
     * @param input One line of input, with its whitespace already normalized.
     * @return Text after the first word, or an empty string if there is none.
     */
    private static String parseArgument(String input) {
        String[] words = splitIntoKeywordAndArgument(input);
        return words.length > 1 ? words[1] : "";
    }

    /**
     * Returns the todo described by the text after the "todo" command word.
     *
     * @param argument Text after the command word, which is the description.
     * @return Todo carrying that description.
     * @throws TuringException If the description is missing.
     */
    private static Todo parseTodo(String argument) throws TuringException {
        if (argument.isEmpty()) {
            throw new TuringException("A todo needs a description, or I have nothing to remember.",
                    "Please use: todo <task>, e.g. todo borrow book");
        }

        return new Todo(argument);
    }

    /**
     * Returns the deadline described by the text after the "deadline" command
     * word, which is expected to read {@code <task> /by <when>}.
     *
     * @param argument Text after the command word.
     * @return Deadline carrying that description and due date.
     * @throws TuringException If the task or the due date is missing.
     */
    private static Deadline parseDeadline(String argument) throws TuringException {
        String[] descriptionAndBy = splitAtSeparator(argument, BY_SEPARATOR_PATTERN);
        if (descriptionAndBy == null) {
            throw new TuringException("A deadline needs a task and a due date, separated by /by.",
                    "Please use: deadline <task> /by <when>, e.g. deadline return book /by 2019-10-15");
        }

        return new Deadline(descriptionAndBy[0], descriptionAndBy[1]);
    }

    /**
     * Returns the event described by the text after the "event" command word,
     * which is expected to read {@code <task> /from <start> /to <end>}.
     *
     * @param argument Text after the command word.
     * @return Event carrying that description, start and end.
     * @throws TuringException If the task, the start or the end is missing.
     */
    private static Event parseEvent(String argument) throws TuringException {
        String[] descriptionAndTimes = splitAtSeparator(argument, FROM_SEPARATOR_PATTERN);
        if (descriptionAndTimes == null) {
            throw new TuringException("An event needs a task and a start time, separated by /from.", EVENT_USAGE);
        }

        String[] startAndEnd = splitAtSeparator(descriptionAndTimes[1], TO_SEPARATOR_PATTERN);
        if (startAndEnd == null) {
            throw new TuringException("An event needs an end time after its start time, separated by /to.",
                    EVENT_USAGE);
        }

        return new Event(descriptionAndTimes[0], startAndEnd[0], startAndEnd[1]);
    }

    /**
     * Returns the text a "find" command searches for.
     *
     * @param argument Text after the command word.
     * @return Text to look for in a task description.
     * @throws TuringException If nothing was given to search for.
     */
    private static String parseSearchText(String argument) throws TuringException {
        if (argument.isEmpty()) {
            throw new TuringException("Please tell me what to search for.",
                    "Please use: find <text>, e.g. find book");
        }

        return argument;
    }

    /**
     * Returns the date typed after a command word such as "on". Unlike the
     * date carried by a task, this one has to be a date the chatbot
     * recognizes: there is nothing to look for otherwise.
     *
     * @param argument Text after the command word.
     * @param commandWord Command the user typed, quoted back in any error message.
     * @return Date the user asked about.
     * @throws TuringException If the text is missing or is not a date.
     */
    private static LocalDate parseDate(String argument, CommandWord commandWord) throws TuringException {
        String keyword = commandWord.getKeyword();
        String usage = "Please use: " + keyword + " <date>, e.g. " + keyword + " 2019-10-15";
        if (argument.isEmpty()) {
            throw new TuringException("Please tell me which day to look at.", usage);
        }

        // Asking a TaskTime keeps one idea of what a date looks like, shared
        // with the dates tasks carry.
        LocalDate date = TaskTime.of(argument).getDate();
        if (date == null) {
            throw new TuringException("I need a date written as yyyy-MM-dd, and \"" + argument + "\" is not one.",
                    usage);
        }
        return date;
    }

    /**
     * Returns the task number typed after a command word such as "mark".
     *
     * @param argument Text after the command word.
     * @param commandWord Command the user typed, quoted back in any error message.
     * @return Task number as shown to the user, starting at 1.
     * @throws TuringException If the text is missing or is not a whole number.
     */
    private static int parseTaskNumber(String argument, CommandWord commandWord) throws TuringException {
        String keyword = commandWord.getKeyword();
        String usage = "Please use: " + keyword + " <task number>, e.g. " + keyword + " 2";
        if (argument.isEmpty()) {
            throw new TuringException("Please tell me which task to " + keyword + ".", usage);
        }

        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            // The user typed something like "mark two", or a number too large to hold.
            throw new TuringException("I need a task number, and \"" + argument + "\" is not one.", usage);
        }
    }

    /**
     * Returns the advice offered whenever the input names no command, which
     * lists the words the chatbot does answer to.
     *
     * @return Sentence naming every command keyword.
     */
    private static String suggestKeywords() {
        return "Try one of: " + CommandWord.getKeywords() + ".";
    }

    /**
     * Splits the input into the word naming the command and everything after it.
     *
     * @param input One line of input, with its whitespace already normalized.
     * @return One part if the input is a single word, two parts otherwise.
     */
    private static String[] splitIntoKeywordAndArgument(String input) {
        // The limit of 2 keeps any remaining spaces inside the argument itself.
        return input.split(" ", 2);
    }

    /**
     * Splits text into the part before the separator and the part after it.
     * Both parts have to carry text for the split to count as successful, so
     * "return book /by" and "/by Sunday" are both rejected.
     *
     * @param text Text to split, such as "return book /by Sunday".
     * @param separatorPattern Regular expression matching the separator.
     * @return The two parts, or null if the separator is missing or a part is blank.
     */
    private static String[] splitAtSeparator(String text, String separatorPattern) {
        // Limit of 2 keeps any later occurrence of the separator inside the second part,
        // so "/by the 2nd /by lunchtime" is a due date rather than another split point.
        String[] parts = text.split(separatorPattern, 2);
        boolean hasBothParts = parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank();
        return hasBothParts ? parts : null;
    }
}

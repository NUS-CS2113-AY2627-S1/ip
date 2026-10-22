/**
 * Makes sense of what the user typed.
 * <p>
 * Everything here turns text into something the rest of the program can use: a
 * command word, the text that follows it, a task number, or a finished
 * {@link Task}. Nothing here touches the task list or the screen, so the rules
 * about where a description ends and a date begins are all in one file and can
 * be read without reference to anything else.
 * <p>
 * The methods are static because parsing depends only on its argument; there is
 * no parser state worth keeping between commands.
 */
public class Parser {

    /**
     * The character {@link Storage} divides a task's fields with, and which a
     * task's own text therefore may not contain.
     */
    private static final String FIELD_SEPARATOR = "|";

    /** Prevents instantiation: this class is a collection of functions. */
    private Parser() {
    }

    /**
     * Returns the command the user asked for, ready to be carried out.
     * <p>
     * This is the one place that decides what a command word means. A word that
     * means nothing is refused here rather than later, since nothing further can
     * usefully be done with it.
     *
     * @param userInput one line exactly as the user typed it.
     * @return the command the user asked for.
     * @throws CortisolException if the command word is unknown, or its arguments
     *                           cannot be read.
     */
    public static Command parse(String userInput) throws CortisolException {
        String commandWord = parseCommandWord(userInput);
        String arguments = parseArguments(userInput);

        switch (commandWord) {
        case "list":
            return new ListCommand();
        case "find":
            return new FindCommand(parseKeyword(arguments));
        case "todo":
            return new AddCommand(parseTodo(arguments));
        case "deadline":
            return new AddCommand(parseDeadline(arguments));
        case "event":
            return new AddCommand(parseEvent(arguments));
        case "mark":
            return new MarkCommand(parseTaskNumber(arguments, "mark"));
        case "unmark":
            return new UnmarkCommand(parseTaskNumber(arguments, "unmark"));
        case "delete":
            return new DeleteCommand(parseTaskNumber(arguments, "delete"));
        case "bye":
            return new ExitCommand();
        default:
            throw new CortisolException("I do beg your pardon, sir/madam, but that "
                    + "instruction is not in my repertoire.\n"
                    + " I can manage: todo, deadline, event, list, find, mark, unmark, "
                    + "delete, bye.");
        }
    }

    /**
     * Returns the first word of a line, which names the command.
     * <p>
     * A line of nothing but spaces yields an empty command word, which no
     * command matches, so it is refused like any other unknown instruction.
     *
     * @param userInput one line exactly as the user typed it.
     * @return the command word, without surrounding spaces.
     */
    public static String parseCommandWord(String userInput) {
        // Splitting on a run of whitespace, with a limit of 2, keeps the rest
        // of the line in one piece however the user spaced it out.
        return userInput.trim().split("\\s+", 2)[0];
    }

    /**
     * Returns everything after the command word.
     *
     * @param userInput one line exactly as the user typed it.
     * @return the text following the command word, without surrounding spaces,
     *         or an empty string if the command was typed on its own.
     */
    public static String parseArguments(String userInput) {
        String[] inputParts = userInput.trim().split("\\s+", 2);
        return inputParts.length > 1 ? inputParts[1].trim() : "";
    }

    /**
     * Reads the task number out of the argument of a command that refers to a
     * task by number.
     * <p>
     * Whether the number refers to an existing task is not decided here: that
     * belongs to {@link TaskList}, which is the only place that knows how many
     * tasks there are.
     *
     * @param arguments text following the command word.
     * @param commandWord command the user typed, used to phrase the error messages.
     * @return the number the user typed, counting from 1.
     * @throws CortisolException if no number was given, or the text is not a number.
     */
    public static int parseTaskNumber(String arguments, String commandWord)
            throws CortisolException {
        if (arguments.isEmpty()) {
            throw new CortisolException("Which task shall I " + commandWord + ", sir/madam?\n"
                    + " Do try: " + commandWord + " <task number>");
        }

        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new CortisolException("'" + arguments + "' is not a number I recognise, "
                    + "sir/madam.\n Do try: " + commandWord + " <task number>");
        }
    }

    /**
     * Reads the keyword out of the argument of a search.
     *
     * @param arguments text following the command word.
     * @return the keyword to search for.
     * @throws CortisolException if nothing was given to search for.
     */
    public static String parseKeyword(String arguments) throws CortisolException {
        if (arguments.isEmpty()) {
            throw new CortisolException("You have not said what to look for, sir/madam.\n"
                    + " Do try: find <keyword>");
        }

        return arguments;
    }

    /**
     * Refuses text that contains the separator the data file is built around.
     * <p>
     * {@link Storage} writes a task as fields divided by "|", and does not
     * escape a "|" that appears inside one of them, so such a task would come
     * back truncated on the next run. Refusing it as it is typed keeps the file
     * honest, and tells the user at the moment they can still do something
     * about it.
     *
     * @param text one field of a task, as the user typed it.
     * @throws CortisolException if the text contains the separator.
     */
    private static void refuseSeparator(String text) throws CortisolException {
        if (text.contains(FIELD_SEPARATOR)) {
            throw new CortisolException("A task may not contain '" + FIELD_SEPARATOR
                    + "', sir/madam. I use it to rule the columns of my ledger.\n"
                    + " Do try the same instruction without it.");
        }
    }

    /**
     * Builds a todo from the text following the "todo" command word.
     *
     * @param arguments text following the command word.
     * @return the todo that text describes.
     * @throws CortisolException if no description was given.
     */
    public static ToDo parseTodo(String arguments) throws CortisolException {
        if (arguments.isEmpty()) {
            throw new CortisolException("A todo without a description is rather like tea "
                    + "without leaves, sir/madam.\n Do try: todo <description>");
        }
        refuseSeparator(arguments);

        return new ToDo(arguments);
    }

    /**
     * Builds a deadline from the text following the "deadline" command word.
     *
     * @param arguments text following the command word.
     * @return the deadline that text describes.
     * @throws CortisolException if the description or due date is missing, or
     *                           the due date cannot be read as a date.
     */
    public static Deadline parseDeadline(String arguments) throws CortisolException {
        // Limit of 2 keeps any later "/by" as part of the due date itself.
        String[] parts = arguments.split("/by", 2);
        String description = parts[0].trim();
        String deadline = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty()) {
            throw new CortisolException("You have not told me what is due, sir/madam.\n"
                    + " Do try: deadline <description> /by 2019-12-02");
        }
        if (deadline.isEmpty()) {
            throw new CortisolException("A deadline is of little use without a date, sir/madam.\n"
                    + " Do try: deadline <description> /by 2019-12-02");
        }
        refuseSeparator(description);

        return new Deadline(description, TaskDateTime.parse(deadline));
    }

    /**
     * Builds an event from the text following the "event" command word.
     *
     * @param arguments text following the command word.
     * @return the event that text describes.
     * @throws CortisolException if the description, start or end is missing.
     */
    public static Event parseEvent(String arguments) throws CortisolException {
        String[] fromParts = arguments.split("/from", 2);
        String description = fromParts[0].trim();
        String[] toParts = fromParts.length > 1
                ? fromParts[1].split("/to", 2)
                : new String[0];
        String startTime = toParts.length > 0 ? toParts[0].trim() : "";
        String endTime = toParts.length > 1 ? toParts[1].trim() : "";

        if (description.isEmpty()) {
            throw new CortisolException("You have not told me what the occasion is, sir/madam.\n"
                    + " Do try: event <description> /from <start> /to <end>");
        }
        if (startTime.isEmpty() || endTime.isEmpty()) {
            throw new CortisolException("An event requires both a start and an end, sir/madam.\n"
                    + " Do try: event <description> /from <start> /to <end>");
        }
        // An event's start and end are stored as fields of their own, so they
        // are as unable to carry the separator as the description is.
        refuseSeparator(description);
        refuseSeparator(startTime);
        refuseSeparator(endTime);

        return new Event(description, startTime, endTime);
    }
}

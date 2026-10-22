import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {
    private static final String BY_KEYWORD = "/by ";
    private static final String FROM_KEYWORD = "/from ";
    private static final String TO_KEYWORD = "/to ";

    public static String getCommandWord(String fullCommand) throws FunkyException {
        String trimmed = fullCommand.trim();
        if (trimmed.isEmpty()) {
            throw new FunkyException("Please enter a valid command.");
        }
        return trimmed.split("\\s+", 2)[0];
    }

    public static String getArguments(String fullCommand) {
        String[] parts = fullCommand.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    public static int parseIndex(String arguments, String commandWord) throws FunkyException {
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new FunkyException("Please give a task number, e.g. " + commandWord + " 2.");
        }
    }

    public static String parseFindKeyword(String arguments) throws FunkyException {
        if (arguments.isEmpty()) {
            throw new FunkyException("Please give a keyword to search for, e.g. find book.");
        }
        return arguments;
    }

    public static ToDo parseTodo(String arguments) throws FunkyException {
        if (arguments.isEmpty()) {
            throw new FunkyException("The description of a todo cannot be empty.");
        }
        return new ToDo(arguments);
    }

    public static Deadline parseDeadline(String arguments) throws FunkyException {
        int byIndex = arguments.indexOf(BY_KEYWORD);
        if (byIndex < 0) {
            throw new FunkyException("Please enter a valid deadline command with /by.");
        }
        String description = arguments.substring(0, byIndex).trim();
        if (description.isEmpty()) {
            throw new FunkyException("The description of a deadline cannot be empty.");
        }
        String by = arguments.substring(byIndex + BY_KEYWORD.length()).trim();
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException e) {
            throw new FunkyException("Please enter the date as yyyy-MM-dd, e.g. 2019-10-15.");
        }
    }

    public static Event parseEvent(String arguments) throws FunkyException {
        int fromIndex = arguments.indexOf(FROM_KEYWORD);
        int toIndex = arguments.indexOf(TO_KEYWORD);
        if (fromIndex < 0 || toIndex < fromIndex) {
            throw new FunkyException("Please enter a valid event command with /from and /to.");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + FROM_KEYWORD.length(), toIndex).trim();
        String to = arguments.substring(toIndex + TO_KEYWORD.length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new FunkyException("An event needs a description, a /from time and a /to time.");
        }
        return new Event(description, from, to);
    }
}
package xxlilchatxx;

/**
 * Parses raw user input into structured command information.
 */
public class Parser {

    /**
     * Represents the type of command a user input maps to.
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, UNKNOWN
    }

    /**
     * Determines which command type the given input represents.
     *
     * @param input Raw user input.
     * @return The matching command type.
     */
    public static CommandType parseCommandType(String input) {
        if (input.equals("bye")) {
            return CommandType.BYE;
        } else if (input.equals("list")) {
            return CommandType.LIST;
        } else if (input.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (input.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (input.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            return CommandType.TODO;
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        } else if (input.equals("event") || input.startsWith("event ")) {
            return CommandType.EVENT;
        } else if (input.equals("find") || input.startsWith("find ")) {
            return CommandType.FIND;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Extracts the task index from a mark/unmark/delete command.
     *
     * @param input        Raw user input, e.g. "mark 2".
     * @param prefixLength Length of the command word plus trailing space, e.g. 5 for "mark ".
     * @return Zero-based task index.
     * @throws NumberFormatException If the index portion is not a valid number.
     */
    public static int parseIndex(String input, int prefixLength) {
        return Integer.parseInt(input.substring(prefixLength)) - 1;
    }

    /**
     * Extracts the description from a todo command.
     *
     * @param input Raw user input, e.g. "todo buy bread".
     * @return The trimmed description, or an empty string if none was given.
     */
    public static String parseTodoDescription(String input) {
        return input.length() > 4 ? input.substring(5).trim() : "";
    }

    /**
     * Extracts description and by-date from a deadline command.
     *
     * @param input Raw user input, e.g. "deadline return book /by Sunday".
     * @return A two-element array: [description, by]. May contain empty strings if malformed.
     */
    public static String[] parseDeadlineParts(String input) {
        String rest = input.length() > 8 ? input.substring(9).trim() : "";
        String[] parts = rest.split(" /by ");
        if (parts.length < 2) {
            return new String[]{parts.length > 0 ? parts[0] : "", ""};
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    /**
     * Extracts description, from-time, and to-time from an event command.
     *
     * @param input Raw user input, e.g. "event meeting /from Mon 2pm /to 4pm".
     * @return A three-element array: [description, from, to]. May contain empty strings if malformed.
     */
    public static String[] parseEventParts(String input) {
        String rest = input.length() > 5 ? input.substring(6).trim() : "";
        String[] parts = rest.split(" /from ");
        if (parts.length < 2) {
            return new String[]{parts.length > 0 ? parts[0] : "", "", ""};
        }
        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length < 2) {
            return new String[]{parts[0].trim(), "", ""};
        }
        return new String[]{parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()};
    }

    /**
     * Extracts the search keyword from a find command.
     *
     * @param input Raw user input, e.g. "find book".
     * @return The keyword to search for, or an empty string if none was given.
     */
    public static String parseFindKeyword(String input) {
        return input.length() > 4 ? input.substring(5).trim() : "";
    }
}
import task.*;

/**
 * Parses user commands into executable actions.
 */
public class Parser {

    /**
     * Parses a todo command.
     *
     * @param input The full user input.
     * @return A ToDo task.
     * @throws IrriException If the description is empty.
     */
    public static Task parseTodo(String input) throws IrriException{
        String description = input.substring(5).trim();
        if (description.isEmpty()){
            throw new IrriException("The description of a todo cannot be empty.");
        }
        return new ToDo(description);
    }

    /**
     * Parses a deadline command.
     *
     * @param input The full user input.
     * @return A Deadline task.
     * @throws IrriException If the format is invalid.
     */
    public static Task parseDeadline(String input) throws IrriException{
        String rest = input.substring(9).trim();
        int byIndex = rest.indexOf(" /by ");
        if (byIndex == -1){
            throw new IrriException("A deadline must include a due date. Use: deadline <description> /by <date>");
        }
        String description = rest.substring(0,byIndex).trim();
        String by = rest.substring(byIndex + 5).trim();
        if (description.isEmpty()){
            throw new IrriException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()){
            throw new IrriException("The deadline date cannot be empty. Use: /by <date>");
        }
        return new Deadline(description, by);
    }

    /**
     * Parses an event command.
     *
     * @param input The full user input.
     * @return An Event task.
     * @throws IrriException If the format is invalid.
     */
    public static Task parseEvent(String input) throws IrriException{
        String rest = input.substring(6).trim();
        int fromIndex = rest.indexOf(" /from ");
        int toIndex = rest.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex){
            throw new IrriException("An event must include start and end times. Use: event <description> /from <start> /to <end>");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + 6, toIndex).trim();
        String to = rest.substring(toIndex +5).trim();
        if (description.isEmpty()){
            throw new IrriException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()){
            throw new IrriException("The start time cannot be empty. Use: /from <start>");
        }
        if (to.isEmpty()){
            throw new IrriException("The end time cannot be empty. Use: /to <end>");
        }
        return new Event(description, from, to);
    }

    /**
     * Parses a task number from a mark, unmark, or delete command.
     *
     * @param input The full user input.
     * @param command The command name ("mark", "unmark", or "delete").
     * @return The 0-based task index.
     * @throws IrriException If the number is missing or invalid.
     */
    public static int parseTaskNumber(String input, String command) throws IrriException{
        String rest = input.substring(command.length()).trim();
        if (rest.isEmpty()){
            throw new IrriException("Please specify a task number. Use: " + command + " <number>");
        }
        try {
            int index = Integer.parseInt(rest) - 1;
            if (index < 0) {
                throw new IrriException("Task number must be positive.");
            }
            return index;
        } catch (NumberFormatException e){
            throw new IrriException("Please enter a valid task number (e.g., \" + command + \" 2)");
        }
    }

    /**
     * Parses a find command to extract the search keyword.
     *
     * @param input The full user input.
     * @return The search keyword.
     * @throws IrriException If the keyword is empty.
     */
    public static String parseFind(String input) throws IrriException {
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new IrriException("Please provide a keyword to search for. " + "Use: find <keyword>");
        }
        return keyword;
    }
}

package dook;

/**
 * Parses user input into commands and extracts the relevant command arguments.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";

    /**
     * Determines the command type from the user's input.
     *
     * @param input the user's input
     * @return the command type corresponding to the input
     */
    public CommandType parseCommand(String input) {
        String command = input.split(" ")[0];
        switch (command) {
            case BYE_COMMAND:
                return CommandType.BYE;
            case LIST_COMMAND:
                return CommandType.LIST;
            case MARK_COMMAND:
                return CommandType.MARK;
            case UNMARK_COMMAND:
                return CommandType.UNMARK;
            case TODO_COMMAND:
                return CommandType.TODO;
            case DEADLINE_COMMAND:
                return CommandType.DEADLINE;
            case EVENT_COMMAND:
                return CommandType.EVENT;
            case DELETE_COMMAND:
                return CommandType.DELETE;
            case FIND_COMMAND:
                return CommandType.FIND;
            default:
                return CommandType.UNKNOWN;
        }
    }

    /**
     * Parses the task number from a mark command.
     *
     * @param input the user's mark command
     * @return the task number
     * @throws DookException if the task number is not an integer
     */
    public int parseMarkCommand(String input) throws DookException {
        try {
            // Remove the command prefix before converting the remaining input to an integer.
            return Integer.parseInt(input.substring(MARK_COMMAND.length()).trim());
        } catch (NumberFormatException e) {
            throw new DookException("The task number must be an integer.");
        }
    }

    /**
     * Parses the task number from an unmark command.
     *
     * @param input the user's unmark command
     * @return the task number
     * @throws DookException if the task number is not an integer
     */
    public int parseUnmarkCommand(String input) throws DookException {
        try {
            // Remove the command prefix before converting the remaining input to an integer.
            return Integer.parseInt(input.substring(UNMARK_COMMAND.length()).trim());
        } catch (NumberFormatException e) {
            throw new DookException("The task number must be an integer.");
        }
    }

    /**
     * Parses a todo command and creates the corresponding todo task.
     *
     * @param input the user's todo command
     * @return the todo task created from the input
     * @throws DookException if the todo description is empty
     */
    public Todo parseTodoCommand(String input) throws DookException {
        String description = input.substring(TODO_COMMAND.length()).trim();
        if (description.isEmpty()) {
            throw new DookException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command and creates the corresponding deadline task.
     *
     * @param input the user's deadline command
     * @return the deadline task created from the input
     * @throws DookException if the deadline format or its fields are invalid
     */
    public Deadline parseDeadlineCommand(String input) throws DookException {
        String content = input.substring(DEADLINE_COMMAND.length()).trim();
        if (content.isEmpty()) {
            throw new DookException("The content of a deadline cannot be empty.");
        }
        // Split the input into the description and the deadline date.
        String[] deadlineParts = content.split(" /by ");
        if (deadlineParts.length != 2) {
            throw new DookException("A deadline must include exactly one description and one /by date.");
        }
        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();
        if (description.isEmpty()) {
            throw new DookException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new DookException("The /by date of a deadline cannot be empty.");
        }
        return new Deadline(description, by);
    }

    /**
     * Parses an event command and creates the corresponding event task.
     *
     * @param input the user's event command
     * @return the event task created from the input
     * @throws DookException if the event format or its fields are invalid
     */
    public Event parseEventCommand(String input) throws DookException {
        String content = input.substring(EVENT_COMMAND.length()).trim();
        if (content.isEmpty()) {
            throw new DookException("The content of an event cannot be empty.");
        }
        // Split the input into the description, start date, and end date.
        String[] eventParts = content.split(" /from | /to ");
        if (eventParts.length != 3) {
            throw new DookException("An event must include exactly one description, one /from date, and one /to date.");
        }
        String description = eventParts[0].trim();
        String from = eventParts[1].trim();
        String to = eventParts[2].trim();
        if (description.isEmpty()) {
            throw new DookException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new DookException("The /from date of an event cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new DookException("The /to date of an event cannot be empty.");
        }
        return new Event(description, from, to);
    }

    /**
     * Parses the task number from a delete command.
     *
     * @param input the user's delete command
     * @return the task number
     * @throws DookException if the task number is not an integer
     */
    public int parseDeleteCommand(String input) throws DookException {
        try {
            // Remove the command prefix before converting the remaining input to an integer.
            return Integer.parseInt(input.substring(DELETE_COMMAND.length()).trim());
        } catch (NumberFormatException e) {
            throw new DookException("The task number must be an integer.");
        }
    }

    /**
     * Parses the keyword from a find command.
     *
     * @param input the user's find command
     * @return the keyword to search for
     * @throws DookException if the keyword is empty
     */
    public String parseFindCommand(String input) throws DookException {
        String keyword = input.substring(FIND_COMMAND.length()).trim();
        if (keyword.isEmpty()) {
            throw new DookException("The keyword to find cannot be empty.");
        }
        return keyword;
    }
}

package mybff;

/** Interprets command words, task numbers, and task descriptions. */
public class Parser {
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    /**
     * Creates an executable command without changing tasks or interacting with the user.
     *
     * @param command the user input with surrounding whitespace removed
     * @return the command representing the requested operation
     * @throws MyBffException if the command is unknown or a task description is invalid
     * @throws NumberFormatException if a task number is missing, malformed, or outside the int range
     */
    public Command parse(String command) throws MyBffException {
        if (command.equals(COMMAND_BYE)) {
            return new ExitCommand();
        }
        if (command.equals(COMMAND_LIST)) {
            return new ListCommand();
        }
        String commandWord = getCommandWord(command);
        if (commandWord.equals(COMMAND_FIND)) {
            String keyword = command.substring(COMMAND_FIND.length()).trim();
            if (keyword.isEmpty()) {
                throw new MyBffException("The keyword for find cannot be empty.");
            }
            return new FindCommand(keyword);
        }
        if (commandWord.equals(COMMAND_MARK)) {
            return new CompletionCommand(parseTaskIndex(command), true);
        }
        if (commandWord.equals(COMMAND_UNMARK)) {
            return new CompletionCommand(parseTaskIndex(command), false);
        }
        if (commandWord.equals(COMMAND_DELETE)) {
            return new DeleteCommand(parseTaskIndex(command));
        }
        return new AddCommand(createTask(command));
    }

    /** Returns the first word of a command. */
    private String getCommandWord(String command) {
        return command.split("\\s+", 2)[0];
    }

    /**
     * Converts a command's one-based task number to a list index.
     * The caller checks whether the index identifies a task in the current list.
     *
     * @param command a trimmed mark, unmark, or delete command
     * @return the task number minus one
     * @throws NumberFormatException if the number is missing, malformed, or outside the int range
     */
    private int parseTaskIndex(String command) {
        String taskNumber = command.substring(getCommandWord(command).length()).trim();
        return Integer.parseInt(taskNumber) - 1;
    }

    /**
     * Creates a task from an add command.
     *
     * @throws MyBffException if the command is unknown or the todo description is empty
     */
    private Task createTask(String command) throws MyBffException {
        if (command.startsWith(COMMAND_DEADLINE + " ")) {
            String[] deadlineParts = command.substring(COMMAND_DEADLINE.length())
                    .trim().split(" /by ", 2);
            return new Deadline(deadlineParts[0], deadlineParts.length == 2 ? deadlineParts[1] : "");
        }
        if (command.startsWith(COMMAND_EVENT + " ")) {
            String[] eventParts = command.substring(COMMAND_EVENT.length())
                    .trim().split(" /from ", 2);
            String[] timeParts = (eventParts.length == 2 ? eventParts[1] : "").split(" /to ", 2);
            return new Event(eventParts[0], timeParts[0], timeParts.length == 2 ? timeParts[1] : "");
        }
        String[] commandParts = command.split("\\s+", 2);
        if (commandParts[0].equals(COMMAND_TODO)) {
            String description = commandParts.length == 2 ? commandParts[1].trim() : "";
            if (description.isEmpty()) {
                throw new MyBffException("The description of a todo cannot be empty.");
            }
            return new ToDo(description);
        }
        throw new MyBffException("I'm sorry, but I don't know what that means :-(");
    }

}

package seedu.potato;

import java.io.IOException;

/**
 * Parses user input and executes the corresponding application action.
 */
public class CommandParser {
    private static final String UNMARK_COMMAND = "unmark";
    private static final String MARK_COMMAND = "mark";
    private static final String DELETE_COMMAND = "delete";
    private static final String LIST_COMMAND = "list";
    private static final String FIND_COMMAND = "find";
    private static final String EXIT_COMMAND = "bye";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private final Command command;
    private final TaskList taskList;

    /**
     * Creates a parser connected to the user interface and task storage.
     *
     * @param command Command input/output handler.
     * @param taskList Storage for the user's tasks.
     */
    public CommandParser(Command command, TaskList taskList) {
        this.command = command;
        this.taskList = taskList;
    }

    /**
     * Parses and executes one line of user input.
     *
     * @param userInput Text entered by the user.
     * @return {@code true} if the application should exit; otherwise {@code false}.
     * @throws IOException If the task save file cannot be written.
     */
    public boolean execute(String userInput) throws IOException {
        String trimmedInput = userInput.trim();
        String[] commandParts = trimmedInput.split("\\s+", 2);

        if (trimmedInput.equalsIgnoreCase(EXIT_COMMAND)) {
            command.showFarewell();
            return true;
        }

        if (trimmedInput.equalsIgnoreCase(LIST_COMMAND)) {
            command.showTaskList(taskList.getTasks());
            return false;
        }

        if (commandParts[0].equalsIgnoreCase(MARK_COMMAND)) {
            markTask(commandParts);
            return false;
        }

        if (commandParts[0].equalsIgnoreCase(UNMARK_COMMAND)) {
            unmarkTask(commandParts);
            return false;
        }

        if (commandParts[0].equalsIgnoreCase(DELETE_COMMAND)) {
            deleteTask(commandParts);
            return false;
        }

        if (commandParts[0].equalsIgnoreCase(FIND_COMMAND)) {
            findTasks(commandParts);
            return false;
        }

        try {
            Task newTask = createTask(commandParts);
            taskList.add(newTask);
            command.showTaskAdded(newTask, taskList.getTaskCount());
        } catch (IllegalArgumentException exception) {
            System.out.println(Command.SEPARATOR);
            System.out.println(exception.getMessage());
            System.out.println(Command.SEPARATOR);
            return false;
        }
        return false;
    }

    /**
     * Attempts to create a task from the supplied command.
     *
     * @param commandParts Command word and its optional task arguments.
     * @return Task represented by the command.
     * @throws IllegalArgumentException If the command or task arguments are invalid.
     */
    private Task createTask(String[] commandParts) {
        String commandWord = commandParts[0];
        String taskArguments = commandParts.length < 2 ? null : commandParts[1];

        if (commandWord.equalsIgnoreCase(TODO_COMMAND)) {
            return ToDo.createFromInput(taskArguments);
        }

        if (commandWord.equalsIgnoreCase(DEADLINE_COMMAND)) {
            return Deadline.createFromInput(taskArguments);
        }

        if (commandWord.equalsIgnoreCase(EVENT_COMMAND)) {
            return Event.createFromInput(taskArguments);
        }

        throw new IllegalArgumentException(
            "Command does not create a task." + System.lineSeparator() + 
            "Valid tasks are: " + TODO_COMMAND + ", " + DEADLINE_COMMAND + ", " + EVENT_COMMAND);
    }

    /**
     * Marks the task selected by a {@code mark TASK_NUMBER} command.
     *
     * @param commandParts Command word and its optional argument.
     * @throws IOException If the task save file cannot be written.
     */
    private void markTask(String[] commandParts) throws IOException {
        if (commandParts.length < 2) {
            command.showInvalidTaskNumber();
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            Task markedTask = taskList.markAsDone(taskNumber);
            command.showTaskMarkedAsDone(markedTask);
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            command.showInvalidTaskNumber();
        }
    }

    /**
     * Reverses completion for the task selected by an {@code unmark TASK_NUMBER} command.
     *
     * @param commandParts Command word and its optional argument.
     * @throws IOException If the task save file cannot be written.
     */
    private void unmarkTask(String[] commandParts) throws IOException {
        if (commandParts.length < 2) {
            command.showInvalidTaskNumber();
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            Task unmarkedTask = taskList.markAsNotDone(taskNumber);
            command.showTaskMarkedAsNotDone(unmarkedTask);
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            command.showInvalidTaskNumber();
        }
    }

    /**
     * Deletes the task selected by a {@code delete TASK_NUMBER} command.
     *
     * @param commandParts Command word and its optional argument.
     * @throws IOException If the task save file cannot be written.
     */
    private void deleteTask(String[] commandParts) throws IOException {
        if (commandParts.length < 2) {
            command.showInvalidTaskNumber();
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            Task deletedTask = taskList.delete(taskNumber);
            command.showTaskDeleted(deletedTask, taskList.getTaskCount());
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            command.showInvalidTaskNumber();
        }
    }

    /**
     * Finds and displays tasks selected by a {@code find SEARCH_TEXT} command.
     *
     * @param commandParts Command word and its search text.
     */
    private void findTasks(String[] commandParts) {
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            command.showInvalidCommand();
            return;
        }

        command.showFoundTasks(taskList.find(commandParts[1]));
    }
}

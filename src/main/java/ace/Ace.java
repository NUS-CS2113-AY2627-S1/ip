package ace;

import ace.exception.AceException;
import ace.storage.Storage;
import ace.parser.Parser;
import ace.parser.Parser.ParsedCommand;
import ace.task.Task;
import ace.task.TaskManager;
import ace.ui.Messages;
import ace.ui.Ui;

import java.util.List;

/**
 * Coordinates task management, persistence and user commands in Ace.
 */
public class Ace {
    private static TaskManager taskManager = new TaskManager();
    private static final Storage storage = new Storage();
    private static final Ui ui = new Ui();

    /**
     * Displays the current task list, or a message when it is empty.
     *
     * @throws AceException if a task cannot be retrieved
     */
    public static void printTaskList() throws AceException {
        int taskCount = taskManager.getTasksCount();
        if (taskCount == 0) {
            ui.printAceMessage("I don't know what happened or if I messed something up, "
                    + "but... you have no tasks available.");
            return;
        }

        StringBuilder message = new StringBuilder("These should be your tasks:");
        for (int i = 0; i < taskCount; i++) {
            Task task = taskManager.getTask(i);
            message.append("\n\t").append(i + 1).append(".").append(task);
        }
        ui.printAceMessage(message);
    }

    /**
     * Displays tasks whose descriptions contain the requested keyword.
     * Task numbers correspond to their positions in the full task list.
     *
     * @param keyword text to search for in task descriptions
     * @throws AceException if a matching task cannot be retrieved
     */
    private static void printMatchingTasks(String keyword) throws AceException {
        List<Integer> indices = taskManager.findTaskIndices(keyword);
        if (indices.isEmpty()) {
            ui.printAceMessage("I couldn't find any tasks containing: " + keyword);
            return;
        }

        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:");
        for (int index : indices) {
            message.append("\n\t").append(index + 1).append(".")
                    .append(taskManager.getTask(index));
        }
        ui.printAceMessage(message);
    }

    /**
     * Displays confirmation after a task has been marked as completed.
     *
     * @param taskNumber zero-based index of the completed task
     * @throws AceException if the task number is invalid
     */
    public static void printTaskMarkedDone(int taskNumber) throws AceException {
        Task task = taskManager.getTask(taskNumber);
        ui.printAceMessage("Oh wow, you managed to finish this task:\n" + "\t" + task);
    }

    /**
     * Displays confirmation after a task has been marked as incomplete.
     *
     * @param taskNumber zero-based index of the incomplete task
     * @throws AceException if the task number is invalid
     */
    public static void printTaskMarkedUndone(int taskNumber) throws AceException {
        Task task = taskManager.getTask(taskNumber);
        ui.printAceMessage("I'm sorry, looks like this task isn't done after all:\n" + "\t" + task);
    }

    /**
     * Executes one parsed user command.
     *
     * @param line full command entered by the user
     * @return false if the user wants to exit, otherwise true
     * @throws AceException if the command cannot be completed
     */
    private static boolean processCommand(String line) throws AceException {
        ParsedCommand command = Parser.parse(line);

        switch (command.getKeyword()) {
        case "help":
            ui.printAceMessage(Messages.HELP_MESSAGE);
            break;
        case "bye":
            ui.printAceMessage(Messages.BYE_MESSAGE);
            return false;
        case "list":
            printTaskList();
            break;
        case "find":
            printMatchingTasks(command.getSearchTerm());
            break;
        case "mark":
            taskManager.markAsDone(command.getTaskNumber());
            saveTasks();
            printTaskMarkedDone(command.getTaskNumber());
            break;
        case "unmark":
            taskManager.markAsUndone(command.getTaskNumber());
            saveTasks();
            printTaskMarkedUndone(command.getTaskNumber());
            break;
        case "delete":
            Task deletedTask = taskManager.deleteTask(command.getTaskNumber());
            saveTasks();
            ui.printAceMessage("Should be good? I've removed this task:\n\t" + deletedTask
                    + "\n\tNow you have... " + taskManager.getTasksCount() + " tasks in the list.");
            break;
        case "todo":
        case "deadline":
        case "event":
            addTask(command.getTask());
            break;
        default:
            throw new AceException(Messages.UNKNOWN_COMMAND_EXCEPTION);
        }
        return true;
    }

    private static void addTask(Task task) throws AceException {
        taskManager.addTask(task);
        saveTasks();
        ui.printAceMessage("I think I managed to add this new task:\n\t" + task);
    }

    /**
     * Loads saved tasks and handles commands until the user exits.
     * Stops if loading fails to avoid overwriting existing task data.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) throws AceException {
        try {
            taskManager = new TaskManager(storage.loadTasks());
        } catch (AceException exception) {
            ui.showError(exception.getMessage());
            return;
        }

        ui.printAceMessage(Messages.BANNER);
        ui.printAceMessage(Messages.WELCOME_MESSAGE);
        ui.printAceMessage(Messages.ASSISTANCE_MESSAGE);

        boolean isRunning = true;

        while (isRunning && ui.hasNextCommand()) {
            String line = ui.readCommand();
            try {
                isRunning = processCommand(line);
            } catch (AceException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void saveTasks() throws AceException {
        storage.saveTasks(taskManager);
    }
}

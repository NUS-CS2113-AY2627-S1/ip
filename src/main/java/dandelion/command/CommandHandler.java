package dandelion.command;

import dandelion.parser.Parser;
import dandelion.storage.Storage;
import dandelion.task.Task;
import dandelion.task.TaskList;
import dandelion.ui.Ui;

import java.io.IOException;

/**
 * Handles user commands by coordinating parsing, task management, storage, and UI.
 */
public class CommandHandler {
    /** Tasks modified by commands during the current session. */
    private final TaskList tasks;

    /** Storage used to save task changes. */
    private final Storage storage;

    /** Parser used to interpret user commands. */
    private final Parser parser;

    /** UI used to display command results and errors. */
    private final Ui ui;

    /**
     * Creates a command handler with the required application collaborators.
     *
     * @param tasks Task list to modify.
     * @param storage Storage used to save task changes.
     * @param parser Parser used to interpret commands.
     * @param ui UI used to display command results.
     */
    public CommandHandler(TaskList tasks, Storage storage, Parser parser, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.parser = parser;
        this.ui = ui;
    }

    /**
     * Handles one user command by dispatching it to the appropriate method.
     *
     * @param command User input.
     * @return {@code false} when the application should stop; {@code true} otherwise.
     */
    public boolean handleCommand(String command) {
        String commandWord = parser.getCommandWord(command);

        switch (commandWord) {
        case "bye":
            ui.showGoodbyeMessage();
            return false;
        case "list":
            ui.showTaskList(tasks);
            break;
        case "mark":
            handleMark(command);
            break;
        case "unmark":
            handleUnmark(command);
            break;
        case "delete":
            handleTaskDeletion(command);
            ui.showTaskCount(tasks.size());
            break;
        case "todo":
        case "deadline":
        case "event":
            handleTaskCreation(command, commandWord);
            ui.showTaskCount(tasks.size());
            break;
        case "find":
            handleFind(command);
            break;
        default:
            ui.showUnknownCommand();
            break;
        }
        return true;
    }

    /**
     * Marks the requested task as done.
     *
     * @param command User command containing a task number.
     */
    private void handleMark(String command) {
        updateTaskStatus(command, true);
    }

    /**
     * Marks the requested task as not done.
     *
     * @param command User command containing a task number.
     */
    private void handleUnmark(String command) {
        updateTaskStatus(command, false);
    }

    /**
     * Updates a task's completion status.
     *
     * @param command User command containing a task number.
     * @param isDone Whether the task should be marked as done.
     */
    private void updateTaskStatus(String command, boolean isDone) {
        if (tasks.isEmpty()) {
            ui.showNoTasksMessage();
            return;
        }

        try {
            int taskNumber = Integer.parseInt(parser.getArgument(command));
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showInvalidTaskNumber(tasks.size());
                return;
            }

            Task task = isDone
                    ? tasks.markTask(taskNumber - 1)
                    : tasks.unmarkTask(taskNumber - 1);
            saveTasks();
            ui.showTaskStatusUpdated(task, isDone);
        } catch (NumberFormatException exception) {
            ui.showMissingTaskNumber();
        }
    }

    /**
     * Deletes the task specified in a delete command.
     *
     * @param command User command containing a task number.
     */
    private void handleTaskDeletion(String command) {
        if (tasks.isEmpty()) {
            ui.showNoTasksMessage();
            return;
        }

        try {
            int taskNumber = Integer.parseInt(parser.getArgument(command));
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showInvalidTaskNumber(tasks.size());
                return;
            }
            Task deletedTask = tasks.deleteTask(taskNumber - 1);
            saveTasks();
            ui.showTaskDeleted(deletedTask);
        } catch (NumberFormatException exception) {
            ui.showMissingTaskNumber();
        }
    }

    /**
     * Creates and stores a task based on its command type.
     *
     * @param command     Complete user command.
     * @param commandWord Command type.
     */
    private void handleTaskCreation(String command, String commandWord) {
        try {
            Task task = switch (commandWord) {
            case "todo" -> parser.parseTodo(command);
            case "deadline" -> parser.parseDeadline(command);
            case "event" -> parser.parseEvent(command);
            default -> throw new IllegalArgumentException("Unknown task type.");
            };
            addTask(task);
        } catch (IllegalArgumentException exception) {
            ui.showError(exception.getMessage());
        }
    }

    /**
     * Finds tasks whose descriptions contain the requested keyword.
     *
     * @param command User command containing a search keyword.
     */
    private void handleFind(String command) {
        String keyword = parser.getArgument(command);
        if (keyword.isEmpty()) {
            ui.showMissingSearchKeyword();
            return;
        }
        ui.showMatchingTasks(tasks.findTasks(keyword));
    }

    /**
     * Adds a task to the task list and confirms the addition.
     *
     * @param task Task to add.
     */
    private void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded(task);
    }

    /**
     * Saves the current task list to the data file.
     */
    private void saveTasks() {
        try {
            storage.saveTasks(tasks);
        } catch (IOException exception) {
            ui.showSavingError(exception.getMessage());
        }
    }
}

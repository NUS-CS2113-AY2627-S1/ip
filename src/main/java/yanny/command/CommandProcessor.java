package yanny.command;

import java.io.IOException;
import java.util.Locale;

import yanny.exception.YannyException;
import yanny.storage.Storage;
import yanny.task.Task;
import yanny.task.TaskList;
import yanny.ui.Ui;

/**
 * Processes user commands and manages the tasks stored by Yanny.
 */
public class CommandProcessor {
    private final Parser parser = new Parser();
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates a command processor for the supplied task list and storage.
     *
     * @param ui the terminal interface used to display command results.
     * @param storage the component that saves task changes.
     * @param tasks the task list to process commands against.
     */
    public CommandProcessor(Ui ui, Storage storage, TaskList tasks) {
        this.ui = ui;
        this.storage = storage;
        this.tasks = tasks;
    }

    /**
     * Processes one command and updates the stored tasks when necessary.
     *
     * @param command the command entered by the user.
     * @throws YannyException if the command contains invalid user input.
     */
    public void processCommand(String command) throws YannyException {
        switch (parser.parseCommandType(command)) {
        case LIST -> handleListCommand();
        case FIND -> handleFindCommand(command);
        case MARK -> handleMarkCommand(command.trim());
        case UNMARK -> handleUnmarkCommand(command.trim());
        case DELETE -> handleDeleteCommand(command, "delete");
        case REMOVE -> handleDeleteCommand(command, "remove");
        case ADD -> handleAddCommand(command);
        }
    }

    /** Displays the current tasks and their completion status. */
    private void handleListCommand() {
        ui.showTaskList(tasks.getTasks());
    }

    /** Displays tasks whose descriptions contain the supplied keyword. */
    private void handleFindCommand(String command) throws YannyException {
        String keyword = parser.parseFindKeyword(command.trim());
        ui.showSearchResults(tasks.getTasks(), tasks.findMatchingTaskIndices(keyword));
    }

    /** Handles a command to mark a task as done. */
    private void handleMarkCommand(String command) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, "mark");
        Task task = tasks.getTask(taskIndex, "MARK");
        boolean wasDone = task.isDone();
        task.markAsDone();
        try {
            saveTasks();
        } catch (YannyException exception) {
            restoreTaskStatus(task, wasDone);
            throw exception;
        }
        ui.showMarkedTask(task);
    }

    /** Handles a command to mark a task as not done. */
    private void handleUnmarkCommand(String command) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, "unmark");
        Task task = tasks.getTask(taskIndex, "UNMARK");
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            saveTasks();
        } catch (YannyException exception) {
            restoreTaskStatus(task, wasDone);
            throw exception;
        }
        ui.showUnmarkedTask(task);
    }

    /**
     * Deletes a task from the collection and displays the removed task.
     *
     * @param command the delete or remove command.
     * @param commandName the command keyword used in validation messages.
     * @throws YannyException if the task number is missing or invalid.
     */
    private void handleDeleteCommand(String command, String commandName) throws YannyException {
        int taskIndex = parser.parseTaskIndex(command, commandName);
        String upperCommandName = commandName.toUpperCase(Locale.ROOT);
        Task deletedTask = tasks.deleteTask(taskIndex, upperCommandName);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.restoreTask(taskIndex, deletedTask);
            throw exception;
        }
        ui.showDeletedTask(deletedTask);
    }

    /**
     * Adds a task from a command and displays the result.
     *
     * @param command the command entered by the user.
     * @throws YannyException if the command contains invalid user input.
     */
    private void handleAddCommand(String command) throws YannyException {
        ui.showCommandReceived(command);
        Task task = parser.parseTaskCommand(command);
        tasks.addTask(task);
        try {
            saveTasks();
        } catch (YannyException exception) {
            tasks.removeLastTask();
            throw exception;
        }
        ui.showAddedTask(task, tasks.size());
    }

    /** Saves the current task list and reports file-system failures as user errors. */
    private void saveTasks() throws YannyException {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException | IllegalArgumentException | SecurityException exception) {
            throw new YannyException("TASK DATA COULD NOT BE SAVED. CHECK FILE PERMISSIONS.");
        }
    }

    /** Restores a task's completion state after a failed persistence operation. */
    private void restoreTaskStatus(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }
}

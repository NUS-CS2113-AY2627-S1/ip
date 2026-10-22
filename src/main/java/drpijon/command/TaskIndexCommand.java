package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Provides shared task-number validation for commands that target one task.
 */
public abstract class TaskIndexCommand extends Command {
    /** User-provided one-based task number. */
    private final String arguments;

    /**
     * Creates a task-index command.
     *
     * @param arguments task number entered by the user
     */
    protected TaskIndexCommand(String arguments) {
        this.arguments = arguments;
    }

    /**
     * Returns the zero-based index of the selected task.
     *
     * @param tasks task list to validate against
     * @return zero-based index of the selected task
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    protected int getTaskIndex(TaskList tasks) throws DrPijonException {
        if (arguments.isEmpty()) {
            throw new DrPijonException("BOOOOOOOO! Please specify a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new DrPijonException("BOOOOOOOO! Please specify a valid task number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DrPijonException("BOOOOOOOO! That task number does not exist!");
        }
        return taskNumber - 1;
    }

    /**
     * Returns the selected task.
     *
     * @param tasks task list to read
     * @return selected task
     * @throws DrPijonException when the task number is missing, invalid, or out of range
     */
    protected Task getTask(TaskList tasks) throws DrPijonException {
        return tasks.get(getTaskIndex(tasks));
    }

    /**
     * Updates and saves the selected task's done status, then displays the result.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the update
     * @param newDoneStatus done status to apply
     * @throws DrPijonException when the task number is invalid or the update cannot be saved
     */
    protected void updateTaskStatus(TaskList tasks, Ui ui, Storage storage, boolean newDoneStatus)
            throws DrPijonException {
        Task selectedTask = getTask(tasks);
        selectedTask.setDone(newDoneStatus);
        storage.save(tasks);
        if (newDoneStatus) {
            ui.showTaskMarked(selectedTask);
        } else {
            ui.showTaskUnmarked(selectedTask);
        }
    }
}

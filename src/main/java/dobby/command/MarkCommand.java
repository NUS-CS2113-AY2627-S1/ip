package dobby.command;

import dobby.exception.DobbyException;
import dobby.storage.Storage;
import dobby.task.Task;
import dobby.task.TaskList;
import dobby.ui.Ui;

/**
 * Represents the command that marks a selected task as done.
 */
public final class MarkCommand extends Command {

    /** The one-based task number supplied by the user. */
    private final int taskNumber;

    /**
     * Creates a command that marks the supplied task number as done.
     *
     * @param taskNumber the one-based task number to mark.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the selected task, displays a confirmation, and saves the updated list.
     *
     * @param tasks the task list to update.
     * @param ui the user interface used to display the confirmation.
     * @param storage the storage handler used to persist the updated list.
     * @throws DobbyException when the selected task number is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DobbyException {
        Task task = getSelectedTask(tasks);
        task.markAsDone();
        ui.showTaskMarked(task);
        saveTasks(tasks, ui, storage);
    }

    /**
     * Returns and validates the task selected by the user.
     *
     * @param tasks the task list from which to select a task.
     * @return the selected task.
     * @throws DobbyException when the selected task number is invalid.
     */
    private Task getSelectedTask(TaskList tasks) throws DobbyException {
        if (tasks.size() == 0) {
            throw new DobbyException("There are no tasks to mark. Add one with todo <description> first.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DobbyException("Task " + taskNumber + " does not exist. Choose a number from 1 to "
                    + tasks.size() + ".");
        }

        return tasks.get(taskNumber - 1);
    }
}

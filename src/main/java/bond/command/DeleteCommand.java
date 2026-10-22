package bond.command;

import bond.exception.StorageException;
import bond.storage.Storage;
import bond.task.Task;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Deletes a selected task and saves the updated list before reporting success.
 */
public class DeleteCommand extends Command {

    private final int taskIndex;

    /**
     * Creates a deletion command for a validated task index.
     *
     * @param taskIndex Zero-based index that must exist in the list when executed.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Deletes and saves the task list, restoring the task at its original index if saving fails.
     *
     * @param taskList Tasks containing the selected index in the current session.
     * @param ui User interface used to confirm a successful deletion.
     * @param storage File storage used to save the updated list.
     * @throws StorageException If the updated list cannot be saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws StorageException {
        Task deletedTask = taskList.deleteTask(taskIndex);
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            taskList.restoreTask(taskIndex, deletedTask);
            throw e;
        }
        ui.showTaskDeleted(deletedTask, taskList.getSize());
    }
}

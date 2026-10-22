package bond.command;

import bond.exception.StorageException;
import bond.storage.Storage;
import bond.task.Task;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Marks a selected task as not done and saves the updated list before reporting success.
 */
public class UnmarkCommand extends Command {

    private final int taskIndex;

    /**
     * Creates an unmark command for a validated task index.
     *
     * @param taskIndex Zero-based index that must exist in the list when executed.
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Unmarks and saves the task, restoring its previous status if saving fails.
     *
     * @param taskList Tasks containing the selected index in the current session.
     * @param ui User interface used to confirm a successful status change.
     * @param storage File storage used to save the updated list.
     * @throws StorageException If the updated list cannot be saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws StorageException {
        Task task = taskList.getTask(taskIndex);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            if (wasDone) {
                task.markAsDone();
            }
            throw e;
        }
        ui.showTaskUnmarked(task);
    }
}

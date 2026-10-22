package bob.command;

import bob.exception.IndexOutOfBoundsError;
import bob.storage.Storage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Removes a task, saves the updated list, and displays the deletion result.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command to delete the task at the given zero-based index.
     * The index is checked against the current list when the command executes.
     *
     * @param index position of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Removes the selected task, then saves and displays the updated task count.
     * If saving fails, reports the failure and still displays the in-memory result.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IndexOutOfBoundsError {
        Task deletedTask = tasks.remove(index);
        saveTasks(tasks, ui, storage);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}

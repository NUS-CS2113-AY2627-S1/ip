package isa.command;

import isa.exception.IsaException;
import isa.exception.StorageException;
import isa.storage.Storage;
import isa.task.Task;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Removes a selected task and saves the remaining list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a deletion command for a zero-based task index.
     *
     * @param taskIndex Index of the task to delete.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Deletes the selected task, saves the list, and displays the result.
     *
     * @param tasks Current task list.
     * @param ui Console interface for the result.
     * @param storage Storage used to save the list.
     * @throws IsaException If the selected task does not exist.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IsaException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IsaException("that task number does not exist!");
        }

        Task removedTask = tasks.remove(taskIndex);

        try {
            storage.save(tasks);
        } catch (StorageException e) {
            ui.showSaveWarning(e.getMessage());
        }

        ui.showDeleted(removedTask, tasks.size());
    }
}

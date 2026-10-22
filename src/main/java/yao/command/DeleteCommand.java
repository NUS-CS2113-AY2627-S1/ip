package yao.command;

import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.YaoException;
import yao.task.Task;

/**
 * Command to remove a task from the task list and persist the change.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Constructs a DeleteCommand for the task at the given 0-based index.
     *
     * @param index The 0-based index of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws YaoException {
        if (index < 0 || index >= tasks.size()) {
            throw new YaoException("OOPS!!! Task number is out of range.");
        }
        Task removedTask = tasks.delete(index);
        storage.save(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}

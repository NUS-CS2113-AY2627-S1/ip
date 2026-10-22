package yao.command;

import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.YaoException;
import yao.task.Task;

/**
 * Command to mark a specified task as completed.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Constructs a MarkCommand for the task at the given 0-based index.
     *
     * @param index The 0-based index of the task to mark done.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws YaoException {
        if (index < 0 || index >= tasks.size()) {
            throw new YaoException("OOPS!!! Task number is out of range.");
        }
        Task task = tasks.get(index);
        task.markAsDone();
        storage.save(tasks);
        ui.showTaskMarked(task);
    }
}

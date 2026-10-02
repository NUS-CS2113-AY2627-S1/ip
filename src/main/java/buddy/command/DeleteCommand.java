package buddy.command;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Removes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command that deletes the task at the given 1-based index.
     *
     * @param index the 1-based position of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException {
        requireValidIndex(index, tasks.size());
        Task removedTask = tasks.remove(index - 1);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}

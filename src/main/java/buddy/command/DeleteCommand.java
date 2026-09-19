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
    private final int taskNumber;

    /**
     * Creates a command that deletes the task with the given one-based number.
     *
     * @param taskNumber one-based number of the task to delete
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException {
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.getAll());
        ui.showTaskRemoved(removedTask, tasks.size());
    }
}

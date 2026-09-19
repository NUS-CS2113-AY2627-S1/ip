package buddy.command;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that marks the task with the given one-based number as not done.
     *
     * @param taskNumber one-based number of the task to unmark
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException {
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        storage.save(tasks.getAll());
        ui.showTaskUnmarked(task);
    }
}

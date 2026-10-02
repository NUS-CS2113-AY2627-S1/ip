package buddy.command;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Marks a task as done or not done.
 */
public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    /**
     * Creates a command that marks the task at the given 1-based index.
     *
     * @param index the 1-based position of the task to mark
     * @param isDone true to mark the task done, false to mark it not done
     */
    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException {
        requireValidIndex(index, tasks.size());
        Task task = tasks.get(index - 1);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.asList());
        ui.showTaskStatusChanged(task, isDone);
    }
}

package wenwen.command;

import wenwen.exception.WenwenException;
import wenwen.storage.Storage;
import wenwen.task.Task;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Marks a numbered task as done or not done.
 */
public class MarkCommand extends Command {
    private final int taskNumber;
    private final boolean isDone;

    /**
     * Creates a command that changes a task's completion state.
     *
     * @param taskNumber one-based task number
     * @param isDone new completion state
     */
    public MarkCommand(int taskNumber, boolean isDone) {
        this.taskNumber = taskNumber;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WenwenException {
        Task task = tasks.getByNumber(taskNumber);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.saveTasks(tasks.asList());
        ui.showTaskStatusChanged(task, isDone);
    }
}

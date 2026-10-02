package alfred.command;

import java.io.IOException;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;
import alfred.exception.AlfredException;
import alfred.task.Task;

/**
 * Marks a completed task as not done.
 */
public class UnmarkCommand extends Command {

    private final int taskNumber;

    /**
     * Creates a command that unmarks the task with the specified one-based number.
     *
     * @param taskNumber One-based number of the task to unmark.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AlfredException, IOException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AlfredException("Invalid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        ui.showTaskStatusChanged(false);
        storage.save(tasks);
        ui.showTask(task);
    }
}

package alfred.command;

import java.io.IOException;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;
import alfred.exception.AlfredException;
import alfred.task.Task;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {

    private final int taskNumber;

    /**
     * Creates a command that marks the task with the specified one-based number.
     *
     * @param taskNumber One-based number of the task to mark.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AlfredException, IOException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AlfredException("Invalid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        ui.showTaskStatusChanged(true);
        storage.save(tasks);
        ui.showTask(task);
    }
}

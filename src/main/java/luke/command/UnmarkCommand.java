package luke.command;

import java.io.IOException;

import luke.exception.LukeException;
import luke.storage.Storage;
import luke.task.Task;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Marks a task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that unmarks the specified task.
     *
     * @param taskNumber One-based number of the task to unmark.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws LukeException, IOException {
        Task unmarkedTask = tasks.unmark(taskNumber);
        storage.save(tasks.getTasks(), tasks.getTaskCount());
        ui.showTaskUnmarked(unmarkedTask);
    }
}

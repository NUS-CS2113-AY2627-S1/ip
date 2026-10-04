package luke.command;

import java.io.IOException;

import luke.exception.LukeException;
import luke.storage.Storage;
import luke.task.Task;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that marks the specified task.
     *
     * @param taskNumber One-based number of the task to mark.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws LukeException, IOException {
        Task markedTask = tasks.mark(taskNumber);
        storage.save(tasks.getTasks(), tasks.getTaskCount());
        ui.showTaskMarked(markedTask);
    }
}

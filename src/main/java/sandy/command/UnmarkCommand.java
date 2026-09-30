package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.exception.SandyException;
import sandy.storage.Storage;
import sandy.task.Task;

/**
 * Marks a task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that unmarks the given task number.
     *
     * @param taskNumber The one-based task number.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the selected task incomplete and saves the updated list.
     *
     * @param tasks The tasks managed by the application.
     * @param ui The console interface.
     * @param storage The task storage.
     * @throws SandyException If the task number does not exist.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SandyException {
        Task task = tasks.getTask(taskNumber);
        task.unmarkAsDone();
        ui.showUnmarkedTask(task);
        saveTasks(tasks, ui, storage);
    }
}

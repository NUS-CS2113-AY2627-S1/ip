package aigis;

import aigis.task.Task;

/**
 * Represents a command that removes a task by its one-based display number.
 */
public final class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a delete command.
     *
     * @param taskNumber The one-based number of the task to remove.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Removes the selected task when it exists.
     *
     * @param tasks The task list being changed.
     * @param ui The UI used to display the result.
     * @param storage The storage service, which saves when the application exits.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            ui.showMessage("That task does not exist.");
            return;
        }
        Task deletedTask = tasks.remove(taskIndex);
        ui.showMessage("Deleted task: " + deletedTask);
    }
}

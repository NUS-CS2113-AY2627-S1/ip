package zack.command;

import zack.Ui;
import zack.task.Task;
import zack.task.TaskList;

/** Removes a task by its index in the full task list. */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command to remove a task.
     *
     * @param taskIndex zero-based index in the full task list
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        Task deletedTask = tasks.remove(taskIndex);
        ui.showMessage(" Removed this task:");
        ui.showMessage("   " + deletedTask);
        ui.showMessage(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean changesTasks() {
        return true;
    }
}
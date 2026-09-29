package zack.command;

import zack.Ui;
import zack.task.Task;
import zack.task.TaskList;

/** Adds a task and displays the updated task count. */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command to add the given task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        tasks.add(task);
        ui.showMessage(" Got it. I've added this task:");
        ui.showMessage("   " + task);
        ui.showMessage(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean changesTasks() {
        return true;
    }
}
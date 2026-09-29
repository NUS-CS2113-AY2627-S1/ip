package zack.command;

import zack.Ui;
import zack.task.Task;
import zack.task.TaskList;

/** Marks a task as completed or not completed. */
public class MarkCommand extends Command {
    private final int taskIndex;
    private final boolean isDone;

    /**
     * Creates a command to update a task's completion status.
     *
     * @param taskIndex zero-based index in the full task list
     * @param isDone true to mark completed; false to mark not completed
     */
    public MarkCommand(int taskIndex, boolean isDone) {
        this.taskIndex = taskIndex;
        this.isDone = isDone;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            ui.showMessage(" Nice! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            ui.showMessage(" OK, I've marked this task as not done yet:");
        }
        ui.showMessage("   " + task);
    }

    /** {@inheritDoc} */
    @Override
    public boolean changesTasks() {
        return true;
    }
}
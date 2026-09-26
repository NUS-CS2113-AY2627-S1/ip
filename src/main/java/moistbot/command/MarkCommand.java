package moistbot.command;

import moistbot.task.Task;
import moistbot.ui.UserInterface;

/**
 * Marks a selected task as complete.
 */
public final class MarkCommand extends TaskCompletionCommand {
    /**
     * Creates a command for the supplied task number.
     *
     * @param taskNumber The 1-based number of the task to mark
     */
    public MarkCommand(int taskNumber) {
        super(taskNumber, true, "mark");
    }

    /**
     * Displays confirmation that the selected task is complete.
     *
     * @param ui The user interface used for feedback
     * @param task The task that was marked
     */
    @Override
    protected void showConfirmation(UserInterface ui, Task task) {
        ui.printMarkTask(task);
    }
}

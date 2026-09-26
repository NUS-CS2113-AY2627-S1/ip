package moistbot.command;

import moistbot.task.Task;
import moistbot.ui.UserInterface;

/**
 * Marks a selected task as incomplete.
 */
public final class UnmarkCommand extends TaskCompletionCommand {
    /**
     * Creates a command for the supplied task number.
     *
     * @param taskNumber The 1-based number of the task to unmark
     */
    public UnmarkCommand(int taskNumber) {
        super(taskNumber, false, "unmark");
    }

    /**
     * Displays confirmation that the selected task is incomplete.
     *
     * @param ui The user interface used for feedback
     * @param task The task that was unmarked
     */
    @Override
    protected void showConfirmation(UserInterface ui, Task task) {
        ui.printUnmarkTask(task);
    }
}

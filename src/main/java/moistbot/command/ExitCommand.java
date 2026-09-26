package moistbot.command;

import moistbot.storage.Storage;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Ends the current MoistBot session after displaying a farewell.
 */
public final class ExitCommand extends Command {
    /**
     * Creates an exit command without additional arguments.
     */
    public ExitCommand() {
    }

    /**
     * Displays the farewell before the application loop terminates.
     *
     * @param tasks The task list, which is unchanged by this command
     * @param ui The user interface used to display the farewell
     * @param storage The persistence service, which is unused by this command
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) {
        ui.printExit();
    }

    /**
     * Signals that this command ends the application.
     *
     * @return Always true
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

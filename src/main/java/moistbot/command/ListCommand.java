package moistbot.command;

import moistbot.storage.Storage;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

import java.time.LocalDate;

/**
 * Displays the current task list without modifying it.
 */
public final class ListCommand extends Command {
    private final LocalDate cutoffDate;

    /**
     * Creates a list command without additional arguments.
     */
    public ListCommand() {
        cutoffDate = null;
    }

    /**
     * Creates a list command that includes dated tasks up to an inclusive cutoff.
     *
     * @param cutoffDate The inclusive date limit
     */
    public ListCommand(LocalDate cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    /**
     * Displays every task in its current order.
     *
     * @param tasks The task list to display
     * @param ui The user interface used to display the tasks
     * @param storage The persistence service, which is unused by this command
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) {
        if (cutoffDate == null) {
            ui.printTasks(tasks);
            return;
        }
        ui.printTasksOnOrBefore(tasks, cutoffDate);
    }
}

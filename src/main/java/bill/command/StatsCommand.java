package bill.command;

import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Displays task completion totals.
 */
public class StatsCommand extends Command {
    /**
     * Creates a command that displays task totals.
     */
    public StatsCommand() {
    }

    /**
     * Displays the total, completed, and incomplete task counts.
     *
     * @param tasks Task list to summarize.
     * @param ui User interface for the counts.
     * @param storage Unused storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showStats(tasks);
    }
}

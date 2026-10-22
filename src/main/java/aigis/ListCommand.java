package aigis;

/**
 * Represents the command that displays all stored tasks.
 */
public final class ListCommand extends Command {
    /**
     * Displays the current task list.
     *
     * @param tasks The task list to display.
     * @param ui The UI used to display the tasks.
     * @param storage The storage service, which is not needed for listing.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks);
    }
}

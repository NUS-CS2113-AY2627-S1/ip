package moistbot.command;

import moistbot.storage.Storage;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Displays tasks whose descriptions contain a search term.
 */
public final class FindCommand extends Command {
    private final String searchTerm;

    /**
     * Creates a command that searches task descriptions for the supplied text.
     *
     * @param searchTerm The text that matching task descriptions must contain
     */
    public FindCommand(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * Displays all tasks with descriptions containing the search term.
     *
     * @param tasks The task list to search
     * @param ui The user interface used to display matching tasks
     * @param storage The persistence service, which is unused by this command
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) {
        ui.printMatchingTasks(tasks, searchTerm);
    }
}

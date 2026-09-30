package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;

/**
 * Finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches for the given keyword.
     *
     * @param keyword The keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Finds and displays tasks whose descriptions contain the keyword.
     *
     * @param tasks The tasks to search.
     * @param ui The console interface.
     * @param storage Unused because searching does not modify task data.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.findTasks(keyword));
    }
}

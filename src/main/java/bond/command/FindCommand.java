package bond.command;

import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Displays tasks whose descriptions contain a keyword without changing them.
 */
public class FindCommand extends Command {

    private final String keyword;

    /**
     * Creates a command that searches task descriptions for a keyword.
     *
     * @param keyword Nonempty keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Displays matching tasks with their original mission numbers.
     *
     * @param taskList Tasks available in the current session.
     * @param ui User interface used to display the matches.
     * @param storage File storage, which this read-only command does not use.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMatchingTasks(taskList, keyword);
    }
}

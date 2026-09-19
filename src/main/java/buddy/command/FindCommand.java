package buddy.command;

import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Shows every task whose description contains a given keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that finds tasks matching the given keyword.
     *
     * @param keyword text to search for within each task's description
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTaskList(tasks.find(keyword));
    }
}

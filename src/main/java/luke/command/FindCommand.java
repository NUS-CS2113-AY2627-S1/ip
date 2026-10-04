package luke.command;

import luke.storage.Storage;
import luke.task.Task;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches for the specified keyword.
     *
     * @param keyword Keyword to find in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task[] matchingTasks = tasks.find(keyword);
        ui.showFindResults(matchingTasks);
    }
}

package bob.command;

import bob.exception.TaskNotFoundError;
import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Displays tasks whose descriptions contain a keyword without changing the task list.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a search using a nonempty, case-sensitive keyword.
     *
     * @param keyword text to find within task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Displays descriptions matching the keyword, or throws an error if none match.
     * Leaves the task list and saved file unchanged.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TaskNotFoundError {
        TaskList matches = tasks.findMatches(keyword);
        if (matches.size() == 0) {
            throw new TaskNotFoundError();
        }
        ui.showMatchingTasks(matches);
    }
}

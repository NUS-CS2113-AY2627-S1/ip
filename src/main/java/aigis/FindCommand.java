package aigis;

import java.util.List;

import aigis.task.Task;

/**
 * Represents a command that displays tasks matching a search keyword.
 */
public final class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for a keyword.
     *
     * @param keyword The text to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Displays all tasks whose descriptions contain the search keyword.
     *
     * @param tasks The task list to search.
     * @param ui The UI used to display matching tasks.
     * @param storage The storage service, which is not needed for searching.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.find(keyword);
        ui.showTasks(matchingTasks);
    }
}

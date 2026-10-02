package buddy.command;

import java.util.List;

import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Finds and displays the tasks whose description contains a given keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that finds tasks matching the given keyword.
     *
     * @param keyword the text to search for within each task's description
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matches = tasks.find(keyword);
        ui.showMatchingTasks(matches);
    }
}

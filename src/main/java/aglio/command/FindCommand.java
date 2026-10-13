package aglio.command;

import aglio.storage.Storage;
import aglio.task.Task;
import aglio.task.TaskList;
import aglio.ui.Ui;

import java.util.ArrayList;

/** Represents the "find" command that searches tasks by keyword. */
public class FindCommand extends Command {

    private final String keyword;

    /**
     * Creates a FindCommand that searches for the given keyword.
     *
     * @param keyword the search term to match against task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matches = tasks.findTasks(keyword);
        ui.showFindResults(matches);
    }
}

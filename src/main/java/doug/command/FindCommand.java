package doug.command;

import java.util.ArrayList;

import doug.storage.Storage;
import doug.task.Task;
import doug.task.TaskList;
import doug.ui.Ui;

/**
 * Finds and displays tasks whose descriptions match a keyword.
 */
public class FindCommand extends Command {
    /** Keyword used to search for matching tasks. */
    private String keyword;

    /**
     * Creates a command that searches for the specified keyword.
     *
     * @param keyword keyword to find in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Finds tasks matching the keyword and displays them.
     *
     * @param taskList task list to search
     * @param ui user interface used to display matching tasks
     * @param storage storage supplied by the application
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ArrayList<Task> foundTasks = taskList.findTasks(keyword);
        ui.printFoundTasks(foundTasks);
    }
}

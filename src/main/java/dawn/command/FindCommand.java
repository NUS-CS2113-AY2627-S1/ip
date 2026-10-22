package dawn.command;

import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

import java.util.ArrayList;

/**
 * Command to find tasks containing a specific search keyword in their description.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a find command for the given keyword.
     *
     * @param keyword the case-insensitive keyword to look for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command, filtering the task list and displaying the matches.
     *
     * @param tasks the current list of tasks.
     * @param ui the user interface component.
     * @param storage the file storage system.
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            ui.showMessage(String.format("I couldn't find any tasks matching '%s'.\n\n", keyword));
            return;
        }

        StringBuilder sb = new StringBuilder("I found these tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append(String.format("%d.%s\n", i + 1, matchingTasks.get(i)));
        }
        ui.showMessage(sb.toString());
    }
}

package yao.command;

import java.util.ArrayList;
import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.task.Task;

/**
 * Command to search for tasks containing a specified keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified search keyword.
     *
     * @param keyword The keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }
}

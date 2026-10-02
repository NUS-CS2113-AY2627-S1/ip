package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;
import chre.task.Task;
import java.util.List;

/**
 * Finds tasks matching a keyword in the task list.
 */
public class FindCommand extends Command {
    private String keyword;

    /**
     * Creates a FindCommand with the given search keyword.
     *
     * @param keyword the keyword to search for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException {
        List<Task> matchingTasks = taskList.findTasks(keyword);
        ui.showFoundTasks(matchingTasks, keyword);
    }
}

package jamal.command;

import java.util.List;

import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;
import jamal.task.Task;

/** Shows every task whose description contains a keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches for the given keyword.
     *
     * @param keyword Text to look for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matches = tasks.find(keyword);
        ui.showFoundTasks(matches);
    }
}

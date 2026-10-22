package eric.command;

import java.util.List;

import eric.storage.Storage;
import eric.task.Task;
import eric.task.TaskList;
import eric.ui.Ui;

/** Shows the tasks whose description contains a keyword. The task list is not changed. */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    /** An example of a command that finds tasks, for messages that suggest what to type. */
    public static final String EXAMPLE_FIND = "find book";

    private final String keyword;

    /**
     * Creates a command that finds tasks.
     *
     * @param keyword Text to look for in the descriptions of the tasks, ignoring upper and lower case.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }
}

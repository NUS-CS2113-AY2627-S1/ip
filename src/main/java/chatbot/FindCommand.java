package chatbot;

import java.util.List;

import chatbot.task.Task;

/** Finds tasks whose descriptions contain a keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /** Creates a find command for the supplied keyword.
     *
     * @param keyword keyword to search for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** Finds and displays tasks matching the keyword.
     *
     * @param tasks task list to search
     * @param ui interface used to display the matches
     * @param storage unused storage handler
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }
}

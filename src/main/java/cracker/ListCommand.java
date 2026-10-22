package cracker;

import java.util.List;

/**
 * Represents a command that displays every task.
 */
public class ListCommand extends Command {
    /**
     * Displays the current task list.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}

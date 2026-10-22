package bryte.command;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;
import bryte.task.Task;

/**
 * Represents a command to find tasks that contain a specific keyword.
 */
public class FindCommand extends Command {
    private String keyword;

    /**
     * Constructs a FindCommand for the given keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    /**
     * Executes the find command by searching for tasks that contain the keyword.
     *
     * @param tasks The task list.
     * @param ui The user interface.
     * @param storage The storage for saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showDivider();
        ui.showMessage(" Here are the matching tasks in your list:");

        int count = 1;
        for (Task task : tasks.getTasks()) {
            if (task.getDescription().toLowerCase().contains(keyword)) {
                ui.showMessage(" " + count + "." + task.toString());
                count++;
            }
        }

        if (count == 1) {
            ui.showMessage(" No matching tasks found!");
        }
        ui.showDivider();
    }
}

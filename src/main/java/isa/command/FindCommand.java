package isa.command;

import isa.storage.Storage;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Lists tasks with descriptions containing a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command to find a keyword in task descriptions.
     *
     * @param keyword Text to find.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Displays tasks whose descriptions contain the keyword.
     *
     * @param tasks Current task list.
     * @param ui Console interface for matching tasks.
     * @param storage Task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks, tasks.findMatchingIndices(keyword));
    }
}

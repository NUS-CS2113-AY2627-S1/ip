package commands;

import tasks.TaskList;
import ui.Ui;

/**
 * Displays tasks whose descriptions contain the requested case-sensitive text.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a description search without changing or saving any task.
     *
     * @param keyword Non-empty search text validated by the parser.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMatchingTasks(tasks.find(keyword));
    }
}

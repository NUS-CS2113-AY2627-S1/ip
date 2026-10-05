package hedy.command;

import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Finds tasks whose description contains a keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches task descriptions.
     *
     * @param keyword text to look for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showFound(tasks.find(keyword));
    }
}

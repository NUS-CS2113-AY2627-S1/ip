package nico.command;

import nico.Storage;
import nico.TaskList;
import nico.Ui;

/**
 * Shows the tasks whose description contains a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that will search for the given keyword.
     *
     * @param keyword The text to search task descriptions for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showMatchingTasks(taskList.find(keyword));
    }
}
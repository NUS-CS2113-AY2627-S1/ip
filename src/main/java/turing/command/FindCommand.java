package turing.command;

import java.util.List;

import turing.Storage;
import turing.Ui;
import turing.task.Task;
import turing.task.TaskList;

/**
 * Shows the tasks whose description contains a given piece of text, so that a
 * user with a long list can pick out the few entries they mean.
 */
public class FindCommand extends Command {
    /** Text to look for in each task's description. */
    private final String searchText;

    /**
     * Creates a command that searches for the given text.
     *
     * @param searchText Text to look for in each task's description.
     */
    public FindCommand(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.find(searchText);
        ui.showMatchingTasks(matchingTasks);
    }
}

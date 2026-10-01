package floppy.command;

import java.util.List;

import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;
import floppy.task.Task;

/** Shows the tasks whose description contains a keyword. */
public class FindCommand extends Command {

    /** The word or phrase to look for in task descriptions. */
    private final String keyword;

    /**
     * Constructs a command that searches for the given keyword.
     *
     * @param keyword the word or phrase to look for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matches = tasks.find(keyword);
        ui.showMatchingTasks(matches, keyword);
    }
}

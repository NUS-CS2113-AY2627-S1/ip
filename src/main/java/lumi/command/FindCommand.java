package lumi.command;

import java.util.ArrayList;
import java.util.List;

import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;
import lumi.task.Task;

/**
 * Shows the tasks whose description mentions a keyword. The matches are
 * numbered from 1 in their own right, because those numbers are only there to
 * read off the screen; the numbers used by mark, unmark and delete still refer
 * to positions in the full list.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that will search for the given text.
     *
     * @param keyword Text to look for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Shows the matching tasks, or says that nothing mentions the keyword.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matches = tasks.find(keyword);
        if (matches.isEmpty()) {
            ui.show("No task in your list mentions \"" + keyword + "\".");
            return;
        }
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Here are the matching tasks in your list:");
        int number = TaskList.FIRST_TASK_NUMBER;
        for (Task task : matches) {
            lines.add(number + "." + task);
            number++;
        }
        ui.show(lines.toArray(new String[0]));
    }
}
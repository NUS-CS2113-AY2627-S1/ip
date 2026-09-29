package zack.command;

import zack.Ui;
import zack.task.Task;
import zack.task.TaskList;

import java.util.Locale;

/** Finds tasks whose descriptions contain a case-insensitive search phrase. */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a case-insensitive search command.
     *
     * @param keyword non-empty text to find in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword.toLowerCase(Locale.ROOT);
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMessage(" Here are the matching tasks:");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(keyword)) {
                ui.showMessage(" " + (i + 1) + "." + task);
                count++;
            }
        }
        ui.showMessage(" " + count + " matching tasks.");
    }
}
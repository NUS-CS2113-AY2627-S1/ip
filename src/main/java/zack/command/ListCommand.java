package zack.command;

import zack.Ui;
import zack.task.TaskList;

/** Displays all tasks in their current order. */
public class ListCommand extends Command {
    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMessage(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.get(i));
        }
    }
}
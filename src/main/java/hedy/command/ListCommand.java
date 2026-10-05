package hedy.command;

import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Prints every task in the list. */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks());
    }
}

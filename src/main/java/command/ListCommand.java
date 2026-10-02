package command;

import display.Ui;
import list.TaskList;
import storage.Storage;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.displayList(tasks);
    }
}
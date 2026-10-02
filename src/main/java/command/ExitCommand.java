package command;

import display.Ui;
import list.TaskList;
import storage.Storage;

public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.displayGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
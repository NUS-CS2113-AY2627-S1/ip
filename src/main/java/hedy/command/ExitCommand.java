package hedy.command;

import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Says goodbye and tells Hedy to stop reading commands. */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

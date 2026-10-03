package turing.command;

import turing.Storage;
import turing.Ui;
import turing.task.TaskList;

/** Says goodbye and ends the conversation. */
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

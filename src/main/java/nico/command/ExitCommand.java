package nico.command;

import nico.Storage;
import nico.TaskList;
import nico.Ui;

/**
 * Ends the program after saying goodbye.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showFarewell();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
package ducky.command;

import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/** Ends the Ducky command loop. Ducky itself prints the farewell message once the loop ends. */
public class ExitCommand extends Command {
    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        // Nothing to do: there is nothing to show or save when exiting.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

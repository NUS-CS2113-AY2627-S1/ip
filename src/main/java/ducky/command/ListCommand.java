package ducky.command;

import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/** Shows every chore currently in the chore list. */
public class ListCommand extends Command {
    /**
     * Shows every chore in the list, or tells the user the list is empty.
     *
     * @param chores The chore list to display.
     * @param ui The Ui used to show the chores.
     * @param storage Not used by this command.
     */
    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        if (chores.size() == 0) {
            ui.showMessage("QUACK! Your pond is empty. Add a chore with todo, deadline or event.");
            return;
        }
        ui.showChores(chores);
    }
}
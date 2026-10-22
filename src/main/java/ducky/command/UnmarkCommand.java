package ducky.command;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/** Marks the chore at a given rank as not done. */
public class UnmarkCommand extends Command {
    /** Zero-based index, within the chore list, of the chore to unmark. */
    private final int choreIndex;

    /**
     * Creates a command that marks the chore at the given index as not done.
     *
     * @param choreIndex Zero-based index of the chore to unmark.
     */
    public UnmarkCommand(int choreIndex) {
        this.choreIndex = choreIndex;
    }

    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        if (!isValidChoreIndex(choreIndex, chores, ui)) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsUndone();
        saveChores(chores, storage, ui);
        ui.showMessage("oh! actl im not done quaking " + chore.getDescription());
    }
}

package ducky.command;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/** Marks the chore at a given rank as done. */
public class MarkCommand extends Command {
    /** Zero-based index, within the chore list, of the chore to mark. */
    private final int choreIndex;

    /**
     * Creates a command that marks the chore at the given index as done.
     *
     * @param choreIndex Zero-based index of the chore to mark.
     */
    public MarkCommand(int choreIndex) {
        this.choreIndex = choreIndex;
    }

    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        if (!isValidChoreIndex(choreIndex, chores, ui)) {
            return;
        }
        Chore chore = chores.get(choreIndex);
        chore.markAsDone();
        saveChores(chores, storage, ui);
        ui.showMessage("done quacking " + chore.getDescription());
    }
}

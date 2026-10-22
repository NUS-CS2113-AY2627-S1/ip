package ducky.command;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/**
 * Deletes the chore at a given rank, announcing it (using the rank it had
 * before deletion) and showing the updated chore list, now renumbered with
 * no gaps.
 */
public class DeleteCommand extends Command {
    /** Zero-based index, within the chore list, of the chore to delete. */
    private final int choreIndex;

    /**
     * Creates a command that deletes the chore at the given index.
     *
     * @param choreIndex Zero-based index of the chore to delete.
     */
    public DeleteCommand(int choreIndex) {
        this.choreIndex = choreIndex;
    }

    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        if (!isValidChoreIndex(choreIndex, chores, ui)) {
            return;
        }
        Chore deleted = chores.get(choreIndex);
        String deletedLine = deleted.toNumberedString(choreIndex + 1);
        chores.remove(choreIndex);
        ui.showMessage(deletedLine + " waddled away!");
        ui.showMessage("Now chorelist is:");
        ui.showChores(chores);
        ui.showMessage("Now you have " + chores.size() + " chores in the list.");
        saveChores(chores, storage, ui);
    }
}

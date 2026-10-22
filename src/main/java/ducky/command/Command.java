package ducky.command;

import java.io.IOException;

import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/**
 * Represents one user command, already recognised and parsed by
 * {@link ducky.parser.Parser}. A command knows how to carry out itself
 * against the chore list, reporting through the ui and persisting through
 * storage as needed.
 */
public abstract class Command {
    /**
     * Carries out this command.
     *
     * @param chores The chore list to read or change.
     * @param ui The ui used to show the result to the user.
     * @param storage The storage used to persist any change to the chore list.
     */
    public abstract void execute(ChoreList chores, Ui ui, Storage storage);

    /**
     * Returns whether this command should end the Ducky command loop.
     * Every command keeps the loop running, except {@link ExitCommand}.
     *
     * @return {@code true} if this command should end the command loop.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the chore list to disk, reporting an error through the ui if
     * saving fails. Shared by every command that changes the chore list, so
     * each one does not need to repeat its own try-catch.
     *
     * @param chores The chore list to save.
     * @param storage The storage used to write the save file.
     * @param ui The ui used to report a saving error, if any.
     */
    protected void saveChores(ChoreList chores, Storage storage, Ui ui) {
        try {
            storage.save(chores);
        } catch (IOException e) {
            ui.showMessage("QUACK?! Couldn't save chores: " + e.getMessage());
        }
    }

    /**
     * Checks that a zero-based chore index refers to an existing chore,
     * reporting an error through the ui if it does not. Shared by every
     * command that acts on a single chore by rank (mark, unmark, delete).
     *
     * @param choreIndex The zero-based chore index to check.
     * @param chores The chore list, used to check the index is in range.
     * @param ui The ui used to report an out-of-range index, if any.
     * @return {@code true} if the index refers to an existing chore.
     */
    protected boolean isValidChoreIndex(int choreIndex, ChoreList chores, Ui ui) {
        if (choreIndex < 0 || choreIndex >= chores.size()) {
            ui.showMessage("QUACK?! There's no chore number " + (choreIndex + 1) + ".");
            return false;
        }
        return true;
    }
}

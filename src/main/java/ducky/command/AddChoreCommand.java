package ducky.command;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/**
 * Adds one already-built chore to the chore list. Used for every chore type
 * (to-do, deadline, event) alike, since adding a chore works the same way
 * regardless of its type: only how {@link ducky.parser.Parser} built the
 * chore differs between them.
 */
public class AddChoreCommand extends Command {
    /** The chore to add. */
    private final Chore chore;

    /**
     * Creates a command that adds the given chore to the chore list.
     *
     * @param chore The chore to add.
     */
    public AddChoreCommand(Chore chore) {
        this.chore = chore;
    }

    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        chores.add(chore);
        saveChores(chores, storage, ui);
        ui.showMessage("QUACKDDING! I've added this chore:");
        ui.showMessage("  " + chore);
        ui.showMessage("Now you have " + chores.size() + " chores in the list.");
    }
}

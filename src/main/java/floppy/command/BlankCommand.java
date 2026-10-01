package floppy.command;

import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;

/**
 * Responds to an empty line. Storing a blank task would clutter the list, so Floppy
 * stays in character and ignores it instead.
 */
public class BlankCommand extends Command {

    /** Constructs a command that responds to an empty line. */
    public BlankCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBlankInput();
    }
}

package floppy.command;

import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;

/** Ends the conversation. The farewell is shown once the main loop stops. */
public class ExitCommand extends Command {

    /** Constructs a command that ends the conversation. */
    public ExitCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Nothing to do: isExit() stops the loop, and Floppy says goodbye afterwards.
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExit() {
        return true;
    }
}

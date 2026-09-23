package lumi.command;

import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;

/**
 * Ends the session. Nothing happens when it runs, because the farewell is
 * printed once the command loop has finished rather than by the command
 * itself; all this command carries is the decision to stop.
 */
public class ExitCommand extends Command {
    /**
     * Does nothing, since the farewell is printed after the loop ends.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Nothing to do: stopping is reported by the caller after the loop ends.
    }

    /** Returns true, which is what makes the command loop stop. */
    @Override
    public boolean isExit() {
        return true;
    }
}

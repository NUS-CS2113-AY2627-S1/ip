package lumi.command;

import lumi.LumiException;
import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;

/**
 * One thing the user asked Lumi to do, ready to be carried out.
 * A command already holds whatever it parsed out of the input, so running it
 * needs only the three parts it may touch: the task list, the display and the
 * store. Subclasses that end the session override {@link #isExit()}.
 */
public abstract class Command {
    /**
     * Carries out this command.
     *
     * @param tasks The list to read or change.
     * @param ui Where to report the outcome.
     * @param storage Where to record a changed list.
     * @throws LumiException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws LumiException;

    /** Returns true only for the command that ends the session. */
    public boolean isExit() {
        return false;
    }
}

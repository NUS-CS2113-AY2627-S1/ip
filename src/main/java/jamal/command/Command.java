package jamal.command;

import jamal.JamaLException;
import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;

/**
 * A single action requested by the user, produced by the Parser.
 * Each subclass carries the details it needs and performs its action in
 * {@link #execute(TaskList, Ui, Storage)}.
 */
public abstract class Command {
    /**
     * Performs the command.
     *
     * @param tasks Task list to read or change.
     * @param ui Ui used to reply to the user.
     * @param storage Storage used to save any change.
     * @throws JamaLException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws JamaLException;

    /**
     * Returns whether the app should exit after this command.
     *
     * @return True only for the exit command.
     */
    public boolean isExit() {
        return false;
    }
}

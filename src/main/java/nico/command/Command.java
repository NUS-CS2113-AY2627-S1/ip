package nico.command;

import nico.NicoException;
import nico.Storage;
import nico.TaskList;
import nico.Ui;

/**
 * Represents a single action the user asked Nico to perform.
 * Each type of command (add, delete, list, ...) is its own subclass.
 */
public abstract class Command {

    /**
     * Carries out this command.
     *
     * @param taskList The user's tasks.
     * @param ui Used to show the result to the user.
     * @param storage Used to save any changes to disk.
     * @throws NicoException If the command cannot be completed.
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws NicoException;

    /**
     * Returns whether this command should end the program.
     *
     * @return True only for the exit command.
     */
    public boolean isExit() {
        return false;
    }
}
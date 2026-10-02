package buddy.command;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * A single user command, ready to be run against the current task list,
 * UI, and storage once {@link buddy.parser.Parser} has made sense of the
 * raw input line.
 */
public abstract class Command {

    /**
     * Carries out this command's effect: updating the task list, saving
     * to storage, and/or showing output, as appropriate.
     *
     * @param tasks the current task list
     * @param ui where to show any output
     * @param storage where to save any changes to the task list
     * @throws BuddyException if the command cannot be carried out, e.g.
     *     it refers to a task number that no longer exists
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException;

    /**
     * Returns whether Buddy should stop running after this command.
     *
     * @return true if this command should end the session, false otherwise
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Checks that a 1-based task number refers to an existing task,
     * throwing a {@link BuddyException} if it doesn't. Shared by any
     * command that operates on an existing task by number.
     *
     * @param index the 1-based task number to check
     * @param taskCount how many tasks currently exist
     * @throws BuddyException if the index is out of range
     */
    protected static void requireValidIndex(int index, int taskCount) throws BuddyException {
        if (index < 1 || index > taskCount) {
            throw new BuddyException("Buddy task number " + index + " does not exist.");
        }
    }
}

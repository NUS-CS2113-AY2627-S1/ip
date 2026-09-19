package buddy.command;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Represents a single user command that can be carried out against the task
 * list. Each kind of command (adding a task, deleting one, exiting, etc.) is
 * its own subclass, so Buddy's main loop does not need to know how any
 * particular command works.
 */
public abstract class Command {

    /**
     * Carries out this command.
     *
     * @param tasks task list to read from or update
     * @param ui used to show the result to the user
     * @param storage storage used to persist the task list if it changes
     * @throws BuddyException if the command cannot be carried out
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException;

    /**
     * Checks whether this command should end the program after it runs.
     *
     * @return true if Buddy should exit; false for every command except "bye"
     */
    public boolean isExit() {
        return false;
    }
}

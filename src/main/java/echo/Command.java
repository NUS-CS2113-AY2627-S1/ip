package echo;

import java.io.UncheckedIOException;

/**
 * A single user command that has already been parsed and is ready to run.
 * Each subclass performs one kind of action on the task list.
 */
public abstract class Command {

    /** Creates a command; subclasses store whatever arguments they need. */
    protected Command() {
    }

    /**
     * Carries out this command.
     *
     * @param tasks   the task list to read or change.
     * @param ui      where to show results to the user.
     * @param storage where to persist the task list after a change.
     * @throws EchoException if the command cannot be carried out, e.g. the task number does not exist.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws EchoException;

    /**
     * Returns whether the program should stop after this command.
     * Only the exit command returns {@code true}.
     *
     * @return {@code true} if the main loop should end after this command.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Checks that a 1-based task number refers to a task in the list.
     *
     * @param tasks      the current task list.
     * @param taskNumber the 1-based position typed by the user.
     * @throws EchoException if there is no task at that position.
     */
    protected static void checkTaskNumber(TaskList tasks, int taskNumber) throws EchoException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EchoException("There is no task number " + taskNumber + " in the list.");
        }
    }

    /**
     * Persists the task list. A failure is reported to the user rather than
     * crashing the program -- the in-memory list is still intact even if the
     * save itself did not succeed.
     *
     * @param tasks   the task list to save.
     * @param ui      where to report a save failure.
     * @param storage where to save to.
     */
    protected static void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks.asList());
        } catch (UncheckedIOException e) {
            ui.showError("Could not save your tasks: " + e.getCause().getMessage());
        }
    }
}

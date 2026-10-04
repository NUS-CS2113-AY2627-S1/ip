package dobby.command;

import dobby.storage.Storage;
import dobby.task.TaskList;
import dobby.ui.Ui;

/**
 * Represents the command that ends the Dobby session.
 */
public final class ExitCommand extends Command {

    /** Creates an exit command. */
    public ExitCommand() {
    }

    /**
     * Executes the exit response.
     *
     * @param tasks the task list, which is not needed for exiting.
     * @param ui the user interface used to display the exit response.
     * @param storage the storage handler, which is not needed for exiting.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!");
    }

    /**
     * Returns that this command ends the Dobby session.
     *
     * @return true because this is the exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

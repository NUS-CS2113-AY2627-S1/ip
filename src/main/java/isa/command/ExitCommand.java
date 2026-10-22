package isa.command;

import isa.storage.Storage;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Ends the application after showing a farewell message.
 */
public class ExitCommand extends Command {
    /**
     * Displays the farewell message.
     *
     * @param tasks Current task list.
     * @param ui Console interface for the message.
     * @param storage Task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Indicates that this command ends the application.
     *
     * @return True because this is the exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

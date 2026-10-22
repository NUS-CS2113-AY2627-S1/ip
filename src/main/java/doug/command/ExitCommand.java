package doug.command;

import doug.task.TaskList;
import doug.ui.Ui;
import doug.storage.Storage;
import doug.errors.DougException;

/**
 * Displays a farewell message and signals that the application should exit.
 */
public class ExitCommand extends Command {
    /**
     * Displays the farewell message and marks this command as an exit command.
     *
     * @param taskList task list supplied by the application
     * @param ui user interface used to display the farewell message
     * @param storage storage supplied by the application
     * @throws DougException if the command cannot be completed
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DougException {
        ui.printGoodbye();
        super.isExit = true;
    }
}

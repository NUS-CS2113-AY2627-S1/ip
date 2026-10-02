package slay69.command;

import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Displays the farewell message and signals that the application should exit.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that exits the chatbot.
     */
    public ExitCommand() {
    }

    /**
     * Displays the farewell message.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Signals to the application loop that it should stop.
     *
     * @return true because this is the exit command
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

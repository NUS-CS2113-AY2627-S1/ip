package moistbot.command;

import moistbot.exception.MoistBotException;
import moistbot.storage.Storage;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Represents an executable user command parsed from console input.
 */
public abstract class Command {
    /**
     * Executes this command using the application's collaborators.
     * @param tasks The task list to query or modify
     * @param ui The user interface used for feedback
     * @param storage The persistence service used after task changes
     * @throws MoistBotException if the command cannot be executed
     */
    public abstract void execute(TaskManager tasks, UserInterface ui, Storage storage) throws MoistBotException;

    /**
     * Returns whether executing this command should end the application.
     *
     * @return True if this command exits the application
     */
    public boolean isExit() {
        return false;
    }
}

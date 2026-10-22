package chatbot;

import chatbot.exception.AgentCTException;

/** Represents one executable chatbot command. */
public abstract class Command {
    /** Executes this command using the application's collaborators.
     *
     * @param tasks current task list
     * @param ui user interface
     * @param storage task storage
     * @throws AgentCTException when command execution encounters invalid input
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AgentCTException;

    /** Returns whether this command ends the application.
     *
     * @return true if this command ends the application
     */
    public boolean isExit() {
        return false;
    }
}

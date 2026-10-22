package mybff;

/** Represents an action that can be executed against the chatbot's current state. */
public abstract class Command {
    /**
     * Executes the command using the application's task list and services.
     *
     * @param tasks the current task list
     * @param ui the user interface for displaying responses
     * @param storage the storage used by commands that change tasks
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /** Returns whether the application should stop after executing this command. */
    public boolean isExit() {
        return false;
    }
}

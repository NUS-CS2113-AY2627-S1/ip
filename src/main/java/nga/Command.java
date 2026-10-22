package nga;

/** Represents one user command that can be executed by the application. */
public abstract class Command {
    /** Creates a command. */
    public Command() {
    }

    /**
     * Executes this command using the application's task list and storage.
     *
     * @param taskList the tasks affected by the command
     * @param storage the storage used when the command changes tasks
     * @return the message and exit status to show to the user
     * @throws TaskParseException if command-specific input is invalid
     */
    public abstract CommandResult execute(TaskList taskList, Storage storage) throws TaskParseException;

    /**
     * Returns whether executing this command should end the application.
     *
     * @return {@code true} when the command ends the application
     */
    public boolean isExit() {
        return false;
    }
}

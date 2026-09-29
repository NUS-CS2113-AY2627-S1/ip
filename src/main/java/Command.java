/**
 * An action requested by the user, already parsed and ready to run.
 * Each kind of command is a subclass that implements {@link #execute}.
 */
public abstract class Command {
    /**
     * Carries out this command and shows the result to the user.
     *
     * @throws ShaheerException if the command cannot be carried out, e.g. the task number does not exist.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws ShaheerException;

    /** Returns true if the chatbot should stop after this command. */
    public boolean isExit() {
        return false;
    }
}

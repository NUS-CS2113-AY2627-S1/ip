/**
 * One instruction from the user, ready to be carried out.
 * <p>
 * Each kind of command is a class of its own, so that adding an instruction to
 * the bot means writing a new class rather than extending a switch that grows
 * a case at a time. {@link Parser} decides which command the user asked for;
 * this is what that command then does.
 */
public abstract class Command {

    /**
     * Carries out this command.
     * <p>
     * Every command is handed all three parts of the session, whether or not it
     * needs them, so that the loop can run any command without knowing which
     * one it holds.
     *
     * @param tasks the task list to read or change.
     * @param ui the means of telling the user what happened.
     * @param storage the file to write the task list back to.
     * @throws CortisolException if the command cannot be carried out, with a
     *                           message already phrased for the user.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws CortisolException;

    /**
     * Returns whether the session should end after this command.
     * <p>
     * Only leaving does; every other command says no, which is why this is
     * answered here once rather than overridden in each subclass.
     *
     * @return true if this command ends the session.
     */
    public boolean isExit() {
        return false;
    }
}

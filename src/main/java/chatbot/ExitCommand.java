package chatbot;

/** Ends the chatbot session. */
public class ExitCommand extends Command {
    /** Displays the goodbye message and ends the command loop.
     *
     * @param tasks unused task list
     * @param ui interface used to display the goodbye message
     * @param storage unused storage handler
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /** Returns that this command ends the application.
     *
     * @return {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

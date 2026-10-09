package echo;

/**
 * Says goodbye and tells the main loop to stop.
 */
public class ExitCommand extends Command {

    /** Creates a command that ends the program. */
    public ExitCommand() {
    }

    /** Says goodbye to the user. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /** Returns {@code true}: the program stops after this command. */
    @Override
    public boolean isExit() {
        return true;
    }
}

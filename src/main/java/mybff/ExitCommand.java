package mybff;

/** Displays the farewell and signals that the application should stop. */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printFarewell();
    }

    /** Returns true to signal that the application should stop. */
    @Override
    public boolean isExit() {
        return true;
    }
}

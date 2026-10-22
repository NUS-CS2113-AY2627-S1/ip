package cracker;

import java.util.List;

/**
 * Represents the command that ends the chatbot session.
 */
public class ExitCommand extends Command {
    /**
     * Displays the farewell message.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        ui.showFarewell();
    }

    /**
     * Returns that this command ends the chatbot session.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

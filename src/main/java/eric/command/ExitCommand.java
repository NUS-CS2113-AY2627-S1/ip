package eric.command;

import eric.storage.Storage;
import eric.task.TaskList;
import eric.ui.Ui;

/** Ends the program, after saying goodbye to the user. */
public class ExitCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

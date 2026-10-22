package jamal.command;

import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;

/** Ends the session. The goodbye message is printed by the main loop. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Nothing to do: tasks are already saved after every change.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

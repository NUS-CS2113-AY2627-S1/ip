package zack.command;

import zack.Ui;
import zack.task.TaskList;

/** Displays a farewell message and requests application termination. */
public class ExitCommand extends Command {
    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMessage(" Bye. Hope to see you again soon!");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExit() {
        return true;
    }
}
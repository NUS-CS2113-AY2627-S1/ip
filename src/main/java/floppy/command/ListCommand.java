package floppy.command;

import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;

/** Shows every task Floppy is holding. */
public class ListCommand extends Command {

    /** Constructs a command that shows the whole task list. */
    public ListCommand() {
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.asList());
    }
}

package echo;

/**
 * Shows every task in the list.
 */
public class ListCommand extends Command {

    /** Creates a command that shows the whole task list. */
    public ListCommand() {
    }

    /** Shows every task in the list, numbered from 1. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.asList());
    }
}

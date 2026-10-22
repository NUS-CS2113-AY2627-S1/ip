package jamal.command;

import jamal.JamaLException;
import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;
import jamal.task.Task;

/** Adds an already-built task (todo, deadline or event) to the list. */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JamaLException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showMessage("added:", "  " + task, "you got " + tasks.countText() + " now.");
    }
}

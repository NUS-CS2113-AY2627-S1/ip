package floppy.command;

import floppy.FloppyException;
import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;
import floppy.task.Task;

/** Adds a task to the list. */
public class AddCommand extends Command {

    /** The task to add, already built from the user's input. */
    private final Task task;

    /**
     * Constructs a command that adds the given task.
     *
     * @param task the task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FloppyException if the tasks cannot be saved to disk.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws FloppyException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.save(tasks.asList());
    }
}

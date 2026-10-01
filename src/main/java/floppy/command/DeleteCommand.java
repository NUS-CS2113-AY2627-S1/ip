package floppy.command;

import floppy.FloppyException;
import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;
import floppy.task.Task;

/** Removes a task from the list. */
public class DeleteCommand extends Command {

    /** The whole line the user entered, which names the task to remove. */
    private final String input;

    /**
     * Constructs a command that removes the task the user picked.
     *
     * @param input the whole line the user entered, already stripped.
     */
    public DeleteCommand(String input) {
        this.input = input;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FloppyException if the command does not name a task that exists, or the
     *         tasks cannot be saved to disk.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws FloppyException {
        Task task = findTask(tasks, input);
        tasks.remove(task);
        ui.showTaskDeleted(task, tasks.size());
        storage.save(tasks.asList());
    }
}

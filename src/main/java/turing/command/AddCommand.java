package turing.command;

import turing.Storage;
import turing.Ui;
import turing.task.Task;
import turing.task.TaskList;

/**
 * Adds one task to the list. Todos, deadlines and events differ only in how
 * the user describes them, which the parser has already worked out by the
 * time this command is built, so all three are added the same way.
 */
public class AddCommand extends Command {
    /** Task to add. */
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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.getTaskCount());
        save(tasks, ui, storage);
    }
}

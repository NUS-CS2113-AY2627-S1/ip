package lumi.command;

import lumi.LumiException;
import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;
import lumi.task.Task;

/**
 * Adds one already-built task to the list. The three kinds of task differ only
 * in how they are parsed, so they all arrive here and are reported the same way.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that will add the given task.
     *
     * @param task The task already built from the user's input.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Appends the task, records the longer list and confirms what was added.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LumiException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.show("Got it. I've added this task:",
                Ui.TASK_INDENT + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}

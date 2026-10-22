package mybff;

import java.io.IOException;

/** Adds a parsed task and restores the previous list if saving fails. */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates an addition command for a task already validated by the parser.
     *
     * @param task the task to append to the list
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** Adds and saves the task, rolling back the addition if saving fails. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        try {
            storage.save(tasks);
        } catch (IOException | SecurityException exception) {
            tasks.delete(tasks.size() - 1);
            ui.printSaveError();
            return;
        }
        ui.printTaskAdded(task, tasks.size());
    }
}

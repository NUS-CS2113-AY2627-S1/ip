package floppy.command;

import floppy.FloppyException;
import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;
import floppy.task.Task;

/** Marks a task as done, or as not done yet. */
public class MarkCommand extends Command {

    /** The whole line the user entered, which names the task to change. */
    private final String input;

    /** True to mark the task done, false to mark it not done. */
    private final boolean shouldBeDone;

    /**
     * Constructs a command that changes the done status of the task the user picked.
     *
     * @param input        the whole line the user entered, already stripped.
     * @param shouldBeDone true to mark the task done, false to mark it not done.
     */
    public MarkCommand(String input, boolean shouldBeDone) {
        this.input = input;
        this.shouldBeDone = shouldBeDone;
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

        if (shouldBeDone) {
            task.markAsDone();
            ui.showTaskMarked(task);
        } else {
            task.markAsNotDone();
            ui.showTaskUnmarked(task);
        }
        storage.save(tasks.asList());
    }
}

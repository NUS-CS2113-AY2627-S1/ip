package mybff;

import java.io.IOException;

/** Changes a task's completion status and restores its previous status if saving fails. */
public class CompletionCommand extends Command {
    private final int index;
    private final boolean isMarkingAsDone;

    /**
     * Creates a completion command whose index is checked when it executes.
     *
     * @param index the zero-based index of the task to update
     * @param isMarkingAsDone whether to mark the task as done or not done
     */
    public CompletionCommand(int index, boolean isMarkingAsDone) {
        this.index = index;
        this.isMarkingAsDone = isMarkingAsDone;
    }

    /** Updates and saves a valid task's status, restoring its previous status if saving fails. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (!tasks.isValidIndex(index)) {
            ui.printInvalidTaskNumber();
            return;
        }
        Task task = tasks.get(index);
        boolean wasDone = task.getStatusIcon().equals("X");
        if (isMarkingAsDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        try {
            storage.save(tasks);
        } catch (IOException | SecurityException exception) {
            if (wasDone) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
            }
            ui.printSaveError();
            return;
        }
        ui.printCompletionChanged(task, isMarkingAsDone);
    }
}

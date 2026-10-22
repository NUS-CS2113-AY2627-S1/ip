package eric.command;

import eric.exception.EricException;
import eric.storage.Storage;
import eric.task.Task;
import eric.task.TaskList;
import eric.ui.Ui;

/** Marks a task as done, or as not done. */
public class MarkCommand extends Command {
    public static final String COMMAND_WORD_MARK = "mark";
    public static final String COMMAND_WORD_UNMARK = "unmark";

    private final int taskNumber;
    private final boolean isDone;

    /**
     * Creates a command that changes the done status of a task.
     *
     * @param taskNumber Number of the task, as shown by the list command (the first is 1).
     * @param isDone True to mark the task as done, false to mark it as not done.
     */
    public MarkCommand(int taskNumber, boolean isDone) {
        this.taskNumber = taskNumber;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EricException {
        String commandWord = isDone ? COMMAND_WORD_MARK : COMMAND_WORD_UNMARK;
        Task task = tasks.get(findTaskIndex(tasks, taskNumber, commandWord));
        if (isDone) {
            task.markDone();
            ui.showTaskMarked(task);
        } else {
            task.markUndone();
            ui.showTaskUnmarked(task);
        }
        saveTasks(tasks, ui, storage);
    }
}

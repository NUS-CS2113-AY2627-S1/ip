package hedy.command;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.Task;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Marks a task as done or not done, then saves the list. */
public class MarkCommand extends Command {
    private final int taskNumber;
    private final boolean isDone;

    /**
     * Creates a command that marks or unmarks a task.
     *
     * @param taskNumber number shown by {@code list}, starting at 1
     * @param isDone true to mark the task done, false to mark it not done
     */
    public MarkCommand(int taskNumber, boolean isDone) {
        this.taskNumber = taskNumber;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException {
        Task task = tasks.mark(taskNumber, isDone);
        storage.save(tasks.getTasks());
        ui.showMarked(task, isDone);
    }
}

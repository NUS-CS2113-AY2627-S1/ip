package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.CommandWord;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Changes a task's completion status and restores its original status if saving fails.
 */
public class SetCompletionCommand extends Command {
    private final int taskIndex;
    private final boolean isDone;

    /**
     * Constructs a command to mark or unmark the task at the given zero-based index.
     *
     * @param taskIndex the zero-based position of the task.
     * @param isDone {@code true} to mark the task, or {@code false} to unmark it.
     */
    public SetCompletionCommand(int taskIndex, boolean isDone) {
        this.taskIndex = taskIndex;
        this.isDone = isDone;
    }

    /**
     * Validates the index, changes the status, and confirms success after saving.
     *
     * @throws DawnException if the index is invalid or saving fails.
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        CommandWord commandWord = isDone ? CommandWord.MARK : CommandWord.UNMARK;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("I couldn't find that task number. Use: " + commandWord.usage());
        }
        Task task = tasks.getTask(taskIndex);
        boolean wasDone = task.isDone();
        task.setDone(isDone);
        try {
            storage.save(tasks);
        } catch (DawnException e) {
            task.setDone(wasDone);
            throw e;
        }
        String message = isDone
                ? "Nice! Marked this task as done:"
                : "Back on the list. Marked this task as not done yet:";
        ui.showMessage(message + "\n\t" + task + "\n\n");
    }
}

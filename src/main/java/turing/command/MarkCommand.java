package turing.command;

import turing.Storage;
import turing.TuringException;
import turing.Ui;
import turing.task.Task;
import turing.task.TaskList;

/**
 * Marks one task as done or as not done. Both directions are the same command
 * with the flag turned the other way, so they share this class rather than
 * duplicating the lookup and the error handling.
 */
public class MarkCommand extends Command {
    /** Task to change, numbered as the user sees it, starting at 1. */
    private final int taskNumber;

    /** True to mark the task as done, false to mark it as not done. */
    private final boolean isDone;

    /**
     * Creates a command that changes the done status of one task.
     *
     * @param taskNumber Task to change, numbered as the user sees it.
     * @param isDone True to mark the task as done, false to mark it as not done.
     */
    public MarkCommand(int taskNumber, boolean isDone) {
        this.taskNumber = taskNumber;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TuringException {
        requireStoredTaskNumber(tasks, taskNumber, getCommandWord());

        Task task = tasks.getTask(taskNumber);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        ui.showTaskMarked(task, isDone);
        save(tasks, ui, storage);
    }

    /**
     * Returns the word the user typed to reach this command, so that any
     * complaint quotes back the command they actually used.
     *
     * @return Either "mark" or "unmark".
     */
    private String getCommandWord() {
        return isDone ? CommandWord.MARK.getKeyword() : CommandWord.UNMARK.getKeyword();
    }
}

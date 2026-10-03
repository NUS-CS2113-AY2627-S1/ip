package turing.command;

import turing.Storage;
import turing.TuringException;
import turing.Ui;
import turing.task.Task;
import turing.task.TaskList;

/** Removes one task from the list. */
public class DeleteCommand extends Command {
    /** Task to remove, numbered as the user sees it, starting at 1. */
    private final int taskNumber;

    /**
     * Creates a command that removes one task.
     *
     * @param taskNumber Task to remove, numbered as the user sees it.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TuringException {
        requireStoredTaskNumber(tasks, taskNumber, CommandWord.DELETE.getKeyword());

        Task removedTask = tasks.remove(taskNumber);
        ui.showTaskRemoved(removedTask, tasks.getTaskCount());
        save(tasks, ui, storage);
    }
}

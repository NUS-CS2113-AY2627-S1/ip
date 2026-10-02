package nick.command;

import java.io.IOException;

import nick.NickException;
import nick.parser.Parser;
import nick.storage.Storage;
import nick.task.Task;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Marks a task as done or not done.
 */
public class MarkCommand extends Command {
    private final String argument;
    private final boolean isDone;

    /**
     * Creates a command that marks a task, identified by the given argument.
     *
     * @param argument The task number text supplied by the user.
     * @param isDone True to mark as done, false to mark as not done.
     */
    public MarkCommand(String argument, boolean isDone) {
        this.argument = argument;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NickException, IOException {
        int index = Parser.parseTaskNumber(argument, tasks.size());
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
            ui.showMarked(task);
        } else {
            task.markAsNotDone();
            ui.showUnmarked(task);
        }
        storage.save(tasks);
    }
}

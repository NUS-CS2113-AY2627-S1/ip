package nick.command;

import java.io.IOException;

import nick.NickException;
import nick.parser.Parser;
import nick.storage.Storage;
import nick.task.Task;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final String argument;

    /**
     * Creates a command that deletes a task, identified by the given argument.
     *
     * @param argument The task number text supplied by the user.
     */
    public DeleteCommand(String argument) {
        this.argument = argument;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NickException, IOException {
        int index = Parser.parseTaskNumber(argument, tasks.size());
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.showRemoved(removed, tasks.size());
    }
}

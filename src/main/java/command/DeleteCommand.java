package command;

import display.Ui;
import exception.AtlasException;
import list.TaskList;
import storage.Storage;
import task.Task;

public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        if (index < 0 || index >= tasks.size()) {
            throw new AtlasException("Invalid: Task number does not exist");
        }
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.displayTaskDeleted(removed, tasks);
    }
}
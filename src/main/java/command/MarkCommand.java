package command;

import display.Ui;
import exception.AtlasException;
import list.TaskList;
import storage.Storage;
import task.Task;

public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        if (index < 0 || index >= tasks.size()) {
            throw new AtlasException("Invalid: Task number does not exist");
        }
        Task task = tasks.getItem(index);
        if (isDone) {
            task.markAsDone();
            ui.displayTaskMarked(task);
        } else {
            task.markAsNotDone();
            ui.displayTaskUnmarked(task);
        }
        storage.save(tasks);
    }
}
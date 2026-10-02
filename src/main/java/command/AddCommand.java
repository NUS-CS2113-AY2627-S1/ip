package command;

import display.Ui;
import list.TaskList;
import storage.Storage;
import task.Task;

public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks);
        ui.displayAdd(task, tasks);
    }
}
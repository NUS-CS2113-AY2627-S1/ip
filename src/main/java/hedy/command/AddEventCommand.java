package hedy.command;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.Event;
import hedy.task.Task;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Adds an event to the task list and saves it. */
public class AddEventCommand extends Command {
    private final String description;
    private final String start;
    private final String end;

    /**
     * Creates a command that adds an event.
     *
     * @param description what the event is
     * @param start when it starts, as the user typed it
     * @param end when it ends, as the user typed it
     */
    public AddEventCommand(String description, String start, String end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException {
        Task task = new Event(description, start, end);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }
}

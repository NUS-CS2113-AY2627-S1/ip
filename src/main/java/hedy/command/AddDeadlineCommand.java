package hedy.command;

import java.time.LocalDate;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.Deadline;
import hedy.task.Task;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Adds a deadline to the task list and saves it. */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final LocalDate by;

    /**
     * Creates a command that adds a deadline.
     *
     * @param description what needs to be done
     * @param by the date it is due
     */
    public AddDeadlineCommand(String description, LocalDate by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException {
        Task task = new Deadline(description, by);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }
}

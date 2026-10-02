package chre.command;

import java.time.LocalDate;
import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;

/**
 * Adds an event task to the task list.
 */
public class EventCommand extends Command {
    private String name;
    private LocalDate from;
    private LocalDate to;

    /**
     * Creates an EventCommand with the given task name and time period.
     *
     * @param name the name of the event
     * @param from the start date
     * @param to the end date
     */
    public EventCommand(String name, LocalDate from, LocalDate to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException {
        taskList.addEvent(name, from, to);
        ui.showTaskAdded(taskList.getLastTask(), taskList.size());
        storage.save(taskList.getTasks());
    }
}

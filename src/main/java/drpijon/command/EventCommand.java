package drpijon.command;

import java.time.format.DateTimeParseException;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Event;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Adds an event task.
 */
public class EventCommand extends AddCommand {
    /**
     * Creates an event command.
     *
     * @param description event description, start time, and end time
     */
    public EventCommand(String description) {
        super(description);
    }

    /**
     * Creates, saves, and displays an event task.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the new task
     * @throws DrPijonException when the event structure is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        String[] eventParts = getDescription().split("/from|/to", 3);
        if (eventParts.length < 3 || eventParts[0].isBlank() || eventParts[1].isBlank()
                || eventParts[2].isBlank()) {
            throw new DrPijonException("OI EVENT MUST INCLUDE /from AND /to >:( Try: event career fest /from 14 Sep "
                    + "/to 21 Sep");
        }

        Event event;
        try {
            event = Event.fromText(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        } catch (DateTimeParseException e) {
            event = Event.fromLegacy(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        }
        addTask(event, tasks, ui, storage);
    }
}

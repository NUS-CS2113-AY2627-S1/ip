package kevie.commands;

import kevie.Kevie;
import kevie.Storage;
import kevie.UserInterface;
import kevie.exceptions.CmdSynNoArgException;
import kevie.exceptions.CmdSyntaxException;
import kevie.tasks.Event;
import kevie.tasks.Task;
import kevie.tasks.TaskList;

/**
 * Command that creates an event task in the todo list.
 */
public class EventCommand extends TaskSaveCommand {

    private final String fromSeperator = "/from";
    private final String toSeperator = "/to";

    /**
     * Creates event command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public EventCommand(UserInterface ui, TaskList taskList, Storage storage) {
        super("event", ui, taskList, storage);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }

        String description;
        String[] eventArgs;
        Event newEvent;

        try {
            eventArgs = arg.split(" " + fromSeperator + " ");
            description = eventArgs[0];
            eventArgs = eventArgs[1].split(" " + toSeperator + " ");
        } catch (Exception e) {
            throw new CmdSyntaxException("Event description, start time or end time not indicated", this, ui);
        }

        try {
            newEvent = new Event(description, eventArgs[0], eventArgs[1]);
        } catch (Exception e) {
            throw new CmdSyntaxException("Event start time or end time not given", this, ui);
        }

        taskList.addTask(newEvent);
        storage.save(taskList, ui);
        ui.botSpeak("Good! Adding new event to the list: ");
        ui.botSpeak(newEvent.toString(), true);
        ui.botSpeak("You now have " + taskList.getLength() + " tasks.", true);
        return false;
    }

    @Override
    public String syntax() {
        return "event [Description] " + fromSeperator + " [Start Time] " + toSeperator + " [End Time]";
    }

    @Override
    public String example() {
        return "event CS2113 team project meeting " + fromSeperator + " Sep 1st 6pm " + toSeperator + " Sep 1st 8pm";
    }
}

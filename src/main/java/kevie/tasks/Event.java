package kevie.tasks;

import kevie.UserInterface;
import kevie.exceptions.FileBadRawException;

/**
 * Represents a task that is an upcoming event, with a start time and an end time.
 */
public class Event extends Task{

    private String startTime;
    private String endTime;

    /**
     * Creates an event task.
     *
     * @param description Description of event task
     * @param startTime Start time of the event
     * @param endTime End time of the event
     */
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates an event task from raw string (used in save files).
     *
     * @param rawArgs Arguments in the raw string
     * @param lineNo Save file line number for exception message printing
     * @param ui User interface to print exception in
     * @throws FileBadRawException If arguments has corrupted values
     */
    public Event(String[] rawArgs, int lineNo, UserInterface ui) throws FileBadRawException {
        super(rawArgs, lineNo, ui);
        try{
            this.startTime = rawArgs[3];
            this.endTime = rawArgs[4];
        } catch (Exception e){
            throw new FileBadRawException(lineNo, ui);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + startTime + ", to: " + endTime + ")";
    }

    @Override
    public char getType() {
        return 'E';
    }

    @Override
    public String getRaw() {
        return super.getRaw() + RAW_SEPERATOR + startTime + RAW_SEPERATOR + endTime;
    }
}

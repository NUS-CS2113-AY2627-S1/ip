package kevie.tasks;

import kevie.UserInterface;
import kevie.exceptions.FileBadRawException;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task{

    private String due;

    /**
     * Creates a deadline task.
     *
     * @param description Description of deadline task
     * @param endTime Due date of deadline
     */
    public Deadline(String description, String endTime) {
        super(description);
        this.due = endTime;
    }

    /**
     * Creates a deadline task from raw string (used in save files).
     *
     * @param rawArgs Arguments in the raw string
     * @param lineNo Save file line number for exception message printing
     * @param ui User interface to print exception in
     * @throws FileBadRawException If arguments has corrupted values
     */
    public Deadline(String[] rawArgs, int lineNo, UserInterface ui) throws FileBadRawException {
        super(rawArgs, lineNo, ui);
        try {
            this.due = rawArgs[3];
        } catch (Exception e){
            throw new FileBadRawException(lineNo, ui);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (due: " + due + ")";
    }

    @Override
    public String getRaw() {
        return super.getRaw() + RAW_SEPERATOR + due;
    }

    @Override
    public char getType() {
        return 'D';
    }
}

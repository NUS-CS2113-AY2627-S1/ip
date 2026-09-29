package kevie.tasks;

import kevie.UserInterface;
import kevie.exceptions.FileBadRawException;

/**
 * Represents a task in a todo list.
 */
public class Task {

    private final static String NOT_DONE_CHAR = "[ ]";
    private final static String DONE_CHAR = "[V]";

    /**
     * String that separates between different arguments in a save file.
     */
    public final static String RAW_SEPERATOR = "\\\\";

    private String description;
    private boolean isDone;

    /**
     * Creates a task
     *
     * @param description Description of task
     */
    public Task(String description)
    {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a task from raw string (used in save files).
     *
     * @param rawArgs Arguments in the raw string
     * @param lineNo Save file line number for exception message printing
     * @param ui User interface to print exception in
     * @throws FileBadRawException If arguments has corrupted values
     */
    public Task(String[] rawArgs, int lineNo, UserInterface ui) throws FileBadRawException {
        try{
            this.description = rawArgs[1];
            this.isDone = Boolean.parseBoolean(rawArgs[2]);
        } catch (Exception e) {
            throw new FileBadRawException(lineNo, ui);
        }
    }

    /**
     * Returns the description of the task.
     *
     * @return Description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status of the task (completed or not).
     *
     * @return Whether the task is done yet
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Set the status of the task (Mark task as done or undone).
     *
     * @param done Status you want to set the task to be
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Return the type ID character of the task.
     *
     * @return The type ID character of the task
     */
    public char getType(){
        return 'X';
    }

    @Override
    public String toString() {
        return  (isDone? DONE_CHAR: NOT_DONE_CHAR) + " [" + getType() + "]" + " " + description;
    }

    /**
     * Returns the string representation of task that is used to store it in save file.
     *
     * @return String representation of task
     */
    public String getRaw(){
        return getType() + RAW_SEPERATOR + getDescription() + RAW_SEPERATOR + isDone();
    }

}

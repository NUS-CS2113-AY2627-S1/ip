package kevie.tasks;

import kevie.UserInterface;
import kevie.exceptions.FileBadRawException;

/**
 * Represents a simple todo task.
 */
public class Todo extends Task{

    /**
     * Creates a todo task
     *
     * @param description Description of task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a todo task from raw string (used in save files).
     *
     * @param rawArgs Arguments in the raw string
     * @param lineNo Save file line number for exception message printing
     * @param ui User interface to print exception in
     * @throws FileBadRawException If there is an error scanning the save file.
     */
    public Todo(String[] rawArgs, int lineNo, UserInterface ui) throws FileBadRawException {
        super(rawArgs, lineNo, ui);
    }

    @Override
    public char getType() {
        return 'T';
    }
}

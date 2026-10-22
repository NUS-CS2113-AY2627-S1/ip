package task;

/**
 * Represents a todo task without any date/time attached.
 */
public class ToDo extends Task{
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon(){
        return "T";
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}

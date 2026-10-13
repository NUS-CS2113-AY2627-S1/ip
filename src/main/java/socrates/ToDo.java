package socrates;

/**
 * A simple task with no associated date.
 */
public class ToDo extends Task {

    public ToDo(String description) {
        super(description);
    }

    public String getStatusLine() {
        return String.format("[T]" + super.getStatusLine());
    }

    public String toSaveFormat() {
        return ("T | " + super.toSaveFormat());
    }

}

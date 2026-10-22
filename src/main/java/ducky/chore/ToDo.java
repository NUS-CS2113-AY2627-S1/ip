package ducky.chore;

/**
 * Represents a to-do: a chore with only a description and no attached date.
 */
public class ToDo extends Chore {

    /**
     * Creates a to-do with the given description.
     *
     * @param description Text describing the chore.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}

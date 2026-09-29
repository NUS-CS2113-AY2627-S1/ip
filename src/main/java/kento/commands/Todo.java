package kento.commands;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTaskIcon() {
        return "T";
    }

    @Override
    public String getTaskFile() {
        return getTaskIcon() + "|" + getStatusIcon() + "|" + getDescription();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s)", this.getStatusIcon(), this.getDescription());
    }

}

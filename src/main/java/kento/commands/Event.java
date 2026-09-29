package kento.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    private LocalDate from;
    private LocalDate to;

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;

    }

    @Override
    public String getDescription() {
        return String.format("%s (from: %s to: %s )", this.description, from
                .format(DateTimeFormatter.ofPattern("MMM d yyyy")),
                to
                        .format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    @Override
    public String getTaskIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s)", this.getStatusIcon(), this.getDescription());
    }

    @Override
    public String getTaskFile() {
        return getTaskIcon() + "|" + getStatusIcon() + "|" + this.description + "|" + this.to + "|" + this.from;
    }
}

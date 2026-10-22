package dexter.tasks;

import dexter.handler.DateParser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** A task with a deadline stored as a date-time rather than free-form text. */
public class Deadline extends Task {
    protected LocalDateTime dueDate;

    /** Parses the deadline; dates without a time are stored at midnight. */
    public Deadline(String description, String date) {
        super(description);
        this.dueDate = DateParser.parse(date);
        this.type = "D";
    }

    /**
     * Returns this task's due date and time.
     *
     * @return the deadline date and time
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /** Displays the date in English, including the time when it is not midnight. */
    @Override
    public String toString() {
        String pattern = dueDate.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? "MMM dd uuuu" : "MMM dd uuuu HH:mm";
        String formattedDate = dueDate.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
        return super.toString() + " (by: " + formattedDate + ")";
    }
}

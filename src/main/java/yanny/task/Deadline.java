package yanny.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a task due on a calendar date, optionally at a specific time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalTime time;

    /**
     * Creates a deadline due on the given date.
     *
     * @param description the text describing the task.
     * @param date the date by which the task must be completed.
     */
    public Deadline(String description, LocalDate date) {
        super(description);
        this.date = Objects.requireNonNull(date);
        this.time = null;
    }

    /**
     * Creates a deadline due at the given date and time.
     *
     * @param description the text describing the task.
     * @param dateTime the date and time by which the task must be completed.
     */
    public Deadline(String description, LocalDateTime dateTime) {
        super(description);
        LocalDateTime requiredDateTime = Objects.requireNonNull(dateTime);
        this.date = requiredDateTime.toLocalDate();
        this.time = requiredDateTime.toLocalTime();
    }

    /**
     * Returns the icon representing a dated deadline task.
     *
     * @return the deadline task type icon.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the date on which this task is due.
     *
     * @return the deadline date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the deadline time when one was supplied.
     *
     * @return the optional deadline time.
     */
    public Optional<LocalTime> getTime() {
        return Optional.ofNullable(time);
    }

    /**
     * Returns the deadline task with a readable date and optional time.
     *
     * @return the formatted deadline task.
     */
    @Override
    public String toString() {
        String displayedDeadline = date.format(DISPLAY_DATE);
        if (time != null) {
            displayedDeadline += " " + time.format(DISPLAY_TIME);
        }
        return super.toString() + " (by: " + displayedDeadline + ")";
    }
}

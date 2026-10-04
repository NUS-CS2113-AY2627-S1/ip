package dook.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.io.SerialisedData;
import dook.parser.DateTimeParser;

/**
 * Represents a task with due time.
 */
public class DeadlineTask extends Task {
    private static final DateTimeFormatter DEADLINE_FORMATTER =
        DateTimeFormatter.ofPattern("EEE, d MMM yyyy H:mm", Locale.UK);

    private static final Pattern PATTERN =
            Pattern.compile("^deadline\\s+{{phrase}}\\s+/by\\s+{{phrase}}$"
                                    .replace("{{phrase}}", "(\\S+(?:\\s+\\S+)*)"));
    private LocalDateTime deadline;

    /**
     * Constructs a new DeadlineTask with specified description and due time.
     *
     * @param  description   The description of the task.
     * @param  deadline      The due time as string.
     */
    public DeadlineTask(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Attempts parsing the user input into a DeadlineTask.
     * Looks for two phrases: first to be the description,
     * second to be the due time as string.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new DeadlineTask (optional).
     */
    public static Optional<Task> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> maybeDeadline = DateTimeParser.parseEndTime(matcher.group(2));
        if (maybeDeadline.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new DeadlineTask(matcher.group(1), maybeDeadline.get()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        return date.equals(deadline.toLocalDate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpired() {
        return deadline.isBefore(LocalDateTime.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisedData serialise() {
        SerialisedData data = super.serialise();
        data.add(deadline);
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalDateTime getSortValue() {
        return deadline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toStringWithTime() {
        return paddingForTimeSlot + deadline.format(TimeSlotFormatter)
                + " " + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[D]%s (by %s)".formatted(
            super.toString(),
            deadline.format(DEADLINE_FORMATTER)
        );
    }
}
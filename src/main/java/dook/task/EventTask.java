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
 * Represents a task with time frame.
 */
public class EventTask extends Task {
    private static final DateTimeFormatter FULL_FORMATTER =
        DateTimeFormatter.ofPattern("d MMM H:mm", Locale.UK);
    private static final DateTimeFormatter SHORT_FORMATTER =
        DateTimeFormatter.ofPattern("H:mm", Locale.UK);

    private static final Pattern PATTERN =
            Pattern.compile("^event\\s+{{phrase}}\\s+/from\\s+{{phrase}}\\s+/to\\s+{{phrase}}$"
                                    .replace("{{phrase}}", "(\\S+(?:\\s+\\S+)*)"));
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    /**
     * Constructs a new EventTask with specified description,
     * start time and end time.
     *
     * @param  description   The description of the task.
     * @param  startDatetime The start time as string.
     * @param  endDatetime   The end time as string.
     */
    public EventTask(
        String description,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime
    ) {
        super(description);
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    /**
     * Attempts parsing the user input into an EventTask.
     * Looks for three phrases: first to be the description,
     * second to be the start time as string,
     * third to be the end time as string.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new EventTask (optional).
     */
    public static Optional<Task> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> maybeStart = DateTimeParser.parseStartTime(matcher.group(2));
        if (maybeStart.isEmpty()) {
            return Optional.empty();
        }

        LocalDateTime startDatetime = maybeStart.get();
        Optional<LocalDateTime> maybeEnd = DateTimeParser.parseEndTime(
            matcher.group(3),
            startDatetime.toLocalDate()
        );
        if (maybeEnd.isEmpty()) {
            return Optional.empty();
        }

        LocalDateTime endDatetime = maybeEnd.get();
        if (endDatetime.isBefore(startDatetime)) {
            return Optional.empty();
        }

        return Optional.of(new EventTask(matcher.group(1), startDatetime, endDatetime));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        return date.compareTo(startDatetime.toLocalDate()) >= 0
            && date.compareTo(endDatetime.toLocalDate()) <= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpired() {
        return endDatetime.isBefore(LocalDateTime.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisedData serialise() {
        SerialisedData data = super.serialise();
        data.add(startDatetime, endDatetime);
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalDateTime getSortValue() {
        return startDatetime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toStringWithTime() {
        return startDatetime.format(TimeSlotFormatter)
                + "-" + endDatetime.format(TimeSlotFormatter)
                + " " + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        boolean sameDate = startDatetime.toLocalDate().equals(endDatetime.toLocalDate());
        return "[E]%s (from %s to %s)".formatted(
            super.toString(),
            startDatetime.format(FULL_FORMATTER),
            endDatetime.format(sameDate ? SHORT_FORMATTER : FULL_FORMATTER));
    }
}

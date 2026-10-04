package dook.task;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a basic task.
 */
public class ToDoTask extends Task {
    private static final Pattern PATTERN = Pattern.compile("^todo\\s+(\\S+(?:\\s+\\S+)*)$");

    /**
     * Constructs a new ToDoTask with specified description.
     *
     * @param  description The description of the task.
     */
    public ToDoTask(String description) {
        super(description);
    }

    /**
     * Attempts parsing the user input into a ToDoTask.
     * Looks for a phrase to be the description.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new ToDoTask (optional).
     */
    public static Optional<Task> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new ToDoTask(matcher.group(1)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalDateTime getSortValue() {
        return LocalDateTime.MIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toStringWithTime() {
        return paddingForTimeSlot.repeat(2) + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[T]%s".formatted(super.toString());
    }
}

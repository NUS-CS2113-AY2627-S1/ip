package dook.command.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dook.command.Command;
import dook.command.Response;
import dook.parser.DateTimeParser;
import dook.service.TaskManager;
import dook.task.Task;

/**
 * Represents the command for listing tasks on a specified date.
 */
public class DateCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^date\\s+(\\S+(?:\\s+\\S+)*)$");
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.UK);

    private LocalDate date;

    /**
     * Constructs a new DateCommand with specified user input and date.
     *
     * @param  userQuery Trimmed user input.
     * @param  date      The date to list tasks for.
     */
    public DateCommand(String userQuery, LocalDate date) {
        super(userQuery);
        this.date = date;
    }

    /**
     * Attempts parsing user input into a new DateCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new DateCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        Optional<LocalDateTime> maybeDateTime = DateTimeParser.parseStartTime(matcher.group(1));
        if (maybeDateTime.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new DateCommand(userQuery, maybeDateTime.get().toLocalDate()));
    }

    /**
     * Returns the tasks occurring on the specified date as a response.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        List<Task> tasksOnDate = taskManager.getTasksByDate(date);
        StringBuilder sb = new StringBuilder(date.format(DATE_FORMATTER));
        for (Task task : tasksOnDate) {
            sb.append("\n  " + task.toStringWithTime());
        }

        if (tasksOnDate.isEmpty()) {
            sb.append("\n  No tasks found. Take a day off.");
        }

        return new Response(sb.toString());
    }
}

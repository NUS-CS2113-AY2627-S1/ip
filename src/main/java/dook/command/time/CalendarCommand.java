package dook.command.time;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dook.command.Command;
import dook.command.Response;
import dook.service.TaskManager;
import dook.task.Task;

/**
 * Represents the command for displaying a monthly calendar with task density indicators.
 */
public class CalendarCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^calendar$");

    private static final int SLOT_WIDTH = 4;
    private static final int CALENDAR_WIDTH = SLOT_WIDTH * 7 + 1;
    private static final String DATE_FORMAT = "%" + (SLOT_WIDTH - 1) + "s";
    private static final String DAYS_OF_WEEK = List.of(
        "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"
    ).stream()
     .map(dow -> String.format("%" + SLOT_WIDTH + "s", dow))
     .collect(Collectors.joining(""));

    private static final List<String> INDICATORS = List.of(" ", "░", "▒", "▓", "█");

    /**
     * Constructs a new CalendarCommand with specified user input.
     *
     * @param  userQuery Trimmed user input.
     */
    public CalendarCommand(String userQuery) {
        super(userQuery);
    }

    /**
     * Attempts parsing user input into a new CalendarCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new CalendarCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new CalendarCommand(userQuery));
    }

    /**
     * Returns the calendar for the current month as a response.
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
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        String calendar = drawCalendar(taskManager, year, month);
        return new Response(calendar);
    }

    /**
     * Returns the calendar header for the given year and month.
     *
     * @param  year  The year.
     * @param  month The month.
     * @return       The formatted calendar header.
     */
    private static String getHeader(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        String header = yearMonth.getMonth().toString() + " " + year;
        String padding = " ".repeat((CALENDAR_WIDTH - header.length()) / 2 + 1);

        return padding + header + "\n" + DAYS_OF_WEEK + "\n ";
    }

    /**
     * Returns the density indicator for the given date.
     *
     * @param  taskManager The task manager containing the task list.
     * @param  date        The date to check.
     * @return             The density indicator.
     */
    private static String getIndicator(TaskManager taskManager, LocalDate date) {
        List<Task> tasksByDate = taskManager.getTasksByDate(date);

        String indicator = INDICATORS.get(
            Math.min(
                tasksByDate.size(),
                INDICATORS.size()
            )
        );
        return indicator;
    }

    /**
     * Returns the drawn calendar for the given year and month.
     *
     * @param  taskManager The task manager containing the task list.
     * @param  year        The year.
     * @param  month       The month.
     * @return             The formatted calendar.
     */
    private static String drawCalendar(
        TaskManager taskManager,
        int year, int month
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int firstDayOfWeek = LocalDate.of(year, month, 1)
                                      .getDayOfWeek()
                                      .getValue() - 1;

        StringBuilder sb = new StringBuilder(getHeader(year, month));
        sb.append(" ".repeat(firstDayOfWeek * SLOT_WIDTH));

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            String indicator = getIndicator(taskManager, date);
            boolean shouldNewLine = (day + firstDayOfWeek) % 7 == 0;

            sb.append(DATE_FORMAT.formatted(day))
              .append(indicator)
              .append(shouldNewLine ? "\n " : "");
        }
        return sb.toString();
    }
}

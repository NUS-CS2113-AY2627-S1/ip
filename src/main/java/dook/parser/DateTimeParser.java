package dook.parser;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ArrayList;

import java.time.Instant;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;

/**
 * Parses flexible date and time strings into LocalDateTime objects.
 */
public abstract class DateTimeParser {
    private static List<DateTimeFormatter> formatters = new ArrayList<>(List.of(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.ISO_INSTANT,
        DateTimeFormatter.ISO_LOCAL_DATE,
        DateTimeFormatter.ISO_OFFSET_DATE
    ));

    static {
        List<String> datePatterns = new ArrayList<>();
        List<String> dowPatterns = List.of("E", "EEEE");
        List<String> timePatterns = List.of("HH", "H", "hh", "h").stream()
            .flatMap(hour -> List.of("'h'", "'H'", ":", "").stream()
                .flatMap(hourSep -> List.of("'m'", ":", "''", " ").stream()
                    .map(minuteSep -> "%s[%s][mm[%sss['s']]][ ][a]"
                        .formatted(hour, hourSep, minuteSep))))
            .toList();

        for (String day : List.of("d", "dd")) {
            for (String year : List.of("uu", "uuuu")) {
                for (String month : List.of("M", "MM")) {
                    for (String sep : List.of("-", "/", ".")) {
                        datePatterns.add(
                            "%s'{{sep}}'%s['{{sep}}'%s]".replace("{{sep}}", sep)
                                                        .formatted(day, month, year)
                        );
                        datePatterns.add(
                            "%s'{{sep}}'%s'{{sep}}'%s".replace("{{sep}}", sep)
                                                      .formatted(year, month, day)
                        );
                    }
                }

                for (String month : List.of("MMM", "MMMM")) {
                    datePatterns.add(
                        "%s %s[[,] %s]".formatted(day, month, year)
                    );
                    datePatterns.add(
                        "%s %s[[,] %s]".formatted(month, day, year)
                    );
                }
            }
        }

        List<String> formatPatterns = new ArrayList<>();
        for (String time : timePatterns) {
            for (String dayOfWeek : dowPatterns) {
                for (String date : datePatterns) {
                    formatPatterns.add("[%s[,] ]%s[['T'][ ][' at ']%s]"
                        .formatted(dayOfWeek, date, time)
                    );
                }
                formatPatterns.add("%s[['T'][ ][' at ']%s]"
                    .formatted(dayOfWeek, time)
                );
            }
            formatPatterns.add(time);
        }

        formatters.addAll(
            formatPatterns.stream()
                          .map(pattern -> DateTimeFormatter.ofPattern(pattern, Locale.UK))
                          .toList()
        );
    }

    /**
     * Returns the local date represented by the temporal accessor.
     *
     * @param  temporal      The temporal accessor to read.
     * @param  referenceDate The reference date used when the year is missing.
     * @return               The resolved local date.
     */
    private static LocalDate getLocalDate(
        TemporalAccessor temporal,
        LocalDate referenceDate
    ) {
        boolean hasMonthDay = temporal.isSupported(ChronoField.DAY_OF_MONTH)
                           && temporal.isSupported(ChronoField.MONTH_OF_YEAR);

        if (hasMonthDay) {
            int day = temporal.get(ChronoField.DAY_OF_MONTH);
            int month = temporal.get(ChronoField.MONTH_OF_YEAR);

            if (temporal.isSupported(ChronoField.YEAR)) {
                int year = temporal.get(ChronoField.YEAR);
                return LocalDate.of(year, month, day);
            }

            LocalDate currentYearDate = LocalDate.of(referenceDate.getYear(), month, day);
            return currentYearDate.isBefore(referenceDate)
                 ? currentYearDate.plusYears(1) : currentYearDate;
        }

        if (temporal.isSupported(ChronoField.DAY_OF_WEEK)) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            return referenceDate.with(TemporalAdjusters.nextOrSame(dow));
        }

        return referenceDate;
    }

    /**
     * Returns the local time represented by the temporal accessor.
     *
     * @param  temporal   The temporal accessor to read.
     * @param  isDeadline Whether the time is for a deadline.
     * @return            The resolved local time.
     */
    private static LocalTime getLocalTime(
        TemporalAccessor temporal,
        boolean isDeadline
    ) {
        int hour;
        int defaultMinute = 0;
        int defaultSecond = 0;

        if (temporal.isSupported(ChronoField.HOUR_OF_DAY)) {
            hour = temporal.get(ChronoField.HOUR_OF_DAY);

        } else if (temporal.isSupported(ChronoField.CLOCK_HOUR_OF_AMPM)) {
            boolean isPm = temporal.isSupported(ChronoField.AMPM_OF_DAY)
                        && temporal.get(ChronoField.AMPM_OF_DAY) == 1;
            hour = temporal.get(ChronoField.CLOCK_HOUR_OF_AMPM) % 12
                    + (isPm ? 12 : 0);

        } else if (isDeadline) {
            hour = 23;
            defaultMinute = 59;
            defaultSecond = 59;

        } else {
            hour = 0;
        }

        int minute = temporal.isSupported(ChronoField.MINUTE_OF_HOUR)
                   ? temporal.get(ChronoField.MINUTE_OF_HOUR) : defaultMinute;
        int second = temporal.isSupported(ChronoField.SECOND_OF_MINUTE)
                   ? temporal.get(ChronoField.SECOND_OF_MINUTE) : defaultSecond;
        return LocalTime.of(hour, minute, second);
    }

    /**
     * Returns the parsed local date-time from the given input.
     *
     * @param  input         The input string to parse.
     * @param  referenceDate The reference date used when the year is missing.
     * @param  isDeadline    Whether the time is for a deadline.
     * @return               The parsed local date-time, if successful.
     */
    private static Optional<LocalDateTime> parse(
        String input,
        LocalDate referenceDate,
        boolean isDeadline
    ) {
        for (DateTimeFormatter formatter : formatters) {
            TemporalAccessor temporal;
            try {
                temporal = formatter.parse(input);
            } catch (DateTimeParseException e) {
                continue;
            }

            LocalDateTime dateTime = temporal.isSupported(ChronoField.INSTANT_SECONDS)
                ? LocalDateTime.ofInstant(Instant.from(temporal), ZoneOffset.UTC)
                : LocalDateTime.of(
                    getLocalDate(temporal, referenceDate),
                    getLocalTime(temporal, isDeadline)
                );
            return Optional.of(dateTime);
        }
        return Optional.empty();
    }

    /**
     * Parses a start time from the given input.
     *
     * @param  input The input string to parse.
     * @return       The parsed start time, if successful.
     */
    public static Optional<LocalDateTime> parseStartTime(String input) {
        LocalDate today = LocalDate.now();
        return parse(input, today, false);
    }

    /**
     * Parses an end time from the given input.
     *
     * @param  input The input string to parse.
     * @return       The parsed end time, if successful.
     */
    public static Optional<LocalDateTime> parseEndTime(String input) {
        LocalDate today = LocalDate.now();
        return parse(input, today, true);
    }

    /**
     * Parses an end time from the given input using a default date.
     *
     * @param  input       The input string to parse.
     * @param  defaultDate The default date to use.
     * @return             The parsed end time, if successful.
     */
    public static Optional<LocalDateTime> parseEndTime(String input, LocalDate defaultDate) {
        return parse(input, defaultDate, true);
    }
}

package bryte.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bryte.exception.BryteException;

/**
 * Utility class for parsing and formatting date and time strings.
 */
public class DateTimeUtil {

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("d/M/yyyy")
    };

    /**
     * Prevents instantiation of this utility class.
     */
    private DateTimeUtil() {
    }

    /**
     * Parses a date/time string into a LocalDateTime object.
     * If no time is provided, it defaults to 00:00.
     *
     * @param dateTimeString The string to parse.
     * @return The parsed LocalDateTime object.
     * @throws BryteException If the string cannot be parsed into a known date/time format.
     */
    public static LocalDateTime parse(String dateTimeString) throws BryteException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            throw new BryteException("Date/time cannot be empty.");
        }

        dateTimeString = dateTimeString.trim();
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // Try parsing with time first
                return LocalDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException e) {
                try {
                    // Try parsing as just a date and add start of day
                    return LocalDate.parse(dateTimeString, formatter).atStartOfDay();
                } catch (DateTimeParseException ex) {
                    // Continue to next formatter
                }
            }
        }
        throw new BryteException("Please enter date in a valid format "
                + "(e.g., yyyy-MM-dd, yyyy-MM-dd HHmm, dd/MM/yyyy HHmm).");
    }

    /**
     * Formats a LocalDateTime object into a string for display.
     *
     * @param dateTime The LocalDateTime object.
     * @return The formatted string (e.g., Oct 15 2019, 18:00).
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime.getHour() == 0 && dateTime.getMinute() == 0) {
            return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a"));
    }

    /**
     * Formats a LocalDateTime object into a string for file storage.
     *
     * @param dateTime The LocalDateTime object.
     * @return The formatted string (e.g., yyyy-MM-dd HHmm).
     */
    public static String formatForStorage(LocalDateTime dateTime) {
        if (dateTime.getHour() == 0 && dateTime.getMinute() == 0) {
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}

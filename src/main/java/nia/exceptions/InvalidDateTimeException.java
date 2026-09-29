package nia.exceptions;

/** Thrown when a "/by", "/from", or "/to" value doesn't match any accepted date-time format. */
public class InvalidDateTimeException extends NiaParserException {
    /** Creates the exception for badValue, the date-time string that couldn't be parsed. */
    public InvalidDateTimeException(String badValue) {
        super(
                String.format(
                        "[Debug] Could not parse date-time \"%s\". Accepted formats: yyyy-MM-dd HHmm, "
                                + "yyyy/MM/dd HHmm, yyyy-MM-dd HH:mm, yyyy/MM/dd HH:mm, or a date alone "
                                + "(time defaults to 23:59), e.g. \"deadline return book /by 2019-10-15 1800\"",
                        badValue),
                "That's not a date I recognize... try yyyy-mm-dd, with or without a time.");
    }
}

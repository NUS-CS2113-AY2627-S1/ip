package nia.exceptions;

/**
 * Thrown when a "/by", "/from", or "/to" value has the right shape for an accepted
 * date-time format (right count/position of digits) but an impossible value within
 * it - e.g. month 30, or a day that doesn't exist in that month (Feb 30). Distinct
 * from InvalidDateTimeException, which covers input that doesn't match any accepted
 * format's shape at all.
 */
public class InvalidDateValueException extends NiaParserException {
    /** Creates the exception for badValue, the date-time string whose shape matched but whose value didn't. */
    public InvalidDateValueException(String badValue, String reason) {
        super(
                String.format(
                        "[Debug] Date-time \"%s\" has the right shape but an invalid value: %s",
                        badValue, reason),
                "That date doesn't actually exist... double check the month and day.");
    }
}

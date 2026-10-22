package bob;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses user commands and validates their task details.
 */
public class Parser {
    /**
     * Creates a command parser.
     */
    public Parser() {
    }

    /**
     * Returns the task number supplied after an action.
     *
     * @param userCmd Full command entered by the user.
     * @param action Action whose task number should be parsed.
     * @return Parsed task number.
     * @throws BobException If the task number is missing or invalid.
     */
    public int parseTaskNumber(String userCmd, String action) throws BobException {
        String taskNumberStr = userCmd.substring(action.length()).trim();
        if (taskNumberStr.isEmpty()) {
            throw new BobException("Give me a task number, partner.");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberStr);
        } catch (NumberFormatException exception) {
            throw new BobException("That doesn't look like a valid task number.");
        }

        return taskNumber;
    }

    /**
     * Returns the text supplied after an action.
     *
     * @param userCmd Full command entered by the user.
     * @param action Action whose description should be parsed.
     * @return Parsed description.
     * @throws BobException If no description was supplied.
     */
    public String parseDescription(String userCmd, String action) throws BobException {
        String description = userCmd.substring(action.length()).trim();
        if (description.isEmpty()) {
            throw new BobException("Give me something to work with, partner.");
        }

        return description;
    }

    /**
     * Returns the description, date, and remaining time of a deadline command.
     *
     * @param userCmd Full deadline command entered by the user.
     * @return Parsed deadline details.
     * @throws BobException If the deadline details are missing or invalid.
     */
    public String[] parseDeadline(String userCmd) throws BobException {
        String details = userCmd.substring("deadline".length());
        if (details.isEmpty()) {
            throw new BobException("Every task needs a description, partner.");
        }
        if (!details.contains(" /by ")) {
            throw new BobException("That deadline needs a /by date and time, partner.");
        }

        int byIndex = details.indexOf(" /by ");
        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new BobException("Every task needs a description, partner.");
        }
        if (by.isEmpty()) {
            throw new BobException("That deadline needs a /by date and time, partner.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HHmm")
                .withResolverStyle(ResolverStyle.STRICT);
        LocalDateTime deadlineInfo;
        try {
            deadlineInfo = LocalDateTime.parse(by, formatter);
        } catch (DateTimeParseException exception) {
            throw new BobException("Use DD/MM/YYYY HHMM for the deadline. For example, 31/12/2099 1200.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (!deadlineInfo.isAfter(now)) {
            throw new BobException("That date has already ridden into the sunset. Pick a future deadline.");
        }
        Duration duration = Duration.between(now, deadlineInfo);
        String[] output = {description, by, String.valueOf(duration.toDays()), String.valueOf(duration.toHoursPart())};
        return output;
    }

    /**
     * Returns the description, start, and end of an event command.
     *
     * @param userCmd Full event command entered by the user.
     * @return Parsed event details.
     * @throws BobException If the event details are missing or invalid.
     */
    public String[] parseEvent(String userCmd) throws BobException {
        String details = userCmd.substring("event".length());
        if (details.isEmpty()) {
            throw new BobException("Every task needs a description, partner.");
        }
        if (!details.contains(" /from ") || !details.contains(" /to ")) {
            throw new BobException("That event needs both /from and /to, partner.");
        }

        int fromIndex = details.indexOf(" /from ");
        int fromStartIndex = fromIndex + " /from ".length();
        int toIndex = details.indexOf(" /to ");
        if (fromStartIndex > toIndex) {
            throw new BobException("Put /from before /to so the trail runs in the right direction.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromStartIndex, toIndex).trim();
        String to = details.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new BobException("Every task needs a description, partner.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BobException("Both the start and end of the event are needed, partner.");
        }
        String[] output = {description, from, to};
        return output;
    }
}

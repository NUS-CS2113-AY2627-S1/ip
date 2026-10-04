package dook.command.time;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dook.command.Command;

/**
 * Represents the command for listing tasks occurring today.
 */
public class TodayCommand extends DateCommand {
    private static final Pattern PATTERN = Pattern.compile("^today$");

    /**
     * Constructs a new TodayCommand with the specified user input.
     *
     * @param userQuery Trimmed user input.
     */
    public TodayCommand(String userQuery) {
        super(userQuery, LocalDate.now());
    }

    /**
     * Attempts parsing user input into a new TodayCommand.
     *
     * @param userQuery Trimmed user input.
     * @return The new TodayCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new TodayCommand(userQuery));
    }
}

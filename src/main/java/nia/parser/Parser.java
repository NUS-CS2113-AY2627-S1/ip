package nia.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import nia.exceptions.EmptyFindKeywordException;
import nia.exceptions.EmptyTodoDescriptionException;
import nia.exceptions.IncorrectTaskIndexArgumentCountException;
import nia.exceptions.InvalidDateTimeException;
import nia.exceptions.InvalidDateValueException;
import nia.exceptions.InvalidTaskIndexException;
import nia.exceptions.MissingDeadlineFieldsException;
import nia.exceptions.MissingEventFieldsException;
import nia.exceptions.NiaParserException;
import nia.exceptions.NonNumericTaskIndexException;
import nia.exceptions.UnknownListFilterException;
import nia.tasks.TaskList;

/**
 * Turns raw input text into a clean, canonical array of words for Processor.
 * Owns all input sanitization: trimming, blank-input guarding, word-splitting,
 * command-alias resolution (e.g. "close"/"exit"/"quit" all mean "bye"), and the
 * command-specific shape of the "add task" commands (todo/deadline/event).
 * Processor should never see a non-canonical command word, and should be able
 * to trust the array shape for each command without re-splitting anything.
 */
public class Parser {
    /**
     * Maps alternate spellings of a command to its canonical form.
     * This is the single place to register new aliases (e.g. "dl" -> "deadline").
     */
    private static final java.util.Map<String, String> ALIASES = java.util.Map.of(
            "close", "bye",
            "exit", "bye",
            "quit", "bye",
            "td", "todo",
            "dl", "deadline",
            "ls", "list",
            "d", "delete"
    );

    /** The one format Nia's save file uses - unambiguous, always includes a time. */
    private static final DateTimeFormatter CANONICAL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Friendly display format for task listings, e.g. "Oct 15 2019, 6:00pm". */
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    /** Time assumed when the user gives a date with no time component. */
    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);

    /**
     * Date-time formats accepted from user input: "-" or "/" date separator, "HHmm" or "HH:mm" time.
     * STRICT resolution (and "uuuu" rather than "yyyy" for the year, which STRICT needs to resolve
     * unambiguously) rejects calendar-impossible dates like Feb 30 instead of SMART's default behavior
     * of silently clamping them to Feb 28.
     */
    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/MM/dd HHmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm").withResolverStyle(ResolverStyle.STRICT)
    );

    /** Date-only formats accepted from user input; time defaults to DEFAULT_TIME. Same STRICT reasoning as above. */
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = List.of(
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT)
    );

    /**
     * Splits `rawInput` into a canonical command word plus command-specific
     * fields, e.g.:
     *   "td buy milk"                        -> ["todo", "buy milk"]
     *   "dl return book /by Sunday"          -> ["deadline", "return book", "Sunday"]
     *   "event exam /from Mon /to Tue"       -> ["event", "exam", "Mon", "Tue"]
     * Other commands (bye/list/mark/unmark, and unrecognized words) fall back to a
     * plain whitespace split with only the first word alias-resolved.
     * Returns an empty array if `rawInput` is blank - callers should treat that
     * as "nothing to process" and skip it. A todo/deadline/event missing a
     * required field (description, "/by", "/from", or "/to") throws the
     * matching NiaParserException rather than coming back with a blank field
     * for Processor to catch later.
     */
    public static String[] parse(String rawInput) throws NiaParserException {
        String trimmed = rawInput.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }

        int firstSpace = trimmed.indexOf(' ');
        String commandWord = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);
        String rest = firstSpace == -1 ? "" : trimmed.substring(firstSpace + 1).trim();
        String command = ALIASES.getOrDefault(commandWord, commandWord);

        switch (command) {
            case "todo":
                return parseTodo(command, rest);
            case "deadline":
                return parseDeadline(command, rest);
            case "event":
                return parseEvent(command, rest);
            case "list":
                return parseList(command, rest);
            case "find":
                return parseFind(command, rest);
            default:
                String[] words = trimmed.split("\\s+");
                words[0] = command;
                return words;
        }
    }

    /** Rejects a blank description rather than letting an empty todo through. */
    private static String[] parseTodo(String command, String rest) throws EmptyTodoDescriptionException {
        if (rest.isEmpty()) {
            throw new EmptyTodoDescriptionException();
        }
        return new String[]{command, rest};
    }

    /**
     * Splits `rest` on "/by" into description and by-fields; throws if either comes
     * back blank, or if by isn't a date-time in any accepted format. The returned by
     * is always in Parser's canonical format, so every later stage (Processor, Task,
     * Storage) can trust it's already valid instead of re-validating.
     */
    private static String[] parseDeadline(String command, String rest) throws NiaParserException {
        int byIndex = rest.indexOf("/by");
        String description = byIndex == -1 ? rest.trim() : rest.substring(0, byIndex).trim();
        String by = byIndex == -1 ? "" : rest.substring(byIndex + "/by".length()).trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new MissingDeadlineFieldsException();
        }
        String canonicalBy = formatCanonical(parseDateTime(by));
        return new String[]{command, description, canonicalBy};
    }

    /**
     * Splits `rest` on "/from" and "/to" into description, from, and to; throws if any
     * comes back blank, or if from/to isn't a date-time in any accepted format. Both
     * are returned in Parser's canonical format, for the same reason as parseDeadline's by.
     */
    private static String[] parseEvent(String command, String rest) throws NiaParserException {
        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");

        String description = fromIndex == -1 ? rest.trim() : rest.substring(0, fromIndex).trim();

        String from = "";
        if (fromIndex != -1) {
            int fromEnd = (toIndex != -1 && toIndex > fromIndex) ? toIndex : rest.length();
            from = rest.substring(fromIndex + "/from".length(), fromEnd).trim();
        }

        String to = toIndex == -1 ? "" : rest.substring(toIndex + "/to".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MissingEventFieldsException();
        }
        String canonicalFrom = formatCanonical(parseDateTime(from));
        String canonicalTo = formatCanonical(parseDateTime(to));
        return new String[]{command, description, canonicalFrom, canonicalTo};
    }

    /**
     * Splits `rest` (empty, or a filter token such as "t" or "todo") into
     * an optional list filter. An empty `rest` means "no filter" (list everything);
     * otherwise the token is resolved to the task tag ("T"/"D"/"E") it selects.
     */
    private static String[] parseList(String command, String rest) throws UnknownListFilterException {
        if (rest.isEmpty()) {
            return new String[]{command};
        }
        return new String[]{command, resolveListFilterTag(rest)};
    }

    /**
     * Maps a list filter token to the task tag ("T"/"D"/"E") it selects, e.g.
     * "t"/"todo" -> "T", "d"/"dl"/"deadline" -> "D", "e"/"event" -> "E",
     * matched case-insensitively. Any other token is rejected rather than
     * silently matching nothing.
     */
    private static String resolveListFilterTag(String filterToken) throws UnknownListFilterException {
        switch (filterToken.toLowerCase()) {
            case "t":
            case "todo":
                return "T";
            case "d":
            case "dl":
            case "deadline":
                return "D";
            case "e":
            case "event":
                return "E";
            default:
                throw new UnknownListFilterException(filterToken);
        }
    }

    /** Rejects a blank keyword rather than letting an empty find match everything. */
    private static String[] parseFind(String command, String rest) throws EmptyFindKeywordException {
        if (rest.isEmpty()) {
            throw new EmptyFindKeywordException();
        }
        return new String[]{command, rest};
    }

    /**
     * Parses and validates `words` (expects exactly [command, "<number>"]) into a
     * one-indexed task number. Shared by mark/unmark/delete, whose argument shape
     * is identical - only what happens to the referenced task differs.
     */
    public static int parseAndValidateTaskIndex(String[] words, TaskList taskList) throws NiaParserException {
        if (words.length != 2) {
            throw new IncorrectTaskIndexArgumentCountException(words[0]);
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            throw new NonNumericTaskIndexException(words[1]);
        }

        if (taskList.isNotValidIndex(taskIndex)) {
            throw new InvalidTaskIndexException(taskIndex, taskList.getSize());
        }
        return taskIndex;
    }

    /**
     * Parses `raw` (already trimmed) against every accepted date-time format in
     * turn - "-" or "/" as the date separator, "HHmm" or "HH:mm" as the time.
     * A date given with no time component defaults to 23:59 that day. Shared by
     * parseDeadline/parseEvent, whose "/by"/"/from"/"/to" fields all accept the
     * same formats.
     * If every format fails, but at least one failure was a value out of range
     * within an otherwise correctly-shaped input (e.g. month 30, or Feb 30 - a
     * day that doesn't exist in that month), that's reported as InvalidDateValueException
     * instead of the more generic "wrong format" InvalidDateTimeException, since
     * the user got the shape right and just needs to fix a specific value.
     */
    public static LocalDateTime parseDateTime(String raw) throws InvalidDateTimeException, InvalidDateValueException {
        String invalidValueReason = null;
        for (DateTimeFormatter format : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(raw, format);
            } catch (DateTimeParseException e) {
                invalidValueReason = invalidValueReason(e).orElse(invalidValueReason);
            }
        }
        for (DateTimeFormatter format : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(raw, format).atTime(DEFAULT_TIME);
            } catch (DateTimeParseException e) {
                invalidValueReason = invalidValueReason(e).orElse(invalidValueReason);
            }
        }
        if (invalidValueReason != null) {
            throw new InvalidDateValueException(raw, invalidValueReason);
        }
        throw new InvalidDateTimeException(raw);
    }

    /**
     * Distinguishes a value out of range within an otherwise correctly-shaped input (e.g. month 30,
     * or Feb 30 under STRICT resolution) from a plain shape mismatch (e.g. "Sunday", or a missing
     * digit) - java.time reports the former with "Invalid value for" or "Invalid date" in its
     * message, and the latter as "could not be parsed at index N".
     */
    private static Optional<String> invalidValueReason(DateTimeParseException e) {
        String message = e.getMessage();
        if (message != null && (message.contains("Invalid value for") || message.contains("Invalid date"))) {
            return Optional.of(message.substring(message.indexOf(':') + 2));
        }
        return Optional.empty();
    }

    /** Formats `dateTime` the way Nia's save file persists it - lossless, machine-parseable. */
    public static String formatCanonical(LocalDateTime dateTime) {
        return dateTime.format(CANONICAL_FORMATTER);
    }

    /** Parses a string that's already in the save file's canonical format back into a LocalDateTime. */
    public static LocalDateTime parseCanonical(String canonical) {
        return LocalDateTime.parse(canonical, CANONICAL_FORMATTER);
    }

    /** Formats `dateTime` for the user to read, e.g. "Oct 15 2019, 6:00pm". */
    public static String formatDisplay(LocalDateTime dateTime) {
        // DateTimeFormatter's "a" pattern renders AM/PM in uppercase regardless of
        // case in the pattern string itself, so lowercase it afterward to match style.
        String formatted = dateTime.format(DISPLAY_FORMATTER);
        return formatted.replace("AM", "am").replace("PM", "pm");
    }
}
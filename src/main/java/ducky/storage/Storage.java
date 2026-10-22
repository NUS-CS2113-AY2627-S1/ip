package ducky.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.chore.Deadline;
import ducky.chore.Event;
import ducky.chore.ToDo;

/**
 * Loads and saves the chore list to a fixed file on disk, so chores survive
 * between runs of Ducky. The save path is relative to the working directory
 * the JVM is launched from (the project root, when run normally).
 * Lines in the save file that cannot be read (e.g. after the file was edited
 * by hand) are skipped rather than stopping Ducky from starting; the number
 * skipped is available from {@link #getSkippedLineCount()}.
 */
public class Storage {
    /** Relative path, from the project root, of the save file. */
    private static final Path DATA_FILE_PATH = Paths.get(".", "data", "ducky.txt");

    /** Matches one saved line: "[typeIcon][statusIcon] description". */
    private static final Pattern LINE_PATTERN = Pattern.compile("^\\[(.)\\]\\[(.)\\] (.*)$");

    /** Matches a deadline's rendered description: "description (by: when)". */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*) \\(by: (.*)\\)$");

    /** Matches an event's rendered description: "description (from: start to: end)". */
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.*) \\(from: (.*) to: (.*)\\)$");

    /**
     * Date format used by older versions of Ducky, e.g. "Oct 15 2019", kept
     * only so their save files still load. The locale is fixed to English so
     * month names are read the same on every machine.
     */
    private static final DateTimeFormatter LEGACY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /** Number of unreadable save-file lines skipped by the most recent {@link #load()}. */
    private int skippedLineCount = 0;

    /**
     * Loads the chores previously saved to disk.
     * If the save file does not exist yet (e.g. on the very first run),
     * an empty list is returned instead of failing. Any line that cannot be
     * read is skipped, so one broken line does not lose every other chore.
     *
     * @return The chores read from disk, or an empty list if none are saved.
     * @throws IOException If the save file exists but cannot be read.
     */
    public List<Chore> load() throws IOException {
        List<Chore> chores = new ArrayList<>();
        skippedLineCount = 0;
        if (!Files.exists(DATA_FILE_PATH)) {
            return chores;
        }
        for (String line : Files.readAllLines(DATA_FILE_PATH)) {
            try {
                chores.add(parseChore(line));
            } catch (IllegalArgumentException | DateTimeParseException e) {
                skippedLineCount++;
            }
        }
        return chores;
    }

    /**
     * Returns how many unreadable lines the most recent {@link #load()} skipped.
     *
     * @return The number of skipped lines (0 if every line was read).
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Saves every chore in the given list to disk, overwriting any previous
     * save file. The data directory is created first if it does not already
     * exist.
     *
     * @param chores The chore list to save.
     * @throws IOException If the data directory or save file cannot be written.
     */
    public void save(ChoreList chores) throws IOException {
        Files.createDirectories(DATA_FILE_PATH.getParent());
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < chores.size(); i++) {
            lines.add(formatChore(chores.get(i)));
        }
        Files.write(DATA_FILE_PATH, lines);
    }

    /**
     * Renders a chore the same way it appears in "list", minus the rank,
     * since the rank is just the chore's position and need not be stored.
     *
     * @param chore The chore to render.
     * @return The chore rendered as one save-file line.
     */
    private String formatChore(Chore chore) {
        return chore.toString();
    }

    /**
     * Parses one save-file line, previously written by {@link #formatChore},
     * back into a chore.
     *
     * @param line One save-file line.
     * @return The chore the line represents.
     * @throws IllegalArgumentException If the line is not in the expected format.
     * @throws DateTimeParseException If a deadline's saved date cannot be read.
     */
    private Chore parseChore(String line) {
        Matcher lineMatcher = matchOrThrow(LINE_PATTERN, line);
        String typeIcon = lineMatcher.group(1);
        boolean isDone = lineMatcher.group(2).equals("X");
        String description = lineMatcher.group(3);

        Chore chore = buildChore(typeIcon, description);
        if (isDone) {
            chore.markAsDone();
        }
        return chore;
    }

    /**
     * Builds the right chore subtype from its type icon and rendered
     * description, undoing the "(by: ...)" / "(from: ... to: ...)" suffix
     * that {@link Deadline} and {@link Event} add when rendering.
     *
     * @param typeIcon Single-letter type icon ("T", "D" or "E").
     * @param description The rendered description, as written by {@link #formatChore}.
     * @return The reconstructed chore.
     * @throws IllegalArgumentException If the type icon is unknown, or the description is not in
     *         the format its type expects.
     * @throws DateTimeParseException If a deadline's saved date cannot be read.
     */
    private Chore buildChore(String typeIcon, String description) {
        switch (typeIcon) {
        case "T":
            return new ToDo(description);
        case "D":
            Matcher deadlineMatcher = matchOrThrow(DEADLINE_PATTERN, description);
            LocalDate by = parseSavedDate(deadlineMatcher.group(2));
            return new Deadline(deadlineMatcher.group(1), by);
        case "E":
            Matcher eventMatcher = matchOrThrow(EVENT_PATTERN, description);
            return new Event(eventMatcher.group(1), eventMatcher.group(2), eventMatcher.group(3));
        default:
            throw new IllegalArgumentException("unknown chore type \"" + typeIcon + "\"");
        }
    }

    /**
     * Matches the whole of the given text against a pattern, so that its
     * groups can be read.
     *
     * @param pattern The pattern the text should match.
     * @param text The text to match.
     * @return A matcher whose groups hold the matched parts of the text.
     * @throws IllegalArgumentException If the text does not match the pattern.
     */
    private Matcher matchOrThrow(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("unreadable saved text \"" + text + "\"");
        }
        return matcher;
    }

    /**
     * Parses a deadline's saved date. Dates are saved in
     * {@link Deadline#DISPLAY_DATE_FORMAT}, but save files written by older
     * versions of Ducky use {@link Deadline#INPUT_DATE_FORMAT} or
     * {@link #LEGACY_DATE_FORMAT}, so those are tried as fallbacks.
     *
     * @param dateText The saved date text.
     * @return The date the text describes.
     * @throws DateTimeParseException If the text matches none of the formats.
     */
    private LocalDate parseSavedDate(String dateText) {
        try {
            return LocalDate.parse(dateText, Deadline.DISPLAY_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            // Not the current format; fall through to the older ones.
        }
        try {
            return LocalDate.parse(dateText, Deadline.INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dateText, LEGACY_DATE_FORMAT);
        }
    }
}

package glados;

import glados.task.Deadline;
import glados.task.Event;
import glados.task.Task;
import glados.task.TaskDateTime;
import glados.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the task list to a text file on disk, one task per line, and loads it back.
 *
 * <p>Each line uses the format produced by {@link Task#toFileString()},
 * e.g. "D | 0 | return book | 2019-10-15".
 */
public class Storage {
    /** Invisible mark some editors put at the start of a UTF-8 file. */
    private static final String BYTE_ORDER_MARK = "﻿";

    /** Character that stands in for any bytes in the file that are not valid UTF-8. */
    private static final char UNREADABLE_CHARACTER = '�';

    /** Location of the data file, relative to the folder the program runs in. */
    private final Path filePath;

    /** How many lines the last load skipped because they were not in the expected format. */
    private int corruptedLineCount = 0;

    /**
     * Creates a storage that reads and writes the given file.
     *
     * @param filePath location of the data file, relative to the folder the program runs in.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the tasks saved in the data file.
     *
     * <p>If the file does not exist yet (e.g. the first time the program runs),
     * an empty list is returned. Blank lines are ignored, and lines not in the
     * expected format, including lines with characters that are not valid UTF-8,
     * are skipped and counted, see {@link #getCorruptedLineCount()}.
     *
     * @return the loaded tasks, in file order.
     * @throws GLaDOSException if the file exists but could not be read.
     */
    public ArrayList<Task> load() throws GLaDOSException {
        corruptedLineCount = 0;
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        String content;
        try {
            // Decoding the bytes here, instead of using Files.readAllLines, means a byte that is
            // not valid UTF-8 (e.g. from a file saved in another encoding) only damages its own
            // line, which is then skipped, rather than making the whole file unreadable.
            content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't read your saved tasks from " + filePath + ".");
        }

        // Some editors, e.g. Notepad's "UTF-8 with BOM", put an invisible byte order mark
        // at the start of the file. It is not part of the first task, so it is removed.
        if (content.startsWith(BYTE_ORDER_MARK)) {
            content = content.substring(BYTE_ORDER_MARK.length());
        }
        List<String> lines = content.lines().toList();

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (GLaDOSException e) {
                corruptedLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Returns how many lines the last call to {@link #load()} skipped as corrupted.
     */
    public int getCorruptedLineCount() {
        return corruptedLineCount;
    }

    /**
     * Turns one data file line, e.g. "D | 1 | return book | 2019-10-15", back into a task.
     *
     * @throws GLaDOSException if the line is not in the expected format.
     */
    private static Task parseTask(String line) throws GLaDOSException {
        if (line.indexOf(UNREADABLE_CHARACTER) != -1) {
            throw new GLaDOSException("Unreadable characters in line: " + line);
        }

        // split takes a regular expression, where | has a special meaning, so it is escaped.
        // The -1 keeps empty trailing fields, so "T | 0 | " is caught as having an empty description.
        String[] parts = line.split(" \\| ", -1);
        for (String part : parts) {
            if (part.isBlank()) {
                throw new GLaDOSException("Empty field in line: " + line);
            }
        }
        if (parts.length < 3) {
            throw new GLaDOSException("Too few fields in line: " + line);
        }

        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];

        Task task;
        if (type.equals("T") && parts.length == 3) {
            task = new Todo(description);
        } else if (type.equals("D") && parts.length == 4) {
            task = new Deadline(description, parseDateTime(parts[3], line));
        } else if (type.equals("E") && parts.length == 5) {
            TaskDateTime from = parseDateTime(parts[3], line);
            TaskDateTime to = parseDateTime(parts[4], line);
            if (to.isBefore(from)) {
                throw new GLaDOSException("Event ends before it starts in line: " + line);
            }
            task = new Event(description, from, to);
        } else {
            throw new GLaDOSException("Unknown type or wrong number of fields in line: " + line);
        }

        if (doneFlag.equals("1")) {
            task.markAsDone();
        } else if (!doneFlag.equals("0")) {
            throw new GLaDOSException("Done flag is not 0 or 1 in line: " + line);
        }
        return task;
    }

    /**
     * Turns a date field of a data file line, e.g. "2019-12-02 1800", back into a date.
     *
     * @param text the date field.
     * @param line the whole line, used in the error message.
     * @throws GLaDOSException if the field is not a valid date in the data file's format.
     */
    private static TaskDateTime parseDateTime(String text, String line) throws GLaDOSException {
        try {
            return TaskDateTime.fromFileString(text);
        } catch (DateTimeParseException e) {
            throw new GLaDOSException("Invalid date in line: " + line);
        }
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * <p>Creates the file's folder first if it does not exist yet.
     *
     * @param tasks the task list.
     * @throws GLaDOSException if the file could not be written.
     */
    public void save(TaskList tasks) throws GLaDOSException {
        ArrayList<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(tasks.get(i).toFileString());
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't save your tasks to " + filePath + ".");
        }
    }
}

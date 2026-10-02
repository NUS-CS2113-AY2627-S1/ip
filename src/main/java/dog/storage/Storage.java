package dog.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import dog.task.Deadline;
import dog.task.Event;
import dog.task.Task;
import dog.task.Todo;

/**
 * Loads and saves tasks using a text file on the local file system.
 */
public class Storage {
    private final Path filePath;
    private int skippedLineCount;

    /**
     * Creates storage that reads from and writes to the given path.
     *
     * @param filePath Relative path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
        this.skippedLineCount = 0;
    }

    /**
     * Loads valid tasks and skips corrupted lines.
     *
     * @return Tasks loaded from the data file.
     * @throws IOException If the existing file cannot be read.
     */
    public ArrayList<Task> loadTasks() throws IOException {
        skippedLineCount = 0;
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
            try {
                tasks.add(parseTask(line));
            } catch (IllegalArgumentException e) {
                skippedLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Saves the current tasks, creating the parent directory when needed.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Returns the number of corrupted records skipped by the latest load.
     *
     * @return Skipped record count, or zero before the first load.
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    /**
     * Decodes one saved record and restores its task type and completion status.
     *
     * @param line Pipe-delimited record containing Base64-encoded text.
     * @return Restored task.
     * @throws IllegalArgumentException If the type, status, field count, or encoding is invalid.
     */
    private Task parseTask(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length < 3) {
            throw new IllegalArgumentException("Missing task fields");
        }

        Task task;
        String description = decode(fields[2]);
        switch (fields[0]) {
        case "T":
            requireFieldCount(fields, 3);
            task = new Todo(description);
            break;
        case "D":
            requireFieldCount(fields, 4);
            task = new Deadline(description, decode(fields[3]));
            break;
        case "E":
            requireFieldCount(fields, 5);
            task = new Event(description, decode(fields[3]), decode(fields[4]));
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw new IllegalArgumentException("Invalid task status");
        }
        return task;
    }

    /**
     * Serializes a supported task with encoded text and a numeric completion status.
     *
     * @param task Todo, deadline, or event to serialize.
     * @return Pipe-delimited record suitable for saving.
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        String commonFields = status + "|" + encode(task.getDescription());
        if (task instanceof Deadline deadline) {
            return "D|" + commonFields + "|" + encode(deadline.getBy());
        }
        if (task instanceof Event event) {
            return "E|" + commonFields + "|" + encode(event.getFrom()) + "|" + encode(event.getTo());
        }
        return "T|" + commonFields;
    }

    /**
     * Rejects records whose field count differs from their task type's format.
     *
     * @param fields Fields extracted from a saved record.
     * @param expectedCount Required number of fields.
     * @throws IllegalArgumentException If the field count is incorrect.
     */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Unexpected task fields");
        }
    }

    /**
     * Encodes UTF-8 text so delimiters and line breaks cannot corrupt a record.
     *
     * @param value Text to encode.
     * @return Base64 representation of the text.
     */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes a saved Base64 field into UTF-8 text.
     *
     * @param value Encoded field.
     * @return Decoded text.
     * @throws IllegalArgumentException If the field is not valid Base64.
     */
    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}

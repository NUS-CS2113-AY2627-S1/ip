package chatbot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import chatbot.task.Deadline;
import chatbot.task.Event;
import chatbot.task.Task;
import chatbot.task.Todo;

/** Loads tasks from disk and saves tasks to disk. */
public class Storage {
    private final Path saveFile;

    /** Creates storage backed by the given file.
     *
     * @param filePath path to the task save file
     */
    public Storage(String filePath) {
        saveFile = Path.of(filePath);
    }

    /** Saves the current tasks to disk.
     *
     * @param tasks tasks to save
     */
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(saveFile.getParent());
            List<String> lines = tasks.stream().map(this::formatTask).toList();
            Files.write(saveFile, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("Warning: Unable to save tasks to " + saveFile + ".");
        }
    }

    /** Loads usable tasks from disk, ignoring missing or malformed records.
     *
     * @return loaded tasks, or an empty list when the file cannot be read
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(saveFile)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(saveFile, StandardCharsets.UTF_8)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            return tasks;
        }
        return tasks;
    }

    /** Converts one saved line into a task.
     *
     * @param line saved task line
     * @return parsed task, or null for malformed data
     */
    private Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] fields = splitStorageLine(line);
        if (fields.length < 3 || fields[2].isBlank()
                || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            return null;
        }
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new Todo(fields[2]);
        } else if (fields[0].equals("D") && fields.length == 4 && !fields[3].isBlank()) {
            task = new Deadline(fields[2], fields[3]);
        } else if (fields[0].equals("E") && fields.length == 5
                && !fields[3].isBlank() && !fields[4].isBlank()) {
            task = new Event(fields[2], fields[3], fields[4]);
        } else {
            return null;
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Splits a storage line while allowing escaped pipes and backslashes. */
    private String[] splitStorageLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;
        for (char character : line.toCharArray()) {
            if (isEscaped) {
                field.append(character);
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaped) {
            field.append('\\');
        }
        fields.add(field.toString().trim());
        return fields.toArray(String[]::new);
    }

    /** Formats a task as one pipe-delimited line for the save file. */
    private String formatTask(Task task) {
        StringBuilder line = new StringBuilder(task.getTaskType())
                .append(" | ").append(task.isDone() ? "1" : "0")
                .append(" | ").append(escapeStorageValue(task.getDescription()));
        if (task instanceof Deadline deadline) {
            line.append(" | ").append(escapeStorageValue(deadline.getBy()));
        } else if (task instanceof Event event) {
            line.append(" | ").append(escapeStorageValue(event.getFrom()))
                    .append(" | ").append(escapeStorageValue(event.getTo()));
        }
        return line.toString();
    }

    /** Escapes characters that have meaning in the storage format. */
    private String escapeStorageValue(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }
}

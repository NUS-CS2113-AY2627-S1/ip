package cracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves tasks to and loads tasks from the chatbot's local data file.
 */
public class Storage {
    /** Relative location of the chatbot data file. */
    private static final Path DATA_FILE = Path.of("data", "duke.txt");

    /** Field separator used in the data file. */
    private static final String FIELD_SEPARATOR = " | ";

    /**
     * Loads valid tasks from the data file into the supplied task list.
     *
     * @param tasks list that receives the loaded tasks
     */
    public void loadTasks(List<Task> tasks) {
        if (!Files.exists(DATA_FILE)) {
            return;
        }

        boolean hasCorruptedData = false;
        try {
            for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
                try {
                    Task task = parseTask(line);
                    tasks.add(task);
                } catch (IllegalArgumentException e) {
                    hasCorruptedData = true;
                }
            }
        } catch (IOException e) {
            System.out.println(" Unable to load saved tasks. Starting with an empty list.");
            return;
        }

        if (hasCorruptedData) {
            System.out.println(" Some saved tasks were corrupted and have been ignored.");
        }
    }

    /**
     * Saves the supplied tasks to the data file, creating its parent folder when needed.
     *
     * @param tasks tasks to save
     */
    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }

        try {
            Path parentDirectory = DATA_FILE.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(" Unable to save tasks to disk.");
        }
    }

    /**
     * Converts a task to one line in the data file.
     *
     * @param task task to convert
     * @return serialized task line
     */
    private String formatTask(Task task) {
        String status = task.getStatusIcon().equals("X") ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + escape(task.getDescription());
        }
        if (task instanceof Deadline deadline) {
            return "D" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + escape(task.getDescription())
                    + FIELD_SEPARATOR + escape(deadline.getBy());
        }
        if (task instanceof Event event) {
            return "E" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + escape(task.getDescription())
                    + FIELD_SEPARATOR + escape(event.getFrom()) + FIELD_SEPARATOR + escape(event.getTo());
        }
        throw new IllegalArgumentException("Unsupported task type");
    }

    /**
     * Converts one data-file line to a task.
     *
     * @param line serialized task line
     * @return reconstructed task
     * @throws IllegalArgumentException if the line is not a valid serialized task
     */
    private Task parseTask(String line) {
        List<String> fields = splitFields(line);
        if (fields.size() < 3 || !fields.get(1).matches("[01]")) {
            throw new IllegalArgumentException("Invalid task format");
        }

        Task task;
        switch (fields.get(0)) {
        case "T":
            requireFieldCount(fields, 3);
            task = new Todo(fields.get(2));
            break;
        case "D":
            requireFieldCount(fields, 4);
            task = new Deadline(fields.get(2), fields.get(3));
            break;
        case "E":
            requireFieldCount(fields, 5);
            task = new Event(fields.get(2), fields.get(3), fields.get(4));
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if (fields.get(1).equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a serialized line while preserving escaped separators.
     *
     * @param line serialized task line
     * @return unescaped fields from the line
     */
    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;
        for (int i = 0; i < line.length(); i++) {
            char currentCharacter = line.charAt(i);
            if (isEscaped) {
                if (currentCharacter != '|' && currentCharacter != '\\') {
                    throw new IllegalArgumentException("Invalid escape sequence");
                }
                field.append(currentCharacter);
                isEscaped = false;
            } else if (currentCharacter == '\\') {
                isEscaped = true;
            } else if (line.startsWith(FIELD_SEPARATOR, i)) {
                fields.add(field.toString());
                field.setLength(0);
                i += FIELD_SEPARATOR.length() - 1;
            } else {
                field.append(currentCharacter);
            }
        }
        if (isEscaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        fields.add(field.toString());
        return fields;
    }

    /**
     * Throws an exception when a serialized task does not contain the expected fields.
     *
     * @param fields fields parsed from a serialized task
     * @param expectedCount expected number of fields
     */
    private void requireFieldCount(List<String> fields, int expectedCount) {
        if (fields.size() != expectedCount) {
            throw new IllegalArgumentException("Incorrect field count");
        }
    }

    /**
     * Escapes reserved field characters in a task attribute.
     *
     * @param text task attribute text
     * @return escaped task attribute text
     */
    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("|", "\\|");
    }
}

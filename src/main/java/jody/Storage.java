package jody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Reads and writes tasks without printing user-interface messages. */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage using the supplied task-file path.
     *
     * @param filePath the path used to load and save tasks
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads UTF-8 task records, skipping blank lines and an initial byte-order mark.
     * Returns an empty list when the file or its parent directory does not exist.
     *
     * @return the tasks in their stored order
     * @throws JodyException if a record is invalid or the file cannot be read
     */
    public ArrayList<Task> load() throws JodyException {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (i == 0 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (line.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(parseRecord(line));
                } catch (JodyException e) {
                    throw new JodyException("Invalid saved task at line " + (i + 1)
                            + " in " + filePath + ": " + e.getMessage()
                            + " Fix that line before restarting Jody.", e);
                }
            }
            return tasks;
        } catch (NoSuchFileException e) {
            // A missing file or parent folder is normal on the first run.
            return tasks;
        } catch (IOException | SecurityException e) {
            throw new JodyException("Could not load tasks from " + filePath
                    + ". Check that the file can be read. " + e.getMessage(), e);
        }
    }

    /**
     * Saves tasks as UTF-8 records through a temporary file in the same directory.
     * Creates missing parent directories and attempts an atomic replacement,
     * falling back to a regular replacement when atomic moves are unsupported.
     *
     * @param tasks the tasks to save, in their desired order
     * @throws JodyException if a task type is unsupported or writing fails
     */
    public void save(List<Task> tasks) throws JodyException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(toRecord(task));
        }

        Path parent = filePath.getParent();
        if (parent == null) {
            parent = Path.of(".");
        }

        Path temporaryFile = null;
        try {
            Files.createDirectories(parent);
            temporaryFile = Files.createTempFile(parent, "jody-", ".tmp");
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, filePath,
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException e) {
            throw new JodyException("Could not save tasks to " + filePath
                    + ". Your latest changes are still in this session but have not been saved. "
                    + e.getMessage(), e);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException e) {
                    // Cleanup is best effort; it must not hide the original save error.
                }
            }
        }
    }

    /**
     * Serializes a supported task into a pipe-separated record with escaped fields.
     *
     * @param task the todo, deadline, or event to serialize
     * @return the task record
     * @throws JodyException if the task type is unsupported
     */
    private static String toRecord(Task task) throws JodyException {
        String commonFields = " | " + (task.isDone() ? "1" : "0")
                + " | " + encode(task.getDescription());
        return switch (task) {
            case Todo todo -> "T" + commonFields;
            case Deadline deadline -> "D" + commonFields + " | " + encode(String.valueOf(deadline.getBy()));
            case Event event -> "E" + commonFields + " | " + encode(String.valueOf(event.getFrom()))
                    + " | " + encode(String.valueOf(event.getTo()));
            default -> throw new JodyException("Cannot save an unsupported task type.");
        };
    }

    /**
     * Decodes and validates a stored record and restores its completion status.
     * Also accepts legacy {@code by:}, {@code from:}, and {@code to:} date prefixes.
     *
     * @param line the pipe-separated task record
     * @return the restored task
     * @throws JodyException if the fields, escapes, type, or dates are invalid
     */
    private static Task parseRecord(String line) throws JodyException {
        String[] fields = line.split("\\|", -1);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = decode(fields[i].trim());
        }
        if (fields.length < 3) {
            throw new JodyException("A task needs a type, completion status and description.");
        }
        if (!fields[1].equals("0") && !fields[1].equals("1")) {
            throw new JodyException("The completion status must be 0 or 1.");
        }
        if (fields[2].isBlank()) {
            throw new JodyException("The task description cannot be empty.");
        }

        Task task;
        switch (fields[0]) {
            case "T":
                requireFieldCount(fields, 3);
                task = new Todo(fields[2]);
                break;
            case "D":
                requireFieldCount(fields, 4);
                task = new Deadline(fields[2], fields[3].replaceFirst("(?i)^by: *", ""));
                break;
            case "E":
                requireFieldCount(fields, 5);
                task = new Event(fields[2], fields[3].replaceFirst("(?i)^from: *", ""),
                        fields[4].replaceFirst("(?i)^to: *", ""));
                break;
            default:
                throw new JodyException("The task type must be T, D or E.");
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Checks that a task record has the required number of fields.
     *
     * @param fields the decoded fields, including the task type at index zero
     * @param expected the required field count
     * @throws JodyException if the actual count differs from the expected count
     */
    private static void requireFieldCount(String[] fields, int expected) throws JodyException {
        if (fields.length != expected) {
            throw new JodyException("Task type " + fields[0] + " needs " + expected + " fields.");
        }
    }

    // Escape reserved characters so a pipe in a description is not a separator.
    /**
     * Escapes backslashes, pipes, and line breaks for storage in one record field.
     *
     * @param value the unescaped field value
     * @return the escaped field value
     */
    private static String encode(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\p")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    /**
     * Restores escaped backslashes, pipes, and line breaks in a record field.
     *
     * @param value the escaped field value
     * @return the decoded field value
     * @throws JodyException if an escape sequence is incomplete or unrecognized
     */
    private static String decode(String value) throws JodyException {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current != '\\') {
                decoded.append(current);
                continue;
            }
            i++;
            if (i >= value.length()) {
                throw new JodyException("An escape sequence is incomplete.");
            }
            switch (value.charAt(i)) {
                case '\\':
                    decoded.append('\\');
                    break;
                case 'p':
                    decoded.append('|');
                    break;
                case 'n':
                    decoded.append('\n');
                    break;
                case 'r':
                    decoded.append('\r');
                    break;
                default:
                    throw new JodyException("An escape sequence is not recognised.");
            }
        }
        return decoded.toString();
    }
}


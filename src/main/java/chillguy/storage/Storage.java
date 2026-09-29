package chillguy.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import chillguy.exception.ChillguyException;
import chillguy.task.Deadline;
import chillguy.task.Event;
import chillguy.task.Task;
import chillguy.task.Todo;

/**
 * Reads and replaces a UTF-8 task file without exposing partially written task lists.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage at the supplied path, relative to the working directory if not absolute.
     *
     * @param filePath Location of the task file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks in file order, treating a missing file or folder as an empty list.
     * Rejects the whole file if any nonblank line is invalid, so no tasks are silently lost.
     *
     * @return All saved tasks, including their completion status.
     * @throws ChillguyException If existing data cannot be read or contains an invalid record.
     */
    public ArrayList<Task> load() throws ChillguyException {
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (NoSuchFileException exception) {
            if (!Files.notExists(filePath, LinkOption.NOFOLLOW_LINKS) || !hasDirectoryParent()) {
                throw createLoadException(exception);
            }
            return new ArrayList<>();
        } catch (IOException exception) {
            throw createLoadException(exception);
        }

        ArrayList<Task> loadedTasks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // Some text editors add a UTF-8 byte order mark to the first line.
            if (i == 0 && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }
            if (line.isBlank()) {
                continue;
            }
            try {
                loadedTasks.add(decodeTask(line));
            } catch (IllegalArgumentException exception) {
                throw new ChillguyException("Sorry, " + getDisplayPath()
                        + " has invalid task data on line " + (i + 1)
                        + ". Fix the file and restart Chillguy.", exception);
            }
        }
        return loadedTasks;
    }

    /**
     * Distinguishes a missing directory from a file blocking one of the parent paths.
     * Windows can report both situations as a missing task file.
     */
    private boolean hasDirectoryParent() {
        Path parent = filePath.toAbsolutePath().getParent();
        while (parent != null && Files.notExists(parent, LinkOption.NOFOLLOW_LINKS)) {
            parent = parent.getParent();
        }
        return parent == null || Files.isDirectory(parent);
    }

    /**
     * Wraps a read failure with the task file's location and a recovery hint for the user.
     */
    private ChillguyException createLoadException(IOException cause) {
        return new ChillguyException("Sorry, I couldn't load tasks from " + getDisplayPath()
                + ". Check that the path is a readable UTF-8 file.", cause);
    }

    /**
     * Saves the full list through a temporary file in the same folder, creating the folder if needed.
     * Requires atomic replacement: a failed write must not truncate the previous saved list.
     *
     * @param tasks Tasks to save in their current order.
     * @throws ChillguyException If writing or safely replacing the task file fails.
     */
    public void save(List<Task> tasks) throws ChillguyException {
        Path temporaryFile = null;
        try {
            Path parent = filePath.toAbsolutePath().getParent();
            Files.createDirectories(parent);
            temporaryFile = Files.createTempFile(parent, "chillguy-", ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(encodeTask(task));
                    writer.newLine();
                }
            }
            Files.move(temporaryFile, filePath, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            throw new ChillguyException("Sorry, I couldn't save tasks to " + getDisplayPath()
                    + ". No changes were made. This file system does not support atomic file replacement.", exception);
        } catch (IOException exception) {
            throw new ChillguyException("Sorry, I couldn't save tasks to " + getDisplayPath()
                    + ". No changes were made. Check that the data folder is writable and the file is not in use.",
                    exception);
        } finally {
            deleteTemporaryFile(temporaryFile);
        }
    }

    /**
     * Returns the task file path with forward slashes for consistent console messages.
     */
    private String getDisplayPath() {
        return filePath.toString().replace('\\', '/');
    }

    /**
     * Cleans up an incomplete save without hiding the original error from the user.
     */
    private void deleteTemporaryFile(Path temporaryFile) {
        if (temporaryFile == null) {
            return;
        }
        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException exception) {
            // A leftover temporary file is never loaded as task data.
        }
    }

    /**
     * Serializes a task's type, completion status, and escaped fields into one storage record.
     */
    private String encodeTask(Task task) {
        List<String> fields = new ArrayList<>();
        fields.add(task.getTaskTypeIcon());
        fields.add(task.isDone() ? "1" : "0");
        fields.add(escapeField(task.getDescription()));
        if (task instanceof Deadline deadline) {
            fields.add(escapeField(deadline.getBy()));
        } else if (task instanceof Event event) {
            fields.add(escapeField(event.getFrom()));
            fields.add(escapeField(event.getTo()));
        }
        return String.join(" | ", fields);
    }

    /**
     * Escapes backslashes, pipes, and line breaks so a field fits within one pipe-separated record.
     */
    private String escapeField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    /**
     * Reconstructs a task and its completion status from a validated storage record.
     *
     * @throws IllegalArgumentException If the record has invalid escapes, fields, type, or status.
     */
    private Task decodeTask(String line) {
        List<String> fields = splitFields(line);
        if (fields.size() < 3 || !(fields.get(1).equals("0") || fields.get(1).equals("1"))) {
            throw new IllegalArgumentException("Invalid task status or missing fields");
        }
        for (String field : fields) {
            if (field.isBlank()) {
                throw new IllegalArgumentException("Empty task field");
            }
        }

        int expectedFields = switch (fields.getFirst()) {
            case "T" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields.size() != expectedFields) {
            throw new IllegalArgumentException("Incorrect number of task fields");
        }
        Task task = switch (fields.getFirst()) {
            case "T" -> new Todo(fields.get(2));
            case "D" -> new Deadline(fields.get(2), fields.get(3));
            case "E" -> new Event(fields.get(2), fields.get(3), fields.get(4));
            default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields.get(1).equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits pipe-separated fields while decoding escaped pipes, backslashes, and line breaks.
     */
    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (isEscaped) {
                field.append(switch (character) {
                    case '\\', '|' -> character;
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    default -> throw new IllegalArgumentException("Invalid escape sequence");
                });
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(field.toString().strip());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        fields.add(field.toString().strip());
        return fields;
    }
}

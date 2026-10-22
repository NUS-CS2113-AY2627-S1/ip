package dawn.storage;

import dawn.exception.DawnException;
import dawn.exception.MalformedRecordException;
import dawn.exception.StorageException;
import dawn.parser.DateTimeParser;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.Task;
import dawn.task.TaskDateTime;
import dawn.task.TaskList;
import dawn.task.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading from and writing to the local data storage file.
 */
public class Storage {
    private final Path filePath;
    /**
     * Warnings from the latest load, displayed by the UI after loading completes.
     */
    private final List<String> loadWarnings = new ArrayList<>();

    /**
     * Constructs a Storage instance tied to a specific file path string.
     *
     * @param filePath string path to the persistence file (e.g. "data/dawn.txt").
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Saves the current list of tasks to a temporary file, then replaces the
     * persistent file atomically. Creates missing parent directories.
     *
     * @param taskList the list of tasks to persist.
     * @throws DawnException if serialization or an I/O operation fails.
     */
    public void save(TaskList taskList) throws DawnException {
        Path temporaryFile = null;
        try {
            Path destination = filePath.toAbsolutePath();
            Path directory = destination.getParent();
            Files.createDirectories(directory);

            List<String> lines = new ArrayList<>();
            for (int i = 0; i < taskList.size(); i++) {
                lines.add(serializeTask(taskList.getTask(i)));
            }

            // Stage the complete file beside the destination before replacing it.
            temporaryFile = Files.createTempFile(directory, "dawn-", ".tmp");
            Files.write(temporaryFile, lines, java.nio.charset.StandardCharsets.UTF_8);
            replaceSavedFile(temporaryFile, destination);
        } catch (IOException e) {
            throw new StorageException("I couldn't save that change. Your list is unchanged. Details: "
                    + e.getMessage());
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // A failed cleanup must not conceal the save failure.
                }
            }
        }
    }

    /**
     * Loads tasks from the persistent text file.
     *
     * @return a populated TaskList based on the saved data.
     * @throws DawnException if a read failure occurs.
     */
    public TaskList load() throws DawnException {
        loadWarnings.clear();
        TaskList taskList = new TaskList();

        if (Files.notExists(filePath)) {
            return taskList;
        }

        try {
            List<String> lines = Files.readAllLines(filePath, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                // Strip UTF-8 BOM if present at the start of the file
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseLineToTask(line);
                    taskList.addTask(task);
                } catch (MalformedRecordException | DawnException e) {
                    // Report malformed records, invalid dates, and capacity limits; let programming errors surface.
                    loadWarnings.add("I skipped a damaged saved task: [" + line + "] - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new StorageException("I couldn't load your saved tasks. I'll start with an empty list. Details: "
                    + e.getMessage());
        }

        return taskList;
    }

    /**
     * Returns a snapshot of warnings without allowing callers to change them.
     */
    public List<String> getLoadWarnings() {
        return List.copyOf(loadWarnings);
    }

    /**
     * Reads either a legacy record or an escaped versioned record.
     */
    private Task parseLineToTask(String line) throws MalformedRecordException, DawnException {
        if (line.startsWith("V2 | ")) {
            return parseVersionedLine(line);
        }

        // Example format: T | 1 | read book
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new MalformedRecordException("Missing essential task components.");
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        switch (type) {
            case "T":
                return new ToDo(description, isDone);
            case "D":
                if (parts.length < 4) {
                    throw new MalformedRecordException("Deadline is missing the due date.");
                }
                TaskDateTime dueDate = DateTimeParser.parseFlexible(parts[3].trim());
                return new Deadline(description, dueDate, isDone);
            case "E":
                if (parts.length < 5) {
                    throw new MalformedRecordException("Event is missing start or end dates.");
                }
                TaskDateTime startDate = DateTimeParser.parseFlexible(parts[3].trim());
                TaskDateTime endDate = DateTimeParser.parseFlexible(parts[4].trim());
                return new Event(description, startDate, endDate, isDone);
            default:
                throw new MalformedRecordException("Unknown task type identifier: " + type);
        }
    }

    /**
     * Replaces a complete save file atomically; overridable to simulate a failed move in tests.
     */
    protected void replaceSavedFile(Path temporaryFile, Path destination) throws IOException {
        Files.move(temporaryFile, destination, StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Writes a versioned record so escaped fields cannot be confused with old data.
     */
    private String serializeTask(Task task) throws DawnException {
        String prefix = "V2 | ";
        String status = task.isDone() ? "1" : "0";
        String description = escapeField(task.getDescription());
        if (task instanceof Deadline deadline) {
            return prefix + "D | " + status + " | " + description + " | "
                    + escapeField(deadline.getDueDate().toStorageString());
        }
        if (task instanceof Event event) {
            return prefix + "E | " + status + " | " + description + " | "
                    + escapeField(event.getStartDate().toStorageString()) + " | "
                    + escapeField(event.getEndDate().toStorageString());
        }
        if (task instanceof ToDo) {
            return prefix + "T | " + status + " | " + description;
        }
        throw new StorageException("I couldn't save that change. Your list is unchanged. Unsupported task type: "
                + task.getClass().getSimpleName());
    }

    /**
     * Escapes field separators, backslashes, and line breaks in new records.
     */
    private static String escapeField(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    /**
     * Reads only versioned records using the matching escape rules.
     */
    private Task parseVersionedLine(String line) throws MalformedRecordException, DawnException {
        List<String> fields = splitEscapedFields(line);
        if (fields.size() < 4 || !fields.get(0).equals("V2")) {
            throw new MalformedRecordException("Missing essential task components.");
        }
        String type = fields.get(1);
        String status = fields.get(2);
        if (!status.equals("0") && !status.equals("1")) {
            throw new MalformedRecordException("Invalid task status.");
        }
        boolean isDone = status.equals("1");
        String description = fields.get(3);
        switch (type) {
            case "T":
                requireFieldCount(fields, 4);
                return new ToDo(description, isDone);
            case "D":
                requireFieldCount(fields, 5);
                return new Deadline(description, DateTimeParser.parseFlexible(fields.get(4)), isDone);
            case "E":
                requireFieldCount(fields, 6);
                return new Event(description, DateTimeParser.parseFlexible(fields.get(4)),
                        DateTimeParser.parseFlexible(fields.get(5)), isDone);
            default:
                throw new MalformedRecordException("Unknown task type identifier: " + type);
        }
    }

    /**
     * Rejects missing or surplus fields instead of silently changing saved task text.
     */
    private static void requireFieldCount(List<String> fields, int expected) throws MalformedRecordException {
        if (fields.size() != expected) {
            throw new MalformedRecordException("Expected " + expected + " fields, found " + fields.size() + ".");
        }
    }

    /**
     * Splits on literal separators while decoding escaped characters within fields.
     */
    private static List<String> splitEscapedFields(String line) throws MalformedRecordException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (current == '\\') {
                if (++i == line.length()) {
                    throw new MalformedRecordException("Incomplete escape sequence.");
                }
                char escaped = line.charAt(i);
                switch (escaped) {
                    case '\\', '|':
                        field.append(escaped);
                        break;
                    case 'r':
                        field.append('\r');
                        break;
                    case 'n':
                        field.append('\n');
                        break;
                    default:
                        throw new MalformedRecordException("Unknown escape sequence: \\" + escaped);
                }
            } else if (line.startsWith(" | ", i)) {
                fields.add(field.toString());
                field.setLength(0);
                i += 2;
            } else {
                field.append(current);
            }
        }
        fields.add(field.toString());
        return fields;
    }
}

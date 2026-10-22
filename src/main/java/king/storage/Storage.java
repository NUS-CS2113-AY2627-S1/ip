package king.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import king.exception.KingException;
import king.task.Deadline;
import king.task.Event;
import king.task.Task;
import king.task.ToDo;

/**
 * Stores tasks as UTF-8 records with Base64 text fields so delimiters and Unicode round-trip safely.
 * Invalid records reject the entire load; saving replaces the file only after writing a complete snapshot.
 */
public class Storage {

    /** Destination for this chatbot's task records. */
    private final Path filePath;

    /**
     * Creates storage using the supplied path, resolved relative to the working directory if necessary.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns all saved tasks, or an empty list when the file or its parent folder does not exist.
     *
     * @return Loaded tasks, or an empty list if the file does not exist.
     * @throws KingException If the file cannot be read or any record is malformed.
     */
    public List<Task> load() throws KingException {
        List<String> records;
        try {
            records = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (NoSuchFileException exception) {
            return new ArrayList<>();
        } catch (IOException exception) {
            throw new KingException("cannot read " + filePath + ". Check the file and its permissions.");
        }

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            try {
                tasks.add(parseRecord(records.get(i)));
            } catch (IllegalArgumentException exception) {
                throw new KingException("invalid saved data at line " + (i + 1) + " in " + filePath
                        + ". Repair or move the file before restarting. The file has not been changed.");
            }
        }
        return tasks;
    }

    /**
     * Saves a complete snapshot, creating parent directories on first use.
     *
     * @param tasks Tasks to persist, in list order.
     * @throws KingException If the snapshot cannot be written or installed at the destination.
     */
    public void save(List<Task> tasks) throws KingException {
        List<String> records = new ArrayList<>();
        for (Task task : tasks) {
            records.add(formatRecord(task));
        }

        Path temporaryFile = null;
        try {
            Path destination = filePath.toAbsolutePath();
            Path parent = destination.getParent();
            Files.createDirectories(parent);
            temporaryFile = Files.createTempFile(parent, "king-", ".tmp");
            Files.write(temporaryFile, records, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, destination, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new KingException("cannot save to " + filePath
                    + ". Changes remain in memory only. Check the folder and permissions;"
                    + " the next task change will retry saving.");
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException exception) {
                    // A leftover temporary file must not hide the original save result.
                }
            }
        }
    }

    /**
     * Returns a record containing type, completion status, and encoded task fields.
     *
     * @param task Task to display or serialize.
     * @return Task record with encoded fields.
     */
    private static String formatRecord(Task task) {
        String record = task.getTaskType() + "|" + (task.isDone() ? "1" : "0")
                + "|" + encode(task.getRawDescription());
        if (task instanceof Deadline deadline) {
            return record + "|" + encode(deadline.getBy().toString());
        } else if (task instanceof Event event) {
            return record + "|" + encode(event.getFrom()) + "|" + encode(event.getTo());
        }
        return record;
    }

    /**
     * Returns a task from a validated record, preserving its completion state.
     *
     * @param record Serialized task record to validate and decode.
     * @return Reconstructed task.
     * @throws IllegalArgumentException If the type, status, field count, or text encoding is invalid.
     */
    private static Task parseRecord(String record) {
        String[] fields = record.split("\\|", -1);
        if (fields.length < 3 || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid status or field count");
        }
        String description = decode(fields[2]);
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new ToDo(description);
        } else if (fields[0].equals("D") && fields.length == 4) {
            task = new Deadline(description, decode(fields[3]));
        } else if (fields[0].equals("E") && fields.length == 5) {
            task = new Event(description, decode(fields[3]), decode(fields[4]));
        } else {
            throw new IllegalArgumentException("Invalid task type or field count");
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Encodes text as Base64 using UTF-8 so record separators remain unambiguous.
     *
     * @param text Text to process.
     * @return Base64 representation of the UTF-8 text.
     */
    private static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns nonblank text decoded from Base64 and strictly validated UTF-8.
     *
     * @param text Text to process.
     * @return Decoded nonblank text.
     */
    private static String decode(String text) {
        try {
            String decoded = StandardCharsets.UTF_8.newDecoder()
                    .decode(ByteBuffer.wrap(Base64.getDecoder().decode(text))).toString();
            if (decoded.isBlank()) {
                throw new IllegalArgumentException("Empty task field");
            }
            return decoded;
        } catch (CharacterCodingException exception) {
            throw new IllegalArgumentException("Invalid UTF-8", exception);
        }
    }
}

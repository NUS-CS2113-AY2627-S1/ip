package proton.storage;

import proton.task.Deadline;
import proton.task.Event;
import proton.task.Task;
import proton.task.Todo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads validated task records and replaces saves atomically to protect the previous file.
 */
public class Storage {
    private static final String HEADER = "PROTON 1";
    private final Path file;
    // Detect changes made outside this process before replacing the loaded file.
    private byte[] previousBytes;

    /**
     * Creates storage for the specified save file.
     */
    public Storage(Path file) {
        this.file = file;
    }

    /**
     * Loads the complete list, accepting an absent file and older display-format saves.
     *
     * @throws IOException If the path cannot be read or any record is invalid.
     */
    public ArrayList<Task> load() throws IOException {
        previousBytes = readBytes();
        ArrayList<Task> tasks = new ArrayList<>();
        if (previousBytes == null) {
            return tasks;
        }
        String text = decode(previousBytes);
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }
        List<String> lines = text.lines().toList();
        boolean isVersioned = !lines.isEmpty() && lines.getFirst().equals(HEADER);
        for (int i = isVersioned ? 1 : 0; i < lines.size(); i++) {
            try {
                tasks.add(isVersioned ? parseRecord(lines.get(i)) : parseLegacy(lines.get(i)));
            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid task on line " + (i + 1) + ".", exception);
            }
        }
        return tasks;
    }

    /**
     * Writes to a sibling temporary file before atomically replacing the previous save.
     *
     * @throws IOException If saving fails, the file changed externally, or atomic replacement is unavailable.
     */
    public void save(List<Task> tasks) throws IOException {
        if (!Arrays.equals(previousBytes, readBytes())) {
            throw new IOException("The save file changed outside Proton. Restart before editing.");
        }
        StringBuilder text = new StringBuilder(HEADER).append('\n');
        for (Task task : tasks) {
            text.append(formatRecord(task)).append('\n');
        }
        byte[] bytes = text.toString().getBytes(StandardCharsets.UTF_8);
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "proton-", ".tmp");
        try {
            Files.write(temporary, bytes);
            Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            previousBytes = bytes;
        } finally {
            try {
                Files.deleteIfExists(temporary);
            } catch (IOException exception) {
                // A leftover temporary file must not turn a successful replacement into a failed command.
            }
        }
    }

    /**
     * Reads the save bytes while treating an absent file as an empty task list.
     * Symbolic links are rejected so that writes cannot escape the expected storage path.
     *
     * @return The file bytes, or {@code null} when the save file does not exist.
     * @throws IOException If the path exists but cannot be read safely.
     */
    private byte[] readBytes() throws IOException {
        if (Files.isSymbolicLink(file) || Files.isSymbolicLink(file.getParent())) {
            throw new IOException("Symbolic links are not supported for task storage.");
        }
        try {
            return Files.readAllBytes(file);
        } catch (NoSuchFileException exception) {
            if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
                throw exception;
            }
            return null;
        }
    }

    /**
     * Formats a task as a versioned record with Base64-encoded text fields.
     *
     * @return The serialized task record without a line terminator.
     */
    private String formatRecord(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());
        if (task instanceof Deadline deadline) {
            return "D\t" + status + "\t" + description + "\t" + encode(deadline.getDueDateTime());
        }
        if (task instanceof Event event) {
            return "E\t" + status + "\t" + description + "\t" + encode(event.getStartDateTime())
                    + "\t" + encode(event.getEndDateTime());
        }
        return "T\t" + status + "\t" + description;
    }

    /**
     * Parses and validates one record from the versioned storage format.
     *
     * @return The task represented by the record.
     * @throws IOException If an encoded text field is not valid UTF-8.
     * @throws IllegalArgumentException If the record structure or field values are invalid.
     */
    private Task parseRecord(String line) throws IOException {
        String[] fields = line.split("\t", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid status or field count.");
        }
        String[] details = new String[fields.length - 2];
        for (int i = 2; i < fields.length; i++) {
            details[i - 2] = decode(Base64.getDecoder().decode(fields[i]));
        }
        return createTask(fields[0], fields[1].equals("1"), details);
    }

    /**
     * Parses an unambiguous task from the legacy display-based storage format.
     *
     * @return The task represented by the legacy record.
     * @throws IllegalArgumentException If the record is malformed or its fields are ambiguous.
     */
    private Task parseLegacy(String line) {
        Matcher matcher = Pattern.compile("\\[([TDE])\\]\\[([ X])\\] (.+)").matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid legacy task.");
        }
        String type = matcher.group(1);
        String details = matcher.group(3);
        String[] fields = {details};
        if (!type.equals("T")) {
            String marker = type.equals("D") ? " (by: " : " (from: ";
            int boundary = details.indexOf(marker);
            if (boundary < 0 || boundary != details.lastIndexOf(marker) || !details.endsWith(")")) {
                throw new IllegalArgumentException("Invalid or ambiguous legacy fields.");
            }
            String description = details.substring(0, boundary);
            String times = details.substring(boundary + marker.length(), details.length() - 1);
            fields = new String[]{description, times};
            if (type.equals("E")) {
                int end = times.indexOf(" to: ");
                if (end < 0 || end != times.lastIndexOf(" to: ")) {
                    throw new IllegalArgumentException("Invalid or ambiguous legacy event.");
                }
                fields = new String[]{description, times.substring(0, end), times.substring(end + 5)};
            }
        }
        return createTask(type, matcher.group(2).equals("X"), fields);
    }

    /**
     * Validates decoded fields and creates the task subtype identified by a storage type code.
     *
     * @return The restored task with its saved completion status.
     * @throws IllegalArgumentException If the type, field count, or field contents are invalid.
     */
    private Task createTask(String type, boolean isDone, String[] fields) {
        for (String field : fields) {
            if (field.isBlank() || field.chars().anyMatch(value -> Character.isISOControl(value) && value != '\t')) {
                throw new IllegalArgumentException("Blank or invalid task field.");
            }
        }
        Task task;
        if (type.equals("T") && fields.length == 1) {
            task = new Todo(fields[0]);
        } else if (type.equals("D") && fields.length == 2) {
            task = new Deadline(fields[0], fields[1]);
        } else if (type.equals("E") && fields.length == 3) {
            task = new Event(fields[0], fields[1], fields[2]);
        } else {
            throw new IllegalArgumentException("Unknown type or incorrect field count.");
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Encodes a text field as Base64 using UTF-8 bytes.
     */
    private String encode(String field) {
        return Base64.getEncoder().encodeToString(field.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes UTF-8 bytes strictly so malformed save data is rejected.
     *
     * @throws IOException If the bytes are not valid UTF-8.
     */
    private String decode(byte[] bytes) throws IOException {
        return StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
    }
}

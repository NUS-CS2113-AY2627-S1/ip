package mybff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/** Loads and saves task files, including legacy formats and atomic replacement. */
public class Storage {
    private static final int MAX_SAVE_ATTEMPTS = 5;
    private static final long SAVE_RETRY_DELAY_MILLIS = 100;
    private static final String STORAGE_HEADER = "MyBff storage v3";

    private final Path filePath;

    /**
     * Creates storage for a task file, resolving relative paths against the working directory.
     *
     * @param filePath the file to load from and save to
     */
    public Storage(Path filePath) {
        this.filePath = filePath.toAbsolutePath();
    }

    /**
     * Loads a new task list in saved order, or returns an empty list on the first run.
     *
     * @return the completely loaded task list
     * @throws IOException if the file cannot be read or contains invalid task data
     */
    public TaskList load() throws IOException {
        TaskList tasks = new TaskList();
        if (Files.notExists(filePath)) {
            return tasks;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();
            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }
            boolean isEncoded = "MyBff storage v2".equals(line);
            boolean isEscaped = STORAGE_HEADER.equals(line);
            if (isEncoded || isEscaped) {
                line = reader.readLine();
            }
            while (line != null) {
                if (!line.isBlank()) {
                    tasks.add(parseSavedTask(line, isEncoded, isEscaped));
                }
                line = reader.readLine();
            }
        }
        return tasks;
    }

    /** Restores a task from the pipe-separated format used by the writer. */
    private Task parseSavedTask(String line, boolean isEncoded, boolean isEscaped)
            throws IOException {
        String[] fields = line.split(" \\| ", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IOException("Invalid saved task: " + line);
        }
        if (isEncoded) {
            try {
                for (int i = 2; i < fields.length; i++) {
                    fields[i] = new String(Base64.getDecoder().decode(fields[i]),
                            StandardCharsets.UTF_8);
                }
            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid encoded task field.", exception);
            }
        } else if (isEscaped) {
            for (int i = 2; i < fields.length; i++) {
                fields[i] = decodeField(fields[i]);
            }
        }
        if (fields[2].isBlank()) {
            throw new IOException("Saved task description is empty.");
        }
        Task task;
        if (fields[0].equals("T") && fields.length == 3) {
            task = new ToDo(fields[2]);
        } else if (fields[0].equals("D") && fields.length == 4) {
            task = new Deadline(fields[2], fields[3]);
        } else if (fields[0].equals("E") && fields.length == 5) {
            task = new Event(fields[2], fields[3], fields[4]);
        } else {
            throw new IOException("Invalid saved task: " + line);
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Restores escaped characters in the human-readable storage format. */
    private static String decodeField(String value) throws IOException {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character != '\\') {
                decoded.append(character);
                continue;
            }
            if (++i == value.length()) {
                throw new IOException("Incomplete escape in saved task.");
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
                throw new IOException("Invalid escape in saved task.");
            }
        }
        return decoded.toString();
    }

    /**
     * Atomically replaces the saved list and removes temporary files after writing.
     *
     * @param tasks the task list to save in its current order
     * @throws IOException if the list cannot be saved
     */
    public void save(TaskList tasks) throws IOException {
        Files.createDirectories(filePath.getParent());
        Path temporary = Files.createTempFile(filePath.getParent(), "mybff-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporary)) {
                writer.write(STORAGE_HEADER);
                writer.newLine();
                for (int i = 0; i < tasks.size(); i++) {
                    writer.write(tasks.get(i).toStorageString());
                    writer.newLine();
                }
            }
            replaceSavedFile(temporary);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /** Retries briefly when access to the destination is temporarily denied. */
    private void replaceSavedFile(Path temporary) throws IOException {
        for (int attempt = 1; ; attempt++) {
            try {
                Files.move(temporary, filePath, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
                return;
            } catch (AccessDeniedException exception) {
                if (attempt == MAX_SAVE_ATTEMPTS) {
                    throw exception;
                }
                try {
                    Thread.sleep(SAVE_RETRY_DELAY_MILLIS);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while waiting to save tasks.", interrupted);
                }
            }
        }
    }
}

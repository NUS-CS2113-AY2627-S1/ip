package clammy.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import clammy.task.Task;
import clammy.task.TaskList;

/**
 * Loads and saves tasks in a UTF-8 file relative to the working directory.
 */
public class Storage {
    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("clammy.txt");

    /**
     * Loads all saved tasks, or returns an empty list on a first run.
     *
     * @throws IOException If the file cannot be read or contains a malformed record.
     */
    public TaskList load() throws IOException {
        TaskList tasks = new TaskList();
        if (Files.exists(DATA_DIRECTORY) && !Files.isDirectory(DATA_DIRECTORY)) {
            throw new IOException("The data path must be a folder.");
        }
        if (Files.notExists(DATA_FILE, LinkOption.NOFOLLOW_LINKS)) {
            return tasks;
        }
        if (!Files.isRegularFile(DATA_FILE, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("The saved task path must be a regular file.");
        }
        List<String> records;
        try {
            records = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IOException("The saved task file could not be read.", exception);
        }
        for (int i = 0; i < records.size(); i++) {
            String record = records.get(i);
            if (i == 0 && record.startsWith("\uFEFF")) {
                record = record.substring(1);
            }
            if (record.isBlank()) {
                continue;
            }
            try {
                tasks.add(TaskCodec.decode(record));
            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid saved task on line " + (i + 1) + ".", exception);
            }
        }
        return tasks;
    }

    /**
     * Saves the full list, creating its folder and replacing the previous file after writing.
     *
     * @param tasks Tasks in their displayed order.
     * @throws IOException If the folder or file cannot be written.
     */
    public void save(TaskList tasks) throws IOException {
        List<String> records = new ArrayList<>();
        for (Task task : tasks.asList()) {
            records.add(TaskCodec.encode(task));
        }
        Files.createDirectories(DATA_DIRECTORY);
        Path temporaryFile = Files.createTempFile(DATA_DIRECTORY, "clammy-", ".tmp");
        try {
            Files.write(temporaryFile, records, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, DATA_FILE, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}

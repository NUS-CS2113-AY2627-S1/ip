package eric.storage;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import eric.task.Task;

/**
 * Saves the task list to a text file on the hard disk, one task per line, and loads it back.
 * The file path is relative, so the file is created next to where Eric is run.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage that saves to {@code filePath}.
     *
     * @param filePath Location of the save file, preferably built with {@link Path#of} so that it
     *         works on any operating system.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /** Returns the location of the save file. */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Reads the tasks from the save file. A missing file, or a missing folder, is not an error: it
     * simply means nothing has been saved yet, so the result has no tasks. Blank lines are ignored.
     * If the file is unusable in any other way, it is rejected as a whole and no tasks are returned.
     *
     * @return The tasks in the file, in file order.
     * @throws StorageException If the file is not a file, cannot be read, is not valid UTF-8 text, or
     *         has any line that is not in the save file format. The message lists every invalid line.
     */
    public List<Task> load() throws StorageException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        if (Files.isDirectory(filePath)) {
            throw new StorageException("it is a folder, not a file.");
        }

        List<String> lines = readLines();
        List<Task> tasks = new ArrayList<>();
        List<String> problems = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(Task.fromFileString(line));
            } catch (IllegalArgumentException exception) {
                problems.add("line " + (i + 1) + ": " + exception.getMessage());
            }
        }

        if (!problems.isEmpty()) {
            throw new StorageException("it is not in the expected format:\n   "
                    + String.join("\n   ", problems));
        }
        return tasks;
    }

    /** Returns all lines of the save file, or throws a StorageException that says why it cannot be read. */
    private List<String> readLines() throws StorageException {
        try {
            return Files.readAllLines(filePath);
        } catch (MalformedInputException exception) {
            throw new StorageException("it is not valid UTF-8 text.");
        } catch (IOException exception) {
            throw new StorageException("it could not be read (" + exception.getMessage() + ").");
        }
    }

    /**
     * Writes all {@code tasks} to the save file, replacing its previous contents. Creates the file,
     * and the folder that contains it, if they do not exist yet.
     *
     * @param tasks Tasks to save, in list order.
     * @throws IOException If the file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Path folder = filePath.getParent();
        if (folder != null) {
            Files.createDirectories(folder);
        }
        List<String> lines = tasks.stream().map(Task::toFileString).toList();
        Files.write(filePath, lines);
    }
}

package bob;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Bob's task data on the hard disk.
 */
public class Storage {
    private static final Path DATA_FILE_PATH = Path.of("data", "bob.txt");
    private static final Path TEMP_FILE_PATH = Path.of("data", "bob.tmp");

    /**
     * Prevents instantiation of the storage utility class.
     */
    private Storage() {
    }

    /**
     * Loads all tasks from the data file.
     *
     * @return Tasks stored in the data file, or an empty list if the file does not exist.
     * @throws IOException If the data file cannot be read.
     * @throws BobException If the data file contains an invalid task.
     */
    public static ArrayList<Task> loadTasks() throws IOException, BobException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE_PATH)) {
            return tasks;
        }

        List<String> taskDataLines = Files.readAllLines(DATA_FILE_PATH, StandardCharsets.UTF_8);
        for (int i = 0; i < taskDataLines.size(); i++) {
            String taskData = taskDataLines.get(i);
            if (taskData.isBlank()) {
                continue;
            }

            int lineNumber = i + 1;
            String[] taskParts = taskData.split(" \\| ", -1);
            Task task = createTask(taskParts, lineNumber);
            if (taskParts[1].equals("1")) {
                task.markAsDone();
            }
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * Creates a task from fields read from one line of the data file.
     *
     * @param taskParts Serialized fields describing the task.
     * @param lineNumber Line number used to identify invalid data.
     * @return Task represented by the serialized fields.
     * @throws BobException If the task fields are missing or invalid.
     */
    private static Task createTask(String[] taskParts, int lineNumber) throws BobException {
        if (taskParts.length < 2) {
            throw createInvalidDataException(lineNumber, "some task details are missing");
        }

        String taskType = taskParts[0];
        String taskStatus = taskParts[1];
        if (!taskStatus.equals("0") && !taskStatus.equals("1")) {
            throw createInvalidDataException(lineNumber, "the task status must be 0 or 1");
        }

        int expectedFieldCount;
        if (taskType.equals("T")) {
            expectedFieldCount = 3;
        } else if (taskType.equals("D")) {
            expectedFieldCount = 4;
        } else if (taskType.equals("E")) {
            expectedFieldCount = 5;
        } else {
            throw createInvalidDataException(lineNumber, "the task type '" + taskType + "' isn't recognized");
        }
        if (taskParts.length != expectedFieldCount) {
            throw createInvalidDataException(lineNumber, "the task has the wrong number of details");
        }

        for (int i = 2; i < taskParts.length; i++) {
            taskParts[i] = unescapeField(taskParts[i]);
            if (taskParts[i].isBlank()) {
                throw createInvalidDataException(lineNumber, "task details cannot be blank");
            }
        }

        String description = taskParts[2];
        if (taskType.equals("T")) {
            return new Todo(description);
        } else if (taskType.equals("D")) {
            return new Deadline(description, taskParts[3]);
        }
        return new Event(description, taskParts[3], taskParts[4]);
    }

    /**
     * Returns an exception describing invalid data at a particular line.
     *
     * @param lineNumber Line containing invalid task data.
     * @param reason Explanation of why the data is invalid.
     * @return Exception containing the formatted error message.
     */
    private static BobException createInvalidDataException(int lineNumber, String reason) {
        return new BobException("Invalid data at line " + lineNumber + " because " + reason + ".");
    }

    /**
     * Escapes reserved characters before a task field is saved.
     *
     * @param field Task field to escape.
     * @return Field with backslashes and separators escaped.
     */
    static String escapeField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Restores escaped characters in a field read from storage.
     *
     * @param field Escaped task field.
     * @return Field with supported escape sequences restored.
     */
    private static String unescapeField(String field) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < field.length(); i++) {
            char currentCharacter = field.charAt(i);
            if (currentCharacter == '\\' && i + 1 < field.length()) {
                char nextCharacter = field.charAt(i + 1);
                if (nextCharacter == '\\' || nextCharacter == '|') {
                    result.append(nextCharacter);
                    i++;
                    continue;
                }
            }
            result.append(currentCharacter);
        }
        return result.toString();
    }

    /**
     * Writes all tasks to the data file without leaving a partially written file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE_PATH.getParent());

        List<String> taskData = new ArrayList<>();
        for (Task task : tasks) {
            taskData.add(task.toDataString());
        }
        Files.write(TEMP_FILE_PATH, taskData, StandardCharsets.UTF_8);
        try {
            Files.move(TEMP_FILE_PATH, DATA_FILE_PATH,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(TEMP_FILE_PATH, DATA_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(TEMP_FILE_PATH);
        }
    }
}

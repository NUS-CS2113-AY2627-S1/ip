package alfred;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.Task;
import alfred.task.Todo;

/**
 * Loads tasks from and saves tasks to a data file.
 */
public class Storage {

    static final String DELIMITER = " | ";

    private final Path filePath;

    /**
     * Creates storage that uses the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads valid tasks from the data file and reports invalid lines.
     *
     * @param warningHandler Handler that receives a warning for each invalid line.
     * @return Tasks loaded from the data file.
     * @throws IOException If the data file cannot be read.
     */
    public List<Task> load(Consumer<String> warningHandler) throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return tasks;
        }

        List<String> taskDataLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (String taskLine : taskDataLines) {
            try {
                tasks.add(createTaskFromData(taskLine));
            } catch (AlfredException exception) {
                warningHandler.accept(exception.getMessage());
            }
        }
        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the tasks cannot be written.
     */
    public void save(Iterable<Task> tasks) throws IOException {
        Files.createDirectories(filePath.getParent());

        List<String> taskDataLines = new ArrayList<>();
        for (Task task : tasks) {
            taskDataLines.add(task.toDataString());
        }
        Files.write(filePath, taskDataLines, StandardCharsets.UTF_8);
    }

    /**
     * Recreates a task from one line of stored task data.
     *
     * @param taskData Pipe-delimited task data.
     * @return Task represented by the stored data.
     * @throws AlfredException If the line is missing fields or has an unrecognized task type.
     */
    private Task createTaskFromData(String taskData) throws AlfredException {
        String[] fields = taskData.split(" \\| ", -1);
        if (fields.length < 2) {
            throw new AlfredException("I could not make sense of this line, Master Wayne: "
                    + "it does not match the expected \"type | status | details\" format.\n"
                    + "Offending line: \"" + taskData + "\"");
        }

        String taskType = fields[0];
        boolean isDone = fields[1].equals("1");
        Task task;

        if (taskType.equals("T")) {
            if (fields.length < 3) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "a todo is missing its description.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Todo(fields[2]);
        } else if (taskType.equals("D")) {
            if (fields.length < 4) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "a deadline is missing its description or /by date.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Deadline(fields[2], fields[3]);
        } else if (taskType.equals("E")) {
            if (fields.length < 5) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "an event is missing its description, /from, or /to value.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Event(fields[2], fields[3], fields[4]);
        } else {
            throw new AlfredException("I could not make sense of this line, Master Wayne: "
                    + "unknown task type \"" + taskType + "\".\n"
                    + "Offending line: \"" + taskData + "\"");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}

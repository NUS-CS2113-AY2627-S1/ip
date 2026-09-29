package nia.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import nia.tasks.Task;
import nia.tasks.TaskList;
import nia.ui.Printer;

/**
 * Saves and loads a TaskList to/from a JSON file, so tasks survive between runs.
 * The whole file is one JSON array of tasks, e.g.
 * [{"type":"deadline","description":"finish math homework","isDone":true,"by":"2024-03-11T18:00:00"}].
 * Task's abstractness and LocalDateTime (a type Gson has no built-in adapter for)
 * both need custom handling - see TaskJsonAdapter and LocalDateTimeJsonAdapter.
 * The whole file is rewritten on every save rather than patched incrementally -
 * simplest correct approach at this scale.
 */
public class Storage {
    private static final String DATA_FILE_PATH = "./data/nia.json";
    private static final Type TASK_LIST_TYPE = new TypeToken<List<Task>>(){}.getType();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskJsonAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonAdapter())
            .setPrettyPrinting()
            .create();

    /** Loads tasks from disk into a new TaskList. Never throws: a missing or
     * corrupted file yields an empty list, with a warning for the latter. */
    public static TaskList load() {
        TaskList taskList = new TaskList();
        Path path = Path.of(DATA_FILE_PATH);

        if (!Files.exists(path)) {
            return taskList;
        }

        try {
            String json = Files.readString(path);
            List<Task> tasks = GSON.fromJson(json, TASK_LIST_TYPE);
            if (tasks != null) {
                for (Task task : tasks) {
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            Printer.printIndentedError("Couldn't read saved tasks (" + e.getMessage() + "), starting fresh.");
        } catch (JsonParseException | DateTimeParseException | NullPointerException e) {
            // JsonParseException covers both Gson's own syntax errors and the "unknown
            // type" case TaskJsonAdapter throws; DateTimeParseException is a separate
            // hierarchy (extends RuntimeException directly, not caught by the above) -
            // thrown by LocalDateTimeJsonAdapter when a by/from/to string isn't valid
            // ISO-8601; NullPointerException covers a task object missing an expected
            // field entirely (e.g. hand-edited), which TaskJsonAdapter doesn't guard.
            Printer.printIndentedError("Save file is corrupted, starting fresh.");
        }

        return taskList;
    }

    /** Overwrites the save file with every task currently in taskList. */
    public static void save(TaskList taskList) {
        try {
            Path path = Path.of(DATA_FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= taskList.getSize(); i++) {
                tasks.add(taskList.getTask(i));
            }
            Files.writeString(path, GSON.toJson(tasks, TASK_LIST_TYPE));
        } catch (IOException e) {
            Printer.printIndentedError("Couldn't save tasks: " + e.getMessage());
        }
    }
}

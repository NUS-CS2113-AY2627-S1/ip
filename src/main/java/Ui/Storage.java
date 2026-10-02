package Ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import Task.Deadline;
import Task.Event;
import Task.TasksList;
import Task.ToDo;

/** Saves a snapshot of the current tasks instead of an ever-growing command history. */
public class Storage {
    /** Save location is relative to the program's working directory. */
    private static final Path FILE_PATH = Path.of("data", "tasks.txt");

    /** Saves plain task fields; input must not contain tabs or line breaks inside a field. */
    public void saveTasks(TasksList tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            ToDo task = tasks.get(i);
            String line = task.getTaskIcon() + "\t" + (task.getStatusIcon().equals("X") ? "1" : "0")
                    + "\t" + task.getDescription();
            if (task instanceof Deadline deadline) {
                line += "\t" + deadline.getBy();
            } else if (task instanceof Event event) {
                line += "\t" + event.getFrom() + "\t" + event.getTo();
            }
            lines.add(line);
        }
        Files.createDirectories(FILE_PATH.getParent());
        Path temporary = Files.createTempFile(FILE_PATH.getParent(), "tasks-", ".tmp");
        try {
            Files.write(temporary, lines);
            try {
                Files.move(temporary, FILE_PATH, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /** Loads saved tasks, or returns an empty list when no save file exists yet. */
    public TasksList loadTasks() throws IOException {
        if (!Files.exists(FILE_PATH)) {
            return new TasksList();
        }

        TasksList tasks = new TasksList();
        List<String> lines = Files.readAllLines(FILE_PATH);
        for (int i = 0; i < lines.size(); i++) {
            try {
                tasks.add(readTask(lines.get(i)));
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid task data on line " + (i + 1) + " of " + FILE_PATH, e);
            }
        }
        return tasks;
    }

    /** Reads type, completion status, description, and optional dates from a saved row. */
    private ToDo readTask(String line) {
        String[] fields = line.split("\t", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task fields or status");
        }
        int expectedFields = switch (fields[0]) {
        case "T" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields.length != expectedFields) {
            throw new IllegalArgumentException("Wrong number of task fields");
        }
        for (int i = 2; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                throw new IllegalArgumentException("Empty task field");
            }
        }
        ToDo task = switch (fields[0]) {
        case "D" -> new Deadline(fields[2], fields[3]);
        case "E" -> new Event(fields[2], fields[3], fields[4]);
        default -> new ToDo(fields[2]);
        };
        task.setDone(fields[1].equals("1"));
        return task;
    }
}

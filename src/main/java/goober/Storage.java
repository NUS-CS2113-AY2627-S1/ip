package goober;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import goober.task.Deadline;
import goober.task.Event;
import goober.task.Task;
import goober.task.TaskList;
import goober.task.Todo;

/**
 * Loads tasks from and saves tasks to the application's data file.
 */
public class Storage {
    private static final Path DATA_FILE = Paths.get("data", "goober.txt");

    /**
     * Saves the tasks to the application's data file.
     *
     * @param taskList Tasks to save.
     */
    public void save(TaskList taskList) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            Files.writeString(DATA_FILE, createFileContent(taskList));
        } catch (IOException e) {
            System.out.println("OOPS!!! Unable to save tasks.");
        }
    }

    /**
     * Loads saved tasks from the data file if it exists.
     *
     * @param taskList Task list to populate.
     */
    public void load(TaskList taskList) {
        if(!Files.exists(DATA_FILE)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE);

            for (String line : lines) {
                loadTask(line, taskList);
            }
        } catch (IOException e) {
            System.out.println("OOPS!!! Unable to load tasks!");
        }
    }

    /**
     * Converts one saved line into a task and adds it to the task list.
     *
     * @param line     Saved task line.
     * @param taskList Task list to update.
     */
    private void loadTask(String line, TaskList taskList) {
        String[] parts = line.split(" \\| ");

        if(parts.length < 3) {
            return;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch(type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if(parts.length < 4) {
                    return;
                }
                try {
                    task = new Deadline(description, LocalDateTime.parse(parts[3]));
                } catch (java.time.format.DateTimeParseException e) {
                    return;
                }
                break;
            case "E":
                if(parts.length < 5) {
                    return;
                }
                try {
                    LocalDateTime from = LocalDateTime.parse(parts[3]);
                    LocalDateTime to = LocalDateTime.parse(parts[4]);
                    task = new Event(description, from, to);
                } catch (java.time.format.DateTimeParseException e) {
                    return;
                }
                break;
            default:
                return;
        }

        if(isDone) {
            task.markAsDone();
        }
        taskList.addTask(task);
    }

    /**
     * Converts the task list into the storage file format.
     *
     * @param taskList Tasks to convert.
     * @return Text representation of the tasks.
     */
    private String createFileContent(TaskList taskList) {
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            String status = task.isDone() ? "1" : "0";

            if (task instanceof Todo) {
                content.append("T | ")
                        .append(status)
                        .append(" | ")
                        .append(task.getDescription());
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                content.append("D | ")
                        .append(status)
                        .append(" | ")
                        .append(deadline.getDescription())
                        .append(" | ")
                        .append(deadline.getBy());
            } else if (task instanceof Event) {
                Event event = (Event) task;
                content.append("E | ")
                        .append(status)
                        .append(" | ")
                        .append(event.getDescription())
                        .append(" | ")
                        .append(event.getFrom())
                        .append(" | ")
                        .append(event.getTo());
            }

            content.append(System.lineSeparator());
        }

        return content.toString();
    }
}

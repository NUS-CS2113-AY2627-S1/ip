package doug.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import doug.errors.DougException;
import doug.task.TaskManager;

public class TaskSaver {
    private static final Path DATA_PATH = Path.of("data", "task_list.txt");

    public static void createSaveFile(ArrayList<Task> taskList) {
        try {
            if (DATA_PATH.getParent() != null)
                Files.createDirectories(DATA_PATH.getParent());

            if (!Files.exists(DATA_PATH))
                Files.createFile(DATA_PATH);

            ArrayList<String> saveLines = new ArrayList<>();
            for (int i = 0; i < taskList.size(); ++i) {
                Task task = taskList.get(i);
                String text = String.format("%c|%d|%s",
                        task.getTaskType().toString().charAt(0),
                        task.getIsDone() ? 1 : 0,
                        task.getDescription());
                saveLines.add(text);
            }

            Files.write(DATA_PATH, saveLines);

        } catch (IOException e) {
            throw new DougException("Failed to save tasks.");
        }
    }

    private static Task.Type resolveTaskType(String taskChar) {
        switch (taskChar) {
            case "T":
                return Task.Type.TODO;
            case "D":
                return Task.Type.DEADLINE;
            case "E":
                return Task.Type.EVENT;
            default:
                throw new DougException("Corrupted TaskType");
        }
    }

    private static boolean resolveIsDone(String isDoneChar) {
        return (isDoneChar.equals("1"));
    }

    public static void loadSaveFile() {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            if (DATA_PATH.getParent() != null)
                Files.createDirectories(DATA_PATH.getParent());

            if (!Files.exists(DATA_PATH))
                Files.createFile(DATA_PATH);

            List<String> savedLines = Files.readAllLines(DATA_PATH);
            for (int i = 0; i < savedLines.size(); ++i) {
                String[] taskParams = savedLines.get(i).split("\\|");
                Task task = new Task(taskParams[2], resolveTaskType(taskParams[0]), resolveIsDone(taskParams[1]));
                taskList.add(task);
            }

            TaskManager.initTaskList(taskList);
        } catch (IOException e) {
            throw new DougException("Could not read save file.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException("Save file corrupted.");
        }
    }
}

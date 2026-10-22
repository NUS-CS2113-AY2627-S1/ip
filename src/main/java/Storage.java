import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final int PREFIX_LENGTH = 7;
    private static final String BY_MARKER = "(by: ";
    private static final String FROM_MARKER = "(from: ";
    private static final String TO_MARKER = " to: ";

    private final String filePath;
    private final List<String> loadWarnings = new ArrayList<>();

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws FunkyException {
        ArrayList<Task> tasks = new ArrayList<>();
        loadWarnings.clear();
        Path path = new File(filePath).toPath();
        if (!Files.exists(path)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new FunkyException("Error reading file.");
        }

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (FunkyException e) {
                loadWarnings.add("Skipping unreadable saved line: " + line);
            }
        }
        return tasks;
    }

    public List<String> getLoadWarnings() {
        return loadWarnings;
    }

    public void save(List<Task> tasks) throws FunkyException {
        try {
            File file = new File(filePath);
            File folder = file.getParentFile();
            if (folder != null) {
                folder.mkdirs();
            }

            StringBuilder content = new StringBuilder();
            for (Task task : tasks) {
                content.append(task.toString()).append(System.lineSeparator());
            }
            Files.writeString(file.toPath(), content.toString());
        } catch (IOException e) {
            throw new FunkyException("Error creating file.");
        }
    }

    private Task parseLine(String line) throws FunkyException {
        if (line.length() < PREFIX_LENGTH || line.charAt(0) != '[' || line.charAt(2) != ']'
                || line.charAt(3) != '[' || line.charAt(5) != ']') {
            throw new FunkyException("Bad line");
        }
        char type = line.charAt(1);
        boolean isDone = line.charAt(4) == 'X';
        String body = line.substring(PREFIX_LENGTH).trim();

        Task task;
        if (type == 'T') {
            task = new ToDo(body);
        } else if (type == 'D') {
            task = parseDeadline(body);
        } else if (type == 'E') {
            task = parseEvent(body);
        } else {
            throw new FunkyException("Unknown task type");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private Deadline parseDeadline(String body) throws FunkyException {
        int byIndex = body.lastIndexOf(BY_MARKER);
        if (byIndex < 0 || !body.endsWith(")")) {
            throw new FunkyException("Bad deadline");
        }
        String description = body.substring(0, byIndex).trim();
        String date = body.substring(byIndex + BY_MARKER.length(), body.length() - 1);
        try {
            return new Deadline(description, LocalDate.parse(date, Deadline.DISPLAY_FORMAT));
        } catch (DateTimeParseException e) {
            throw new FunkyException("Bad date");
        }
    }

    private Event parseEvent(String body) throws FunkyException {
        int fromIndex = body.lastIndexOf(FROM_MARKER);
        int toIndex = body.lastIndexOf(TO_MARKER);
        if (fromIndex < 0 || toIndex < fromIndex || !body.endsWith(")")) {
            throw new FunkyException("Bad event");
        }
        String description = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String to = body.substring(toIndex + TO_MARKER.length(), body.length() - 1).trim();
        return new Event(description, from, to);
    }
}
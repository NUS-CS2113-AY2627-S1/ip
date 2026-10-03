package clammy.storage;

import java.util.ArrayList;
import java.util.List;

import clammy.task.Deadline;
import clammy.task.Event;
import clammy.task.Task;
import clammy.task.Todo;

/**
 * Converts tasks to tab-separated records with escaped text fields.
 */
public final class TaskCodec {
    private TaskCodec() {
    }

    /**
     * Returns a single-line record preserving the task's type, status, and text.
     */
    public static String encode(Task task) {
        List<String> fields = new ArrayList<>();
        if (task instanceof Deadline deadline) {
            fields.add("D");
            fields.add(deadline.toStorageString());
        } else if (task instanceof Event event) {
            fields.add("E");
            fields.add(event.getFrom());
            fields.add(event.getTo());
        } else if (task instanceof Todo) {
            fields.add("T");
        } else {
            throw new IllegalArgumentException("Unsupported task type.");
        }
        fields.add(1, task.isDone() ? "1" : "0");
        fields.add(2, task.getDescription());
        for (int i = 0; i < fields.size(); i++) {
            fields.set(i, escape(fields.get(i)));
        }
        return String.join("\t", fields);
    }

    /**
     * Reconstructs a task from a saved record.
     *
     * @param record Tab-separated task record.
     * @return Restored task including its completion status.
     * @throws IllegalArgumentException If any field, escape sequence, or task type is invalid.
     */
    public static Task decode(String record) {
        String[] fields = record.split("\t", -1);
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task fields or status.");
        }
        int expectedFields = switch (fields[0]) {
        case "T" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        default -> throw new IllegalArgumentException("Unknown task type.");
        };
        if (fields.length != expectedFields) {
            throw new IllegalArgumentException("Incorrect number of task fields.");
        }
        for (int i = 2; i < fields.length; i++) {
            fields[i] = unescape(fields[i]);
            if (fields[i].isBlank() || fields[i].contains("\n") || fields[i].contains("\r")) {
                throw new IllegalArgumentException("Task fields must contain text on one line.");
            }
        }
        Task task = switch (fields[0]) {
        case "T" -> new Todo(fields[2]);
        case "D" -> new Deadline(fields[2], fields[3]);
        case "E" -> new Event(fields[2], fields[3], fields[4]);
        default -> throw new IllegalArgumentException("Unknown task type.");
        };
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Escapes backslashes and tabs so each task stays on one tab-separated line.
     */
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\t", "\\t");
    }

    /**
     * Rejects unrecognized escapes instead of silently changing saved task text.
     */
    private static String unescape(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character != '\\') {
                result.append(character);
                continue;
            }
            i++;
            if (i == text.length()) {
                throw new IllegalArgumentException("Incomplete escape sequence.");
            }
            result.append(switch (text.charAt(i)) {
            case '\\' -> '\\';
            case 't' -> '\t';
            default -> throw new IllegalArgumentException("Unknown escape sequence.");
            });
        }
        return result.toString();
    }
}

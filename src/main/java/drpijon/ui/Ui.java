package drpijon.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import drpijon.task.Deadline;
import drpijon.task.Event;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.task.Todo;

/**
 * Handles console input and common user-facing messages.
 */
public class Ui {
    /** Divider printed between command responses. */
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    /** Display format for dates without an explicit time. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy",
            Locale.ENGLISH);
    /** Display format for dates with an explicit time. */
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd yyyy, h:mm a", Locale.ENGLISH);

    /** Fixed application messages shown to the user. */
    private final DrPijon messages;
    /** Console scanner used to read user commands. */
    private final Scanner scanner;

    /**
     * Creates a console user interface connected to standard input.
     */
    public Ui() {
        messages = new DrPijon();
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another console line is available.
     *
     * @return true when another input line is available
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next raw console line.
     *
     * @return next console line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        System.out.println(messages.getBanner());
        System.out.println(messages.getGreet());
    }

    /**
     * Displays the application goodbye message.
     */
    public void showGoodbye() {
        System.out.println(messages.getGoodbye());
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the separator used between command responses.
     */
    public void showLineSeparator() {
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Displays all tasks and their current done status.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("BEHOLD! AN EMPTY LIST ^w^");
            return;
        }

        System.out.println("BEHOLD! Yummy list of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            showTaskLine(tasks.get(i), i + 1);
        }
    }

    /**
     * Displays deadlines and events occurring on a specific date.
     *
     * @param tasks matching tasks to display
     * @param date date used for the lookup
     */
    public void showTasksOnDate(List<Task> tasks, LocalDate date) {
        String formattedDate = date.format(DISPLAY_DATE_FORMAT);
        if (tasks.isEmpty()) {
            System.out.println(String.format("BEHOLD! NO TASKS ON %s ^w^", formattedDate));
            return;
        }

        System.out.println(String.format("BEHOLD! Tasks on %s:", formattedDate));
        for (int i = 0; i < tasks.size(); i++) {
            showTaskLine(tasks.get(i), i + 1);
        }
    }

    /**
     * Displays tasks whose descriptions match a keyword.
     *
     * @param tasks matching tasks to display
     * @param keyword keyword used for the search
     */
    public void showMatchingTasks(List<Task> tasks, String keyword) {
        if (tasks.isEmpty()) {
            System.out.println(String.format("BEHOLD! No tasks match: %s ^w^", keyword));
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showTaskLine(tasks.get(i), i + 1);
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        String taskType = getTaskTypeName(task);
        System.out.println(String.format("HMMMMMMMMM ok, %s added:", taskType));
        System.out.println(String.format("  [%c][%c] %s", task.getTaskType(), task.isDone() ? 'X' : ' ',
                formatTaskDetails(task)));
        String suffix = task instanceof Todo ? " ^w^" : "";
        System.out.println(String.format("Now you have %d tasks in the list.%s", taskCount, suffix));
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task deleted task
     * @param taskCount number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("COO COO! Task deleted:");
        showTaskSummary(task);
        System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
    }

    /**
     * Displays confirmation that a task was marked as complete.
     *
     * @param task updated task
     */
    public void showTaskMarked(Task task) {
        System.out.println("COO COO! Task marked as COMPLETE:");
        showTaskSummary(task);
    }

    /**
     * Displays confirmation that a task was unmarked.
     *
     * @param task updated task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("COO COO! Task unmarked:");
        showTaskSummary(task);
    }

    /**
     * Displays the compact representation used by update confirmations.
     *
     * @param task task to summarize
     */
    private void showTaskSummary(Task task) {
        char typeMarker = task.getTaskType();
        char statusMarker = task.isDone() ? 'X' : ' ';
        System.out.println(String.format("  [%c][%c] %s", typeMarker, statusMarker, task.getDescription()));
    }

    /**
     * Displays one numbered task using the full task details.
     *
     * @param task task to display
     * @param displayIndex one-based index to display
     */
    private void showTaskLine(Task task, int displayIndex) {
        char typeMarker = task.getTaskType();
        char statusMarker = task.isDone() ? 'X' : ' ';
        System.out.println(String.format("%d. [%c][%c] %s", displayIndex, typeMarker, statusMarker,
                formatTaskDetails(task)));
    }

    /**
     * Formats type-specific details for a task display.
     *
     * @param task task whose details should be formatted
     * @return formatted task details
     */
    private String formatTaskDetails(Task task) {
        if (task instanceof Deadline deadline) {
            String by;
            if (!deadline.hasDate()) {
                by = deadline.getByText();
            } else if (deadline.hasTime()) {
                by = deadline.getBy().format(DISPLAY_DATE_TIME_FORMAT);
            } else {
                by = deadline.getBy().format(DISPLAY_DATE_FORMAT);
            }
            return String.format("%s (by: %s)", deadline.getDescription(), by);
        }
        if (task instanceof Event event) {
            String from = formatEventBoundary(event.getFrom(), event.hasFromDate(), event.hasFromTime(),
                    event.getFromText());
            String to = formatEventBoundary(event.getTo(), event.hasToDate(), event.hasToTime(), event.getToText());
            return String.format("%s (from: %s to: %s)", event.getDescription(), from, to);
        }
        return task.getDescription();
    }

    /**
     * Formats one event boundary while preserving legacy free-text values.
     *
     * @param boundary parsed event boundary
     * @param hasDate whether the boundary was parsed as a date
     * @param hasTime whether the boundary includes an explicit time
     * @param legacyText original free-text boundary
     * @return formatted event boundary
     */
    private String formatEventBoundary(LocalDateTime boundary, boolean hasDate, boolean hasTime, String legacyText) {
        if (!hasDate) {
            return legacyText;
        }
        return hasTime ? boundary.format(DISPLAY_DATE_TIME_FORMAT) : boundary.format(DISPLAY_DATE_FORMAT);
    }

    /**
     * Returns the user-facing name for a task type.
     *
     * @param task task whose type should be named
     * @return user-facing task type name
     */
    private String getTaskTypeName(Task task) {
        if (task instanceof Todo) {
            return "Todo";
        }
        if (task instanceof Deadline) {
            return "Deadline";
        }
        return "Event";
    }
}

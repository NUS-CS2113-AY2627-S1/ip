package goober;

import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;

import goober.task.Deadline;
import goober.task.Event;
import goober.task.Task;
import goober.task.TaskList;
import goober.task.Todo;

/**
 * Handles all console input and output for Goober.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    /** Displays the application welcome message. */
    public void showWelcome() {
        String banner = "____________________________________________________________\n" +
                "  ____              _               \n"
                + " / ___| ___   ___  | |__   ___  _ __  \n"
                + "| |  _ / _ \\ / _ \\ | '_ \\ / _ \\| '__| \n"
                + "| |_| | (_) | (_) || |_) |  __/| |    \n"
                + " \\____|\\___/ \\___/ |_.__/ \\___||_|    \n"
                + "____________________________________________________________\n";
        System.out.println(banner);
        System.out.println("Hello! I'm Goober.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /** Displays the application exit message. */
    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays the confirmation message after adding a task.
     *
     * @param task      Newly added task.
     * @param taskCount Number of tasks currently stored.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }

    /**
     * Displays the commands supported by Goober and their expected input formats.
     */
    public void showHelpMessage() {
        System.out.println(LINE);
        System.out.println("Here are the commands you can use:");
        System.out.println("  todo <description>");
        System.out.println("  deadline <description> /by <d/M/yyyy HHmm>");
        System.out.println("  event <description> /from <d/M/yyyy HHmm> /to <d/M/yyyy HHmm>");
        System.out.println("  list");
        System.out.println("  list todos");
        System.out.println("  list deadlines");
        System.out.println("  list events");
        System.out.println("  list today");
        System.out.println("  list <d/M/yyyy>");
        System.out.println("  find <keyword>");
        System.out.println("  mark <task number>");
        System.out.println("  unmark <task number>");
        System.out.println("  delete <task number>");
        System.out.println("  help");
        System.out.println("  bye");
        System.out.println(LINE);
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param taskList Task list to display.
     */
    public void showTaskList(TaskList taskList) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        System.out.println("Use the index on the left to mark or delete a task.");

        printSection("Todos", Todo.class, taskList);
        printSortedSection("Deadlines", Deadline.class, taskList,
                Comparator.comparing(index -> ((Deadline) taskList.getTask(index)).getBy()));
        printSortedSection("Events", Event.class, taskList,
                Comparator.comparing((Integer index) -> ((Event) taskList.getTask(index)).getFrom())
                        .thenComparing(index -> ((Event) taskList.getTask(index)).getTo()));

        System.out.println("Now you have " + taskList.getTaskCount() + " tasks in the list!");
        System.out.println(LINE);
    }

    /**
     * Displays tasks belonging to one requested task type.
     *
     * @param taskList Task list containing the tasks.
     * @param taskType Task type to display: todos, deadlines, or events.
     */
    public void showTaskList(TaskList taskList, String taskType) {
        System.out.println(LINE);
        System.out.println("Here are the " + taskType + " in your list:");
        System.out.println("Use the index on the left to mark or delete a task.");

        switch (taskType) {
        case "todos":
            printSection("Todos", Todo.class, taskList);
            break;
        case "deadlines":
            printSortedSection("Deadlines", Deadline.class, taskList,
                    Comparator.comparing(index -> ((Deadline) taskList.getTask(index)).getBy()));
            break;
        case "events":
            printSortedSection("Events", Event.class, taskList,
                    Comparator.comparing((Integer index) -> ((Event) taskList.getTask(index)).getFrom())
                            .thenComparing(index -> ((Event) taskList.getTask(index)).getTo()));
            break;
        default:
            return;
        }

        System.out.println(LINE);
    }

    /**
     * Displays deadlines and events occurring on the specified date.
     *
     * @param taskList Task list containing the tasks.
     * @param date Date on which tasks should occur.
     */
    public void showTaskListOnDate(TaskList taskList, LocalDate date) {
        System.out.println(LINE);
        System.out.println("Here are the deadlines and events for " + date + ":");
        System.out.println("Use the index on the left to mark or delete a task.");

        ArrayList<Integer> deadlineIndices = new ArrayList<>();
        ArrayList<Integer> eventIndices = new ArrayList<>();
        for (int i = 0; i < taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (task instanceof Deadline deadline && deadline.getBy().toLocalDate().equals(date)) {
                deadlineIndices.add(i);
            } else if (task instanceof Event event
                    && !date.isBefore(event.getFrom().toLocalDate())
                    && !date.isAfter(event.getTo().toLocalDate())) {
                eventIndices.add(i);
            }
        }

        deadlineIndices.sort(Comparator.comparing(index -> ((Deadline) taskList.getTask(index)).getBy()));
        eventIndices.sort(Comparator.comparing((Integer index) -> ((Event) taskList.getTask(index)).getFrom())
                .thenComparing(index -> ((Event) taskList.getTask(index)).getTo()));

        printDateSection("Deadlines", "No deadlines today!", deadlineIndices, taskList);
        printDateSection("Events", "No events today!", eventIndices, taskList);
        System.out.println(LINE);
    }

    /**
     * Displays tasks whose descriptions contain the supplied keyword.
     *
     * @param taskList Task list to search.
     * @param keyword Keyword to find, matched without regard to case.
     */
    public void showMatchingTasks(TaskList taskList, String keyword) {
        String normalizedKeyword = keyword.toLowerCase();

        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");

        boolean hasMatches = false;
        for (int i = 0; i < taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (task.getDescription().toLowerCase().contains(normalizedKeyword)) {
                System.out.println((i + 1) + ". " + task);
                hasMatches = true;
            }
        }

        if (!hasMatches) {
            System.out.println("No matching tasks found.");
        }
        System.out.println(LINE);
    }

    /**
     * Prints tasks of one type while retaining their original task-list numbers.
     *
     * @param sectionName Name of the section to display.
     * @param taskType Type of task to include in the section.
     * @param taskList Task list containing the tasks.
     */
    private void printSection(String sectionName, Class<?> taskType, TaskList taskList) {
        System.out.println("\n" + sectionName + ":");

        for (int i = 0; i < taskList.getTaskCount(); i++) {
            Task task = taskList.getTask(i);
            if (taskType.isInstance(task)) {
                System.out.println((i + 1) + ". " + task);
            }
        }
    }

    /**
     * Prints tasks of one type sorted by their date values while retaining their original indices.
     *
     * @param sectionName Name of the section to display.
     * @param taskType Type of task to include in the section.
     * @param taskList Task list containing the tasks.
     * @param comparator Comparator for the task indices.
     */
    private void printSortedSection(String sectionName, Class<?> taskType, TaskList taskList,
                                    Comparator<Integer> comparator) {
        System.out.println("\n" + sectionName + ":");
        ArrayList<Integer> taskIndices = new ArrayList<>();

        for (int i = 0; i < taskList.getTaskCount(); i++) {
            if (taskType.isInstance(taskList.getTask(i))) {
                taskIndices.add(i);
            }
        }

        taskIndices.sort(comparator);
        printIndices(taskIndices, taskList);
    }

    /**
     * Prints a section heading followed by indexed tasks.
     *
     * @param sectionName Section heading.
     * @param taskIndices Indices of tasks to print.
     * @param taskList Task list containing the tasks.
     */
    private void printIndices(String sectionName, ArrayList<Integer> taskIndices, TaskList taskList) {
        System.out.println("\n" + sectionName + ":");
        printIndices(taskIndices, taskList);
    }

    /**
     * Prints a date-filtered section or its empty-state message.
     *
     * @param sectionName Section heading.
     * @param emptyMessage Message shown when no tasks match.
     * @param taskIndices Indices of matching tasks.
     * @param taskList Task list containing the tasks.
     */
    private void printDateSection(String sectionName, String emptyMessage,
                                  ArrayList<Integer> taskIndices, TaskList taskList) {
        System.out.println("\n" + sectionName + ":");
        if (taskIndices.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            printIndices(taskIndices, taskList);
        }
    }

    /**
     * Prints tasks using their original task-list indices.
     *
     * @param taskIndices Indices of tasks to print.
     * @param taskList Task list containing the tasks.
     */
    private void printIndices(ArrayList<Integer> taskIndices, TaskList taskList) {
        for (int index : taskIndices) {
            System.out.println((index + 1) + ". " + taskList.getTask(index));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task Marked task.
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Good job! I've marked this task as done:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task Unmarked task.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK bro, I've marked this task as not done yet:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task Deleted task.
     * @param taskCount Number of remaining tasks.
     */
    public void showDeleted(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays a suggested command.
     *
     * @param suggestion Suggested valid command.
     */
    public void showCommandSuggestion(String suggestion) {
        System.out.println("Did you mean \"" + suggestion + "\"?");
    }
}

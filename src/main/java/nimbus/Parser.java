package nimbus;
import java.time.LocalDateTime;

/**
 * Interprets user commands and calls the appropriate task operations.
 */
public class Parser {

    /**
     * Processes a command and displays its result.
     *
     * @param input User command.
     * @param tasks Current task list.
     * @param ui User interface for responses.
     */
    public void execute(String input, TaskList tasks, Ui ui) {
        String[] commandParts = input.trim().split("\\s+", 2);
        String command = commandParts[0];
        String arguments = commandParts.length > 1 ? commandParts[1].trim() : "";

        try {
            switch (command) {
                case "list":
                    ui.showTaskList(tasks);
                    break;
                case "todo":
                    requireText(arguments, "Missing the brief. Use: todo <description>");
                    validateTaskField(arguments);
                    addTask(new Todo(arguments), tasks, ui);
                    break;
                case "deadline":
                    addDeadline(arguments, tasks, ui);
                    break;
                case "event":
                    addEvent(arguments, tasks, ui);
                    break;
                case "mark":
                    setTaskStatus(arguments, true, tasks, ui);
                    break;
                case "unmark":
                    setTaskStatus(arguments, false, tasks, ui);
                    break;
                case "delete":
                    int index = parseIndex(arguments, tasks);
                    Task removedTask = tasks.remove(index);
                    tasks.save();
                    ui.showMessage("Off the agenda. I don't chase cancelled plans:\n  " + removedTask);
                    ui.showMessage("Still on the radar: " + tasks.size());
                    break;
                case "find":
                    requireText(arguments, "Give me a lead. Use: find <keyword>");
                    ui.showMatchingTasks(tasks, arguments);
                    break;
                default:
                    ui.showMessage("Wait a second...");
                    ui.showMessage("Who invited that command? Check the list below.");
                    ui.showMessage("Use: todo, deadline, event, list, find, mark, unmark, delete, or bye.");
            }
        } catch (IllegalArgumentException exception) {
            ui.showMessage("Wait a second...");
            ui.showMessage(exception.getMessage());
        }catch (IllegalStateException exception) {
            ui.showMessage(exception.getMessage());
        }
    }

    /**
     * Checks that a required field contains text.
     */
    private void requireText(String text, String errorMessage) {
        if (text.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks that a task field is nonempty and safe for the storage format.
     */
    private void validateTaskField(String text) {
        requireText(text, "An empty brief? Give me something to work with.");

        if (text.contains("|")) {
            throw new IllegalArgumentException("'|' is reserved for my filing system. Keep it out of task fields.");
        }
    }

    /**
     * Adds a task, saves the list, and displays a confirmation.
     */
    private void addTask(Task task, TaskList tasks, Ui ui) {
        tasks.add(task);
        tasks.save();

        ui.showMessage("Booked. Briefcase business:\n  " + task);
        ui.showMessage("Tasks under management: " + tasks.size());
    }

    /**
     * Parses and adds a deadline task.
     */
    private void addDeadline(String arguments, TaskList tasks, Ui ui) {
        String[] deadlineParts = arguments.split(" /by ", 2);

        if (deadlineParts.length != 2) {
            throw new IllegalArgumentException(
                    "Missing the details. Use: deadline <description> /by yyyy-MM-dd HHmm");
        }

        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();

        validateTaskField(description);
        validateTaskField(by);
        DateTimes.parse(by);

        addTask(new Deadline(description, by), tasks, ui);
    }

    /**
     * Parses and adds an event task.
     */
    private void addEvent(String arguments, TaskList tasks, Ui ui) {
        String[] eventParts = arguments.split(" /from ", 2);

        if (eventParts.length != 2) {
            throw new IllegalArgumentException(
                    "Use: event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);

        if (timeParts.length != 2) {
            throw new IllegalArgumentException(
                    "Missing the finish. Add /to followed by the end date and time.");
        }

        String description = eventParts[0].trim();
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        validateTaskField(description);
        validateTaskField(from);
        validateTaskField(to);

        LocalDateTime start = DateTimes.parse(from);
        LocalDateTime end = DateTimes.parse(to);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException(
                    "Wait, time travel? The event can't end before it starts.");
        }

        addTask(new Event(description, from, to), tasks, ui);
    }

    /**
     * Converts a user task number into a validated zero-based index.
     */
    private int parseIndex(String arguments, TaskList tasks) {
        int taskNumber;

        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "I need a task number. 'Vibes' is not an index.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IllegalArgumentException(
                    "That task doesn't exist. Even I can't manage imaginary business.");
        }

        return taskNumber - 1;
    }

    /**
     * Updates a task's completion status and saves the list.
     */
    private void setTaskStatus(String arguments, boolean isDone, TaskList tasks, Ui ui) {
        Task task = tasks.get(parseIndex(arguments, tasks));

        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        tasks.save();

        if (isDone) {
            ui.showMessage("Another one handled. Naturally:");
        } else {
            ui.showMessage("We're reopening the case:");
        }

        ui.showMessage("  " + task);
    }
}
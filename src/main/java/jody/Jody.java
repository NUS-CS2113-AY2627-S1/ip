package jody;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

/**
 * Runs the Jody command-line task manager and coordinates its UI and storage.
 */
public class Jody {
    private final Ui ui = new Ui();

    /**
     * Starts a new Jody session.
     *
     * @param args command-line arguments; currently unused
     */
    public static void main(String[] args) {
        new Jody().run();
    }

    /**
     * Loads saved tasks, displays the welcome message, and starts command processing.
     * Displays storage errors and closes the UI when the session ends.
     */
    public void run() {
        Storage storage = new Storage(Path.of("data", "jody.txt"));

        try (ui) {
            ArrayList<Task> taskList = storage.load();

            ui.showWelcome();
            runCommand(taskList, storage);
        } catch (JodyException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Processes input until {@code bye} or end of input and saves when requested.
     * Displays command and save errors while allowing the session to continue.
     *
     * @param taskList the mutable task list for this session
     * @param storage the storage used to save tasks
     */
    private void runCommand(ArrayList<Task> taskList, Storage storage) {
        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }

            try {
                boolean hasChanged = processTask(line, taskList);

                if (hasChanged) {
                    storage.save(taskList);
                }
            } catch (JodyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Parses and executes a task command.
     * The current implementation also requests a save after {@code find}.
     *
     * @param line the trimmed command line
     * @param taskList the mutable task list
     * @return {@code true} when the caller should save the task list
     * @throws JodyException if the command or its arguments are invalid
     */
    private boolean processTask(String line, ArrayList<Task> taskList) throws JodyException {
        String[] parts = line.split("\\s+", 2);
        String command = parts[0].toLowerCase(Locale.ROOT);
        String arguments = parts.length == 2 ? parts[1].trim() : "";

        switch (command) {
            case "list":
                requireNoArguments(arguments, "list");
                ui.showTasks(taskList, taskList.size());
                return false;

            case "on":
                showTasksOnDate(arguments, taskList);
                return false;

            case "todo":
                requireDescription(arguments, "Please give your todo a description. "
                        + "Example: todo read a book");
                addTask(new Todo(arguments), taskList);
                return true;

            case "deadline":
                requireDescription(arguments, "Please give your deadline a description and date. "
                        + "Example: deadline return book /by 2/12/2019 1800");
                addTask(new Deadline(arguments), taskList);
                return true;

            case "event":
                requireDescription(arguments, "Please give your event a description and dates. "
                        + "Example: event meeting "
                        + "/from 2/12/2019 1400 /to 2/12/2019 1600");
                addTask(new Event(arguments), taskList);
                return true;

            case "mark":
                return markTask(arguments, taskList);

            case "unmark":
                return unmarkTask(arguments, taskList);

            case "delete":
                deleteTask(arguments, taskList);
                return true;

            case "find":
                findTask(arguments, taskList);
                return true;

            default:
                throw new JodyException("I don't recognize that command. "
                        + "Try todo, deadline, event, list, on, "
                        + "mark, unmark, delete, or bye.");
        }
    }

    /**
     * Checks that required description text is present.
     *
     * @param description the text to validate
     * @param message the error message to use for blank text
     * @throws JodyException if the description is blank
     */
    private void requireDescription(String description, String message)
            throws JodyException {
        if (description.isBlank()) {
            throw new JodyException(message);
        }
    }

    /**
     * Checks that a command was supplied without arguments.
     *
     * @param arguments the trimmed command arguments
     * @param command the command name used in error messages
     * @throws JodyException if the arguments are not empty
     */
    private void requireNoArguments(String arguments, String command)
            throws JodyException {
        if (!arguments.isEmpty()) {
            throw new JodyException("The " + command + " command does not take arguments.");
        }
    }

    /**
     * Appends a task and displays the updated task count.
     *
     * @param task the task to add
     * @param taskList the mutable task list
     */
    private void addTask(Task task, ArrayList<Task> taskList) {
        taskList.add(task);
        ui.showAddedTask(task, taskList.size());
    }

    /**
     * Deletes the task selected by its one-based number and displays confirmation.
     *
     * @param arguments the task number as text
     * @param taskList the mutable task list
     * @throws JodyException if the task number is invalid
     */
    private void deleteTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "delete");
        Task removedTask = taskList.remove(taskIndex);

        ui.showDeletedTask(removedTask, taskList.size());
    }

    /**
     * Marks the selected task as completed and displays confirmation.
     *
     * @param arguments the one-based task number as text
     * @param taskList the task list containing the selected task
     * @return {@code true} if the completion status changed
     * @throws JodyException if the task number is invalid
     */
    private boolean markTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "mark");
        Task task = taskList.get(taskIndex);
        boolean hasChanged = !task.isDone();

        task.markAsDone();
        ui.showMarkedTask(task);

        return hasChanged;
    }

    /**
     * Marks the selected task as incomplete and displays confirmation.
     *
     * @param arguments the one-based task number as text
     * @param taskList the task list containing the selected task
     * @return {@code true} if the completion status changed
     * @throws JodyException if the task number is invalid
     */
    private boolean unmarkTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        int taskIndex = parseTaskNumber(arguments, taskList.size(), "unmark");
        Task task = taskList.get(taskIndex);
        boolean hasChanged = task.isDone();

        task.markAsNotDone();
        ui.showUnmarkedTask(task);

        return hasChanged;
    }

    /**
     * Displays tasks whose descriptions contain the case-sensitive search text.
     * An empty search matches every description.
     *
     * @param arguments the search text
     * @param taskList the tasks to search
     * @throws JodyException reserved by the command-handler signature;
     *         this implementation does not throw it
     */
    private void findTask(String arguments, ArrayList<Task> taskList) throws JodyException {
        ui.findInDescription(arguments, taskList);
    }

    /**
     * Validates a one-based task number and converts it to a zero-based index.
     *
     * @param arguments the task number as text
     * @param taskCount the number of available tasks
     * @param command the command name used in usage examples
     * @return the zero-based task index
     * @throws JodyException if the number is missing, invalid, or out of range,
     *         or the task list is empty
     */
    private int parseTaskNumber(String arguments, int taskCount, String command) throws JodyException {
        if (arguments.isBlank()) {
            throw new JodyException("Please enter a task number. Example: " + command + " 1");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new JodyException("Please enter one whole task number. "
                    + "Example: " + command + " 1");
        }

        if (taskCount == 0) {
            throw new JodyException("Your task list is empty. Add a task first.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new JodyException("Please enter a task number between 1 and "
                    + taskCount + ".");
        }

        return taskNumber - 1;
    }

    /**
     * Displays deadlines due on a date and events spanning that date inclusively.
     * Sorts matches by deadline time or event start time and retains original task numbers.
     *
     * @param input the date in a supported format
     * @param taskList the tasks to inspect
     * @throws JodyException if the date is invalid
     */
    private void showTasksOnDate(String input, ArrayList<Task> taskList) throws JodyException {
        LocalDate date = DateTimeParser.parseDate(input);
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            if (task instanceof Deadline deadline) {
                if (deadline.getBy().toLocalDate().equals(date)) {
                    matches.add(i);
                }
            } else if (task instanceof Event event) {
                if (!date.isBefore(event.getFrom().toLocalDate())
                        && !date.isAfter(event.getTo().toLocalDate())) {
                    matches.add(i);
                }
            }
        }

        matches.sort(Comparator
                .comparing(index -> scheduledTime(taskList.get(index))));

        ui.showTasksOnDate(date, taskList, matches);
    }

    /**
     * Returns the date/time used to sort a deadline or event.
     *
     * @param task a deadline or event
     * @return the deadline's due time or the event's start time
     */
    private LocalDateTime scheduledTime(Task task) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy();
        }

        return ((Event) task).getFrom();
    }
}

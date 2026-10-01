package goober;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import goober.task.Deadline;
import goober.task.Event;
import goober.task.Task;
import goober.task.TaskList;
import goober.task.Todo;

/**
 * Coordinates command parsing, task operations, storage, and user feedback.
 */
public class CommandHandler {
    private static final DateTimeFormatter DEADLINE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
    private static final DateTimeFormatter LIST_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu");

    private final TaskList taskList;
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;

    /**
     * Creates a command handler with the application's supporting components.
     *
     * @param taskList Task list to modify.
     * @param ui User interface used for feedback.
     * @param parser Parser used to identify commands.
     * @param storage Storage used to persist changes.
     */
    public CommandHandler(TaskList taskList, Ui ui, Parser parser, Storage storage) {
        this.taskList = taskList;
        this.ui = ui;
        this.parser = parser;
        this.storage = storage;
    }

    /**
     * Parses and adds a todo task.
     *
     * @param input    Full todo command.
     */
    private void addTodo(String input) throws GooberException {
        String description = input.length() <= 4 ? "" : input.substring(5).trim();
        if (description.isEmpty()) {
            throw new GooberException("The description of a todo cannot be empty!");
        }
        Task task = new Todo(description);
        taskList.addTask(task);
        storage.save(taskList);
        ui.showTaskAdded(task, taskList.getTaskCount());
    }

    /**
     * Parses and adds a deadline task.
     *
     * @param input    Full deadline command.
     */
    private void addDeadline(String input) throws GooberException {
        int byIndex = input.indexOf("/by ");
        if (byIndex < 0) {
            throw new GooberException("A deadline must have a description and a /by value!");
        }
        String description = input.substring(9, byIndex).trim();
        String byText = input.substring(byIndex + 4).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new GooberException("A deadline must have a description and a /by value!");
        }

        LocalDateTime by;
        try {
            by = LocalDateTime.parse(byText, DEADLINE_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new GooberException("Use the deadline format d/M/yyyy HHmm, for example 2/12/2019 1800.");
        }

        Task task = new Deadline(description, by);
        taskList.addTask(task);
        storage.save(taskList);
        ui.showTaskAdded(task, taskList.getTaskCount());
    }

    /**
     * Parses and adds an event task.
     *
     * @param input    Full event command.
     */
    private void addEvent(String input) throws GooberException {
        int fromIndex = input.indexOf("/from ");
        int toIndex = input.indexOf("/to ");
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new GooberException("An event must have a description, a /from value, and a /to value!");
        }
        String description = input.substring(6, fromIndex).trim();
        String fromText = input.substring(fromIndex + 6, toIndex).trim();
        String toText = input.substring(toIndex + 4).trim();
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new GooberException("An event must have a description, a /from value, and a /to value!");
        }

        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(fromText, DEADLINE_INPUT_FORMAT);
            to = LocalDateTime.parse(toText, DEADLINE_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new GooberException("Use the event format d/M/yyyy HHmm, for example 2/12/2019 1800.");
        }

        Task task = new Event(description, from, to);
        taskList.addTask(task);
        storage.save(taskList);
        ui.showTaskAdded(task, taskList.getTaskCount());
    }

    /** Marks the task selected by a user command as completed. */
    private void markTask(String input) {
        int index;
        try {
            index = Integer.parseInt(input.substring(5)) - 1;
        } catch (NumberFormatException e) {
            ui.showError("Enter a valid task number!");
            return;
        }

        if (index < 0 || index >= taskList.getTaskCount()) {
            ui.showError("Task not found!");
        } else if (taskList.getTask(index).isDone()) {
            ui.showError("This task is already marked as done!");
        } else {
            Task task = taskList.getTask(index);
            task.markAsDone();
            storage.save(taskList);
            ui.showTaskMarked(task);
        }
    }

    /** Marks the task selected by a user command as incomplete. */
    private void unmarkTask(String input) {
        int index;
        try {
            index = Integer.parseInt(input.substring(7)) - 1;
        } catch (NumberFormatException e) {
            ui.showError("Enter a valid task number!");
            return;
        }

        if (index < 0 || index >= taskList.getTaskCount()) {
            ui.showError("Task not found!");
        } else if (!taskList.getTask(index).isDone()) {
            ui.showError("This task is already marked as not done!");
        } else {
            Task task = taskList.getTask(index);
            task.markAsUndone();
            storage.save(taskList);
            ui.showTaskUnmarked(task);
        }
    }

    /** Deletes the task selected by a user command. */
    private void deleteTask(String input) {
        int index;
        try {
            index = Integer.parseInt(input.substring(6).trim()) - 1;
        } catch (NumberFormatException e) {
            ui.showError("Enter a valid task number!");
            return;
        }

        if (index < 0 || index >= taskList.getTaskCount()) {
            ui.showError("Task not found!");
            return;
        }
        Task task = taskList.getTask(index);
        taskList.deleteTask(index);
        storage.save(taskList);
        ui.showDeleted(task, taskList.getTaskCount());
    }

    /**
     * Executes one user command.
     *
     * @param input Complete command entered by the user.
     * @return {@code true} when the application should terminate.
     */
    public boolean handleCommand(String input) {
        String command = parser.getCommand(input);
        switch (command) {
            case "bye":
                if (input.equalsIgnoreCase("bye")) {
                    ui.showBye();
                    return true;
                }
                ui.showError("The bye command does not accept additional input!");
                return false;
            case "list":
                if (input.equalsIgnoreCase("list")) {
                    ui.showTaskList(taskList);
                } else if (input.equalsIgnoreCase("list todos")) {
                    ui.showTaskList(taskList, "todos");
                } else if (input.equalsIgnoreCase("list deadlines")) {
                    ui.showTaskList(taskList, "deadlines");
                } else if (input.equalsIgnoreCase("list events")) {
                    ui.showTaskList(taskList, "events");
                } else if (input.toLowerCase().startsWith("list ")) {
                    String dateText = input.substring(5).trim();
                    try {
                        LocalDate date = dateText.equalsIgnoreCase("today")
                                ? LocalDate.now(ZoneId.of("Asia/Singapore"))
                                : LocalDate.parse(dateText, LIST_DATE_FORMAT);
                        ui.showTaskListOnDate(taskList, date);
                    } catch (DateTimeParseException e) {
                        ui.showError("Use list today or list d/M/yyyy, for example list 2/12/2019.");
                    }
                } else {
                    ui.showError("Use list, list todos, list deadlines, or list events.");
                }
                return false;
            case "find":
                if (input.length() > 5) {
                    ui.showMatchingTasks(taskList, input.substring(5).trim());
                } else {
                    ui.showError("Enter a keyword to search for!");
                }
                return false;
            case "help":
                ui.showHelpMessage();
                return false;
            case "mark":
                if (input.length() > 4) {
                    markTask(input);
                } else {
                    ui.showError("Enter a valid task number!");
                }
                return false;
            case "unmark":
                if (input.length() > 6) {
                    unmarkTask(input);
                } else {
                    ui.showError("Enter a valid task number!");
                }
                return false;
            case "delete":
                if(input.length() > 6){
                    deleteTask(input);
                } else {
                    ui.showError("Enter a valid task number!");
                }
                return false;

            case "todo":
                try {
                    addTodo(input);
                } catch (GooberException e) {
                    ui.showError(e.getMessage());
                }
                return false;
            case "deadline":
                try {
                    addDeadline(input);
                } catch (GooberException e) {
                    ui.showError(e.getMessage());
                }
                return false;
            case "event":
                try {
                    addEvent(input);
                } catch (GooberException e) {
                    ui.showError(e.getMessage());
                }
                return false;
            default:
                ui.showError("Sorry bro, I don't know what that means :-(");
                String suggestion = parser.getSuggestedCommand(input);
                if (suggestion != null) {
                    ui.showCommandSuggestion(suggestion);
                }
                return false;
        }
    }
}

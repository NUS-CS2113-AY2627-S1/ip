package mimimeow.command;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import mimimeow.exception.MimiMeowException;
import mimimeow.storage.Storage;
import mimimeow.storage.StorageException;
import mimimeow.task.Deadline;
import mimimeow.task.Event;
import mimimeow.task.Task;
import mimimeow.task.TaskList;
import mimimeow.task.Todo;
import mimimeow.ui.MimiMeowUi;

/** Executes parsed MimiMeow commands and updates the task list. */
public class CommandHandler {
    private final TaskList taskList;
    private final CommandParser commandParser;
    private final MimiMeowUi ui;
    private final Storage storage;
    private final Clock clock;

    /**
     * Creates a command handler with the supplied application components.
     *
     * @param taskList task list to manage
     * @param commandParser parser for user commands
     * @param ui user interface for responses
     * @param storage persistent task storage
     */
    public CommandHandler(TaskList taskList, CommandParser commandParser, MimiMeowUi ui, Storage storage) {
        this(taskList, commandParser, ui, storage, Clock.systemDefaultZone());
    }

    /**
     * Creates a command handler that obtains the current date and time from the specified clock.
     *
     * @param taskList task list to manage
     * @param commandParser parser for user commands
     * @param ui user interface for responses
     * @param storage persistent task storage
     * @param clock source of the current date and time
     */
    CommandHandler(TaskList taskList, CommandParser commandParser, MimiMeowUi ui, Storage storage, Clock clock) {
        this.taskList = taskList;
        this.commandParser = commandParser;
        this.ui = ui;
        this.storage = storage;
        this.clock = clock;
    }

    /**
     * Executes a command and returns whether MimiMeow should exit.
     *
     * @param userInput raw command entered by the user
     * @return true if the application should exit
     */
    public boolean execute(String userInput) {
        ui.showSeparator();
        try {
            Command command = commandParser.parse(userInput);
            switch (command.getWord()) {
            case "bye":
                ui.showGoodbyeMessage();
                ui.showSeparator();
                return true;
            case "list":
                ui.showTaskList(taskList);
                break;
            case "today":
                showTodayTasks();
                break;
            case "upcoming":
                showUpcomingTasks();
                break;
            case "overdue":
                showOverdueTasks();
                break;
            case "find":
                findTasks(command.getArguments());
                break;
            case "mark":
                updateTaskStatus(command.getArguments(), true);
                break;
            case "unmark":
                updateTaskStatus(command.getArguments(), false);
                break;
            case "todo":
                addTodo(command.getArguments());
                break;
            case "deadline":
                addDeadline(command.getArguments());
                break;
            case "event":
                addEvent(command.getArguments());
                break;
            case "delete":
                deleteTask(command.getArguments());
                break;
            default:
                throw new CommandException("Mimi does not recognise that command. Try list, today, upcoming, "
                        + "overdue, find, todo, deadline, event, mark, unmark, delete, or bye.");
            }
        } catch (MimiMeowException exception) {
            ui.showError(exception.getMessage());
        }
        ui.showSeparator();
        return false;
    }

    /** Creates and saves a todo from the supplied command arguments. */
    private void addTodo(String commandArguments) {
        String description = commandArguments.trim();
        addTaskAndReply(new Todo(description));
    }

    /** Displays scheduled tasks that occur on the current date. */
    private void showTodayTasks() {
        ui.showScheduledTasks(
                taskList.findTasksOccurringOn(LocalDate.now(clock)),
                "Here are the tasks scheduled for today:",
                "There are no tasks scheduled for today.");
    }

    /** Displays incomplete scheduled tasks that have not started or become due. */
    private void showUpcomingTasks() {
        ui.showScheduledTasks(
                taskList.findUpcomingTasks(LocalDateTime.now(clock)),
                "Here are your upcoming tasks:",
                "There are no upcoming tasks.");
    }

    /** Displays incomplete deadlines whose due times have passed. */
    private void showOverdueTasks() {
        ui.showScheduledTasks(
                taskList.findOverdueTasks(LocalDateTime.now(clock)),
                "Here are your overdue deadlines:",
                "There are no overdue deadlines.");
    }

    /** Parses, creates, and saves a deadline from the supplied command arguments. */

    private void findTasks(String keyword) {
        if (keyword.isBlank()) {
            throw new CommandException("Mimi needs a keyword to find tasks.");
        }
        ui.showMatchingTasks(taskList.findTasks(keyword));
    }


    private void addDeadline(String commandArguments) {
        String[] deadlineParts = commandArguments.split("\\s*/by\\s+", 2);
        if (deadlineParts.length < 2) {
            throw new CommandException("Mimi's calendar is puzzled. Try: deadline description /by date.");
        }
        String description = deadlineParts[0].trim();
        String deadlineDate = deadlineParts[1].trim();
        addTaskAndReply(new Deadline(description, deadlineDate));
    }

    /** Parses, creates, and saves an event from the supplied command arguments. */
    private void addEvent(String commandArguments) {
        String[] eventParts = commandArguments.split("\\s*/from\\s+", 2);
        if (eventParts.length < 2) {
            throw new CommandException(
                    "Mimi needs an event description, start, and end. Try: event description /from start /to end.");
        }
        String description = eventParts[0].trim();
        String[] timeParts = eventParts[1].split("\\s*/to\\s+", 2);
        if (timeParts.length < 2) {
            throw new CommandException("Mimi needs both /from and /to times before I can pounce on this event.");
        }
        String startTime = timeParts[0].trim();
        String endTime = timeParts[1].trim();
        addTaskAndReply(new Event(description, startTime, endTime));
    }

    /** Adds and saves a task, rolling back the addition if saving fails. */
    private void addTaskAndReply(Task task) {
        taskList.add(task);
        try {
            storage.save(taskList);
        } catch (StorageException exception) {
            taskList.delete(taskList.size() - 1);
            throw exception;
        }
        ui.showTaskAdded(task, taskList.size());
    }

    /** Updates and saves a task's completion status, restoring it if saving fails. */
    private void updateTaskStatus(String taskNumberText, boolean isDone) {
        if (taskNumberText.isEmpty()) {
            throw new CommandException("Mimi needs a task number, meow.");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new CommandException("Mimi can only chase a positive whole-number task index.");
        }
        if (taskNumber < 1 || taskNumber > taskList.size()) {
            throw new CommandException("Mimi cannot find task " + taskNumber + ". Check the task number");
        }
        Task task = taskList.get(taskNumber - 1);
        boolean wasDone = task.isDone();
        if (isDone) {
            task.setAsDone();
        } else {
            task.setAsNotDone();
        }
        try {
            storage.save(taskList);
        } catch (StorageException exception) {
            if (wasDone) {
                task.setAsDone();
            } else {
                task.setAsNotDone();
            }
            throw exception;
        }
        ui.showTaskStatus(task, isDone);
    }

    /** Deletes and saves a task, restoring it at its original position if saving fails. */
    private void deleteTask(String taskNumberText) {
        if (taskNumberText.isEmpty()) {
            throw new CommandException("Mimi needs a task number, meow.");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new CommandException("Mimi can only chase a positive whole-number task index.");
        }
        if (taskNumber < 1 || taskNumber > taskList.size()) {
            throw new CommandException("Mimi cannot find task " + taskNumber + ". Check the task number");
        }
        int taskIndex = taskNumber - 1;
        Task deletedTask = taskList.delete(taskIndex);
        try {
            storage.save(taskList);
        } catch (StorageException exception) {
            taskList.add(taskIndex, deletedTask);
            throw exception;
        }
        ui.showTaskDeleted(deletedTask, taskList.size());
    }
}

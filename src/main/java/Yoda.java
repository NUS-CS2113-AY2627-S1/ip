import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Runs the YODA command-line task manager.
 */
public class Yoda {

    private static final Storage STORAGE = new Storage(Paths.get(".", "data", "yoda.txt"));
    private static TaskList tasks = new TaskList();
    private static final Parser PARSER = new Parser();

    /**
     * Prevents instantiation of this static application entry-point class.
     */
    private Yoda() {
    }

    /**
     * Starts the YODA command-line application.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        tasks = STORAGE.load();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (input.isEmpty()) {
                continue;
            }

            ParsedCommand parsedCommand = PARSER.parse(input);

            ui.showDivider();

            try {
                handleCommand(parsedCommand.getCommand(), parsedCommand.getArguments(), ui);
            } catch (YodaException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showInvalidNumberError();
            } catch (DateTimeParseException e) {
                ui.showInvalidDateError();
            }

            ui.showDivider();
        }
    }

    /**
     * Performs the action requested by a parsed command.
     *
     * @param command the command word entered by the user
     * @param arguments the text after the command word
     * @param ui the interface used to display command-specific messages
     * @throws YodaException if the command or its arguments are invalid
     */
    private static void handleCommand(String command, String arguments, Ui ui) throws YodaException {
        switch (command) {
            case "bye":
                ui.showGoodbye();
                System.exit(0);
                break;
            case "list":
                handleList();
                break;
            case "mark":
                handleMark(arguments);
                STORAGE.save(tasks);
                break;
            case "unmark":
                handleUnmark(arguments);
                STORAGE.save(tasks);
                break;
            case "todo":
                handleTodo(arguments);
                STORAGE.save(tasks);
                break;
            case "deadline":
                handleDeadline(arguments);
                STORAGE.save(tasks);
                break;
            case "event":
                handleEvent(arguments);
                STORAGE.save(tasks);
                break;
            case "delete":
                handleDelete(arguments);
                STORAGE.save(tasks);
                break;
            case "find":
                handleFind(arguments);
                break;
            default:
                throw new YodaException("Unknown command: \"" + command + "\"\n"
                        + "  Available commands: todo, deadline, event, mark, unmark, delete, find, list, bye");
        }
    }

    /**
     * Displays every task in the current task list.
     *
     * @throws YodaException if the task list is empty
     */
    private static void handleList() throws YodaException {
        if (tasks.isEmpty()) {
            throw new YodaException("Your task list is empty. Add a task first!");
        }
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }
    }

    /**
     * Marks a task as completed.
     *
     * @param arguments the one-based task number
     * @throws YodaException if the task number is invalid
     */
    private static void handleMark(String arguments) throws YodaException {
        int taskIndex = tasks.getTaskIndex(arguments, "mark");
        tasks.get(taskIndex).markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(taskIndex));
    }

    /**
     * Marks a task as incomplete.
     *
     * @param arguments the one-based task number
     * @throws YodaException if the task number is invalid
     */
    private static void handleUnmark(String arguments) throws YodaException {
        int taskIndex = tasks.getTaskIndex(arguments, "unmark");
        tasks.get(taskIndex).markAsUndone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(taskIndex));
    }

    /**
     * Adds a to-do task.
     *
     * @param arguments the task description
     * @throws YodaException if the description is missing
     */
    private static void handleTodo(String arguments) throws YodaException {
        if (arguments.isEmpty()) {
            throw new YodaException("Missing description!\n"
                    + "  Correct format: todo <description>\n"
                    + "  Example:        todo Read a book");
        }
        tasks.add(new Todo(arguments));
        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds a deadline task using an ISO-formatted due date.
     *
     * @param arguments the deadline description and its /by date
     * @throws YodaException if the deadline fields are missing
     * @throws DateTimeParseException if the due date is not yyyy-MM-dd
     */
    private static void handleDeadline(String arguments) throws YodaException {
        if (arguments.isEmpty()) {
            throw new YodaException("Missing description and deadline!\n"
                    + "  Correct format: deadline <description> /by <yyyy-MM-dd>\n"
                    + "  Example:        deadline Submit report /by 2019-10-15");
        }
        if (!arguments.contains(" /by ")) {
            throw new YodaException("Missing '/by' field!\n"
                    + "  Correct format: deadline <description> /by <yyyy-MM-dd>\n"
                    + "  Example:        deadline Submit report /by 2019-10-15");
        }
        String[] deadlineParts = arguments.split(" /by ", 2);
        LocalDate dueDate = LocalDate.parse(deadlineParts[1]);
        tasks.add(new Deadline(deadlineParts[0], dueDate));
        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds an event task.
     *
     * @param arguments the event description and time range
     * @throws YodaException if the event fields are missing
     */
    private static void handleEvent(String arguments) throws YodaException {
        if (arguments.isEmpty()) {
            throw new YodaException("Missing description and event times!\n"
                    + "  Correct format: event <description> /from <start> /to <end>\n"
                    + "  Example:        event Team meeting /from Mon 2pm /to Mon 4pm");
        }
        if (!arguments.contains(" /from ")) {
            throw new YodaException("Missing '/from' field!\n"
                    + "  Correct format: event <description> /from <start> /to <end>\n"
                    + "  Example:        event Team meeting /from Mon 2pm /to Mon 4pm");
        }
        String[] eventParts = arguments.split(" /from ", 2);
        if (!eventParts[1].contains(" /to ")) {
            throw new YodaException("Missing '/to' field!\n"
                    + "  Correct format: event <description> /from <start> /to <end>\n"
                    + "  Example:        event Team meeting /from Mon 2pm /to Mon 4pm");
        }
        String description = eventParts[0];
        String[] timeParts = eventParts[1].split(" /to ", 2);
        tasks.add(new Event(description, timeParts[0], timeParts[1]));
        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Removes a task from the list.
     *
     * @param arguments the one-based task number
     * @throws YodaException if the task number is invalid
     */
    private static void handleDelete(String arguments) throws YodaException {
        if (arguments.isEmpty()) {
            throw new YodaException("Missing task number!\n"
                    + "  Correct format: delete <number>\n"
                    + "  Example:        delete 2");
        }
        int taskIndex = tasks.getTaskIndex(arguments, "delete");
        Task removed = tasks.remove(taskIndex);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Displays the tasks whose descriptions contain a search keyword.
     *
     * @param keyword the text to search for
     * @throws YodaException if no keyword was supplied
     */
    private static void handleFind(String keyword) throws YodaException {
        if (keyword.isEmpty()) {
            throw new YodaException("Missing search keyword!\n"
                    + "  Correct format: find <keyword>\n"
                    + "  Example:        find book");
        }

        System.out.println("Here are the matching tasks in your list:");
        int matchNumber = 1;
        for (Task task : tasks.find(keyword)) {
            System.out.println(matchNumber + "." + task);
            matchNumber++;
        }
    }

    /**
     * Displays a confirmation that a task was added.
     *
     * @param task the added task
     * @param taskCount the number of tasks now in the list
     */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

}

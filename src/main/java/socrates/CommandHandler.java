package socrates;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Dispatches user commands to the appropriate task operation, delegating
 * output to a {@link Ui} and state changes to a {@link TaskList}.
 */
public class CommandHandler {
    private final TaskList taskList;
    private final Ui ui;

    /**
     * Creates a command handler operating on the given task list and UI.
     *
     * @param taskList the task list to read from and modify
     * @param ui the UI to print responses through
     */
    public CommandHandler(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
    }

    /**
     * Splits a raw line of user input into a command word and the rest
     * of the line (if any).
     *
     * @param s the raw input line
     * @return an array of length 1 or 2: the command word, and optionally the remainder
     */
    public static String[] formatInput(String s) {
        return s.strip().split(" ", 2);
    }

    /**
     * Creates a {@link ToDo} from its description.
     */
    public static ToDo buildToDo(String description) {
        return new ToDo(description);
    }

    /**
     * Creates a {@link Deadlines} task, parsing the given date string.
     *
     * @throws SocratesException if {@code by} is not a valid yyyy-MM-dd date
     */
    public static Deadlines buildDeadlines(String description, String by) throws SocratesException {
        try {
            return new Deadlines(description, LocalDate.parse(by.trim()));
        } catch (DateTimeParseException e) {
            throw new SocratesException("Please give the date as yyyy-MM-dd, e.g. 2026-10-15");
        }
    }

    /**
     * Creates an {@link Events} task, parsing the given start/end date strings.
     *
     * @throws SocratesException if {@code from} or {@code to} is not a valid yyyy-MM-dd date
     */
    public static Events buildEvents(String descriptions, String from, String to) throws SocratesException {
        try {
            return new Events(descriptions, LocalDate.parse(from.trim()), LocalDate.parse(to.trim()));
        } catch (DateTimeParseException e) {
            throw new SocratesException("Please give the dates as yyyy-MM-dd, e.g. 2026-10-15");
        }
    }

    private void handleList() {
        if (taskList.isEmpty()) {
            ui.printMessage("List is empty");
        } else {
            String concatenatedString = "";
            int n = 0;
            for (Task task : taskList.asArrayList()) {
                concatenatedString = String.format("%s \t %d.%s\n",
                        concatenatedString,
                        ++n,
                        task.getStatusLine()
                );
            }
            ui.printMessage(concatenatedString);
        }
    }

    private void handleMark(String[] input) {
        int markIdx = Integer.parseInt(input[1]) - 1;
        taskList.get(markIdx).markAsDone();
        ui.printMessage("\t Nice! I've marked this task as done:\n" +
                "\t   " +
                taskList.get(markIdx).getStatusLine()
        );
    }

    private void handleUnmark(String[] input) {
        int unmarkIdx = Integer.parseInt(input[1]) - 1;
        taskList.get(unmarkIdx).markAsNotDone();
        ui.printMessage("\t Okay, I've marked this task as not done yet:\n" +
                "\t   " +
                taskList.get(unmarkIdx).getStatusLine()
        );
    }

    private void printAddedTask(Task task, int n) {
        ui.printMessage(String.format("Got it. Ive added this task:\n\t   " +
                task.getStatusLine() +
                "\n\t Now you have %d tasks in the list", n
        ));
    }

    private void handleToDo(String[] input) throws SocratesException {
        try {
            ToDo temp = buildToDo(input[1]);
            taskList.add(temp);
            printAddedTask(temp, taskList.size());
        } catch (IndexOutOfBoundsException e) {
            throw new SocratesException("Task not found, use case: todo {task}");
        }
    }

    private void handleDeadlines(String[] input) throws SocratesException {
        String[] formattedDescription = input[1].split(" /by ");
        if (formattedDescription.length == 1) {
            throw new SocratesException("Deadline not found, use case: deadline {task} /by {deadline}");
        }
        Deadlines temp = buildDeadlines(formattedDescription[0], formattedDescription[1]);
        taskList.add(temp);
        printAddedTask(temp, taskList.size());
    }

    private void handleEvents(String[] input) throws SocratesException {
        String[] formattedDescription = input[1].split(" /from ");
        if (formattedDescription.length == 1) {
            throw new SocratesException("Event timings not found, use case: event {event description} /from {start date and time} /to {end date and time}");
        }
        String[] dateRange = formattedDescription[1].split(" /to ");
        if (dateRange.length == 1) {
            throw new SocratesException("Event timings not found, use case: event {event description} /from {start date and time} /to {end date and time}");
        }
        Events temp = buildEvents(formattedDescription[0], dateRange[0], dateRange[1]);
        taskList.add(temp);
        printAddedTask(temp, taskList.size());
    }

    private void handleDelete(String[] input) throws SocratesException {
        int n;
        try {
            n = Integer.parseInt(input[1]) - 1;
        } catch (NumberFormatException e) {
            throw new SocratesException("Use case: delete {index}");
        }
        if (n >= taskList.size() || n < 0) {
            throw new SocratesException("Index out of bounds");
        }

        ui.printMessage("Noted. I've removed this task:\n\t   " +
                taskList.get(n).getStatusLine() +
                "\n\t now you have " +
                (taskList.size() - 1) +
                " tasks in the list");
        taskList.remove(n);
    }

    private void handleFind(String[] input) throws SocratesException {
        if (input.length < 2 || input[1].isBlank()) {
            throw new SocratesException("Use case: find {keyword}");
        }
        String keyword = input[1].toLowerCase();
        String concatenatedString = "";
        int n = 0;
        for (Task task : taskList.asArrayList()) {
            if (task.getDescription().toLowerCase().contains(keyword)) {
                concatenatedString = String.format("%s \t %d.%s\n",
                        concatenatedString,
                        ++n,
                        task.getStatusLine()
                );
            }
        }
        if (n == 0) {
            ui.printMessage("No matching tasks found, my friend.");
        } else {
            ui.printMessage("Here are the matching tasks in your list:\n" + concatenatedString);
        }
    }

    private void handleHelp() {
        ui.printMessage("As I always say, wisdom begins with knowing what you can ask. Here is what I can help you with:\n" +
                "\t list - view all your tasks\n" +
                "\t todo {task} - add a simple task\n" +
                "\t deadline {task} /by {yyyy-MM-dd} - add a task with a deadline\n" +
                "\t event {task} /from {yyyy-MM-dd} /to {yyyy-MM-dd} - add an event\n" +
                "\t mark {index} - mark a task as done\n" +
                "\t unmark {index} - mark a task as not done\n" +
                "\t delete {index} - remove a task\n" +
                "\t find {keyword} - find tasks matching a keyword\n" +
                "\t bye - end our conversation"
        );
    }

    /**
     * Executes the command named by {@code input[0]}, using {@code input[1]}
     * (if present) as its argument. Prints an unknown-command message if the
     * command word isn't recognized.
     *
     * @throws SocratesException if the command's arguments are invalid
     */
    public void handleInput(String[] input) throws SocratesException {
        switch (input[0]) {
            case "list" -> handleList();
            case "mark" -> handleMark(input);
            case "unmark" -> handleUnmark(input);
            case "todo" -> handleToDo(input);
            case "deadline" -> handleDeadlines(input);
            case "event" -> handleEvents(input);
            case "delete" -> handleDelete(input);
            case "find" -> handleFind(input);
            case "help" -> handleHelp();
            default -> ui.printMessage("I confess, I do not understand that, my friend. Use 'help' to see what I can offer.");
        }
    }
}

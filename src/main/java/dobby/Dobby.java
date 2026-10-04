package dobby;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import dobby.exception.DobbyException;
import dobby.task.Deadline;
import dobby.task.DateTimeParser;
import dobby.task.Event;
import dobby.task.Task;
import dobby.task.Todo;

/**
 * A simple command-line chatbot that keeps the user's tasks in memory.
 *
 * <p>The tasks are intentionally not written to disk. They last only for the
 * current run of Dobby, just like notes written on a magical temporary scroll.
 */
public class Dobby {

    /** The maximum number of tasks that Dobby can remember in one session. */
    private static final int MAX_TASKS = 100;

    /** The line printed between Dobby's messages. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Creates a Dobby chatbot instance. */
    public Dobby() {
    }

    /**
     * Displays the greeting, handles commands, and exits when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        printWelcome();
        runCommandLoop();
    }

    /**
     * Prints Dobby's welcome banner and opening message.
     */
    private static void printWelcome() {
        String banner = "     *        .        *        .        *\n"
                + "      ____          _      _\n"
                + "     |  _ \\   ___  | |__  | |__   _   _\n"
                + "     | | | | / _ \\ | '_ \\ | '_ \\ | | | |\n"
                + "     | |_| || (_) || |_) || |_) || |_| |\n"
                + "     |____/  \\___/ |_.__/ |_.__/  \\__, |\n"
                + "                                  |___/\n"
                + "     .        *        .        *        .";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm Dobby, your mildly magical command goblin.");
        System.out.println("What adventure shall we get into today?");
    }

    /**
     * Reads commands and maintains the task list for the current session.
     */
    private static void runCommandLoop() {
        ArrayList<Task> tasks = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                printMessage("Bye for now! Dobby is off to polish the quest scrolls. Stay mighty!");
                return;
            }

            processCommand(command, tasks);
        }
    }

    /**
     * Processes one command and updates the stored tasks when necessary.
     *
     * @param command the complete command entered by the user.
     * @param tasks the in-memory task list.
     */
    private static void processCommand(String command, ArrayList<Task> tasks) {
        try {
            if (command.equals("list")) {
                printTaskList(tasks);
                return;
            }

            if (command.equals("find") || command.startsWith("find ")) {
                printMatchingTasks(command, tasks);
                return;
            }

            if (command.equals("on") || command.startsWith("on ")) {
                printTasksOnDate(command, tasks);
                return;
            }

            if (command.equals("delete") || command.startsWith("delete ")) {
                deleteTask(command, tasks);
                return;
            }

            if (command.equals("mark") || command.startsWith("mark ")) {
                markTask(command, tasks);
                return;
            }

            if (command.equals("unmark") || command.startsWith("unmark ")) {
                unmarkTask(command, tasks);
                return;
            }

            Task task = parseTask(command);
            addTask(task, tasks);
        } catch (DobbyException exception) {
            printMessage(exception.getMessage());
        }
    }

    /**
     * Adds a parsed task when the task pouch still has room.
     *
     * @param task the parsed task to add.
     * @param tasks the in-memory task list.
     */
    private static void addTask(Task task, ArrayList<Task> tasks) throws DobbyException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DobbyException("Your task pouch is full at 100 quests. Start a new Dobby session before "
                    + "adding another quest.");
        }

        tasks.add(task);

        printTaskAdded(task, tasks.size());
    }

    /**
     * Prints one response surrounded by the standard separator.
     *
     * @param message the response to print.
     */
    private static void printMessage(String message) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     " + message);
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Prints all stored tasks in the order in which they were entered.
     *
     * @param tasks the in-memory task list.
     */
    private static void printTaskList(ArrayList<Task> tasks) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     Behold, brave adventurer! Here are your mighty quests:");

        if (tasks.isEmpty()) {
            System.out.println("     Your task pouch is empty - add a quest and let the adventure begin!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
            }
        }

        System.out.println("    " + SEPARATOR);
    }

    /**
     * Prints tasks whose descriptions contain the requested keyword.
     *
     * @param command the complete {@code find <keyword>} command.
     * @param tasks the in-memory task list.
     * @throws DobbyException when the keyword is missing.
     */
    private static void printMatchingTasks(String command, ArrayList<Task> tasks) throws DobbyException {
        String keyword = command.substring("find".length()).trim();

        if (keyword.isEmpty()) {
            throw new DobbyException("The finding spell needs a keyword. Try find <keyword>, for example find book.");
        }

        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        boolean hasMatchingTask = false;

        System.out.println("    " + SEPARATOR);
        System.out.println("     Aha! I found these quests hiding in your magical task scroll:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (matchesKeyword(task, normalizedKeyword)) {
                System.out.println("     " + (i + 1) + "." + task);
                hasMatchingTask = true;
            }
        }

        if (!hasMatchingTask) {
            System.out.println("     No quests matched that clue. Try another keyword, brave adventurer!");
        }

        System.out.println("    " + SEPARATOR);
    }

    /**
     * Returns whether a task description contains the supplied keyword, ignoring letter case.
     *
     * @param task the task whose description is searched.
     * @param normalizedKeyword the lower-case keyword to search for.
     * @return true when the task description contains the keyword.
     */
    private static boolean matchesKeyword(Task task, String normalizedKeyword) {
        return task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword);
    }

    /**
     * Prints deadlines and events that occur on the requested date.
     *
     * @param command the complete {@code on <date>} command.
     * @param tasks the in-memory task list.
     * @throws DobbyException when the requested date is missing or malformed.
     */
    private static void printTasksOnDate(String command, ArrayList<Task> tasks) throws DobbyException {
        String dateText = command.substring("on".length()).trim();
        LocalDate date = DateTimeParser.parseDate(dateText);

        if (date == null) {
            throw new DobbyException("The on command needs a date in yyyy-MM-dd format, for example on 2019-10-15.");
        }

        System.out.println("    " + SEPARATOR);
        System.out.println("     Quests scheduled for " + DateTimeParser.formatDate(date) + ":");

        boolean hasMatchingTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (occursOnDate(tasks.get(i), date)) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
                hasMatchingTask = true;
            }
        }

        if (!hasMatchingTask) {
            System.out.println("     No deadlines or events are scheduled for this date.");
        }

        System.out.println("    " + SEPARATOR);
    }

    /**
     * Returns whether a deadline or event occurs on the supplied date.
     *
     * @param task the task to check.
     * @param date the date being queried.
     * @return whether the task occurs on the date.
     */
    private static boolean occursOnDate(Task task, LocalDate date) {
        if (task instanceof Deadline deadline) {
            return date.equals(deadline.getDate());
        }

        if (task instanceof Event event) {
            return occursOnDate(event, date);
        }

        return false;
    }

    /**
     * Returns whether an event's inclusive date range contains the supplied date.
     *
     * @param event the event to check.
     * @param date the date being queried.
     * @return whether the event occurs on the date.
     */
    private static boolean occursOnDate(Event event, LocalDate date) {
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();

        return startDate != null && endDate != null
                && !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Removes the task selected by a {@code delete <number>} command.
     *
     * @param command the complete command entered by the user.
     * @param tasks the in-memory task list.
     * @throws DobbyException when the selected task number is missing, malformed, or out of range.
     */
    private static void deleteTask(String command, ArrayList<Task> tasks) throws DobbyException {
        int taskIndex = parseTaskIndex(command, "delete", tasks.size());
        Task removedTask = tasks.remove(taskIndex);
        String questLabel = tasks.size() == 1 ? "quest" : "quests";

        System.out.println("    " + SEPARATOR);
        System.out.println("     Poof! This quest has vanished from the magical task scroll:");
        System.out.println("       " + removedTask);
        System.out.println("     The quest scroll now holds " + tasks.size() + " " + questLabel
                + ". Onward, brave adventurer!");
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Marks the task selected by a {@code mark <number>} command as done.
     *
     * @param command the complete command entered by the user.
     * @param tasks the in-memory task list.
     */
    private static void markTask(String command, ArrayList<Task> tasks) throws DobbyException {
        int taskIndex = parseTaskIndex(command, "mark", tasks.size());
        tasks.get(taskIndex).markAsDone();

        System.out.println("    " + SEPARATOR);
        System.out.println("     Nice! Quest progress unlocked - I've marked this task as done:");
        System.out.println("       " + tasks.get(taskIndex));
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Reverses the done status of the task selected by an {@code unmark <number>} command.
     *
     * @param command the complete command entered by the user.
     * @param tasks the in-memory task list.
     */
    private static void unmarkTask(String command, ArrayList<Task> tasks) throws DobbyException {
        int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
        tasks.get(taskIndex).markAsUndone();

        System.out.println("    " + SEPARATOR);
        System.out.println("     Plot twist! This quest is back on the adventure board -");
        System.out.println("     I've marked this task as not done yet:");
        System.out.println("       " + tasks.get(taskIndex));
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Parses and validates the task number in a task-selection command.
     *
     * @param command the complete command entered by the user.
     * @param action the command name being validated.
     * @param taskCount the number of tasks currently stored.
     * @return the zero-based index selected by the command.
     * @throws DobbyException when the number is missing, malformed, or out of range.
     */
    private static int parseTaskIndex(String command, String action, int taskCount) throws DobbyException {
        String taskNumberText = command.substring(action.length()).trim();
        int taskNumber;

        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new DobbyException("The " + action + " command needs a task number. Use " + action
                    + " <number>, for example " + action + " 1.");
        }

        if (taskCount == 0) {
            throw new DobbyException("There are no tasks to " + action + ". Add one with todo <description> first.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new DobbyException("Task " + taskNumber + " does not exist. Choose a number from 1 to "
                    + taskCount + ".");
        }

        return taskNumber - 1;
    }

    /**
     * Creates a task from a user command.
     *
     * <p>Explicit commands use {@code todo}, {@code deadline}, and
     * {@code event}. Other input is rejected so Dobby can explain the supported
     * command formats instead of silently treating a typo as a task.
     *
     * @param command the complete command entered by the user.
     * @return the parsed task.
     * @throws DobbyException when the command is unknown or malformed.
     */
    private static Task parseTask(String command) throws DobbyException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return parseToDo(command);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            return parseEvent(command);
        }

        throw new DobbyException(
                "I don't know that command. Try list, todo <description>, deadline <description> /by <date/time>, "
                        + "event <description> /from <start> /to <end>, mark <number>, unmark <number>, "
                        + "delete <number>, on <date>, find <keyword>, or bye.");
    }

    /**
     * Parses an explicit ToDo command.
     *
     * @param command the complete ToDo command.
     * @return the parsed ToDo.
     * @throws DobbyException when the ToDo description is empty.
     */
    private static Task parseToDo(String command) throws DobbyException {
        String description = command.substring("todo".length()).trim();

        if (description.isEmpty()) {
            throw new DobbyException("A ToDo needs a description. Use todo <description>, for example todo read book.");
        }

        return new Todo(description);
    }

    /**
     * Parses a deadline command using the {@code /by} separator.
     *
     * @param command the complete deadline command.
     * @return the parsed deadline.
     * @throws DobbyException when the description or due time is missing.
     */
    private static Task parseDeadline(String command) throws DobbyException {
        String taskDetails = command.substring("deadline".length()).trim();
        int byIndex = taskDetails.lastIndexOf(" /by ");

        if (byIndex < 0) {
            throw new DobbyException("A deadline needs a description and a due time. Use deadline <description> "
                    + "/by <date/time>.");
        }

        if (byIndex == 0) {
            throw new DobbyException("A deadline needs a description before /by. Add text before the /by marker.");
        }

        String description = taskDetails.substring(0, byIndex).trim();
        String deadline = taskDetails.substring(byIndex + " /by ".length()).trim();

        if (deadline.isEmpty()) {
            throw new DobbyException("A deadline needs a due time after /by. For example: deadline report /by Friday.");
        }

        return new Deadline(description, deadline);
    }

    /**
     * Parses an event command using the {@code /from} and {@code /to} separators.
     *
     * @param command the complete event command.
     * @return the parsed event.
     * @throws DobbyException when an event detail is missing.
     */
    private static Task parseEvent(String command) throws DobbyException {
        String taskDetails = command.substring("event".length()).trim();
        int fromIndex = taskDetails.indexOf(" /from ");
        int toIndex = fromIndex < 0 ? -1 : taskDetails.indexOf(" /to ", fromIndex + " /from ".length());

        if (taskDetails.isEmpty()) {
            throw new DobbyException("An event needs a description, start time, and end time. Use event <description> "
                    + "/from <start> /to <end>.");
        }

        if (fromIndex < 0) {
            throw new DobbyException("An event needs a start time after /from and an end time after /to. Use event "
                    + "<description> /from <start> /to <end>.");
        }

        if (fromIndex == 0) {
            throw new DobbyException("An event needs a description before /from. Add text before the /from marker.");
        }

        if (toIndex < 0) {
            throw new DobbyException("An event needs an end time after /to. Add /to <end> after the start time.");
        }

        String description = taskDetails.substring(0, fromIndex).trim();
        String start = taskDetails.substring(fromIndex + " /from ".length(), toIndex).trim();
        String end = taskDetails.substring(toIndex + " /to ".length()).trim();

        if (start.isEmpty()) {
            throw new DobbyException("An event needs a start time after /from. Add a start time before /to.");
        }

        if (end.isEmpty()) {
            throw new DobbyException("An event needs an end time after /to. For example: event meeting /from 2pm "
                    + "/to 4pm.");
        }

        return new Event(description, start, end);
    }

    /**
     * Prints the playful quest confirmation used by explicit typed-task commands.
     *
     * @param task the task that was added.
     * @param taskCount the number of tasks now stored.
     */
    private static void printTaskAdded(Task task, int taskCount) {
        String questLabel = taskCount == 1 ? "quest" : "quests";

        System.out.println("    " + SEPARATOR);
        System.out.println("     Huzzah! A new quest has joined your magical task scroll:");
        System.out.println("       " + task);
        System.out.println("     The quest scroll now holds " + taskCount + " " + questLabel
                + ". Keep adventuring!");
        System.out.println("    " + SEPARATOR);
    }

}

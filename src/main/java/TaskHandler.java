import command.CommandExecutor;
import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.ToDo;

/**
 * Handles user commands and stores tasks created during the session.
 */
public class TaskHandler implements CommandExecutor {
    private TaskList tasks = new TaskList();

    /**
     * Loads saved tasks from disk when a data file exists.
     *
     * @throws AthenaException If the saved tasks cannot be loaded.
     */
    public void loadTasks() {
        tasks = Storage.loadTasks();
    }

    /**
     * Marks the selected task as done.
     *
     * @param line Complete mark command containing the task number.
     * @throws AthenaException If the task number is invalid or the change cannot be saved.
     */
    @Override
    public void mark(String line) {
        Task task = getTask(line);
        boolean wasDone = task.isDone();
        task.setDone(true);
        try {
            saveTasks();
        } catch (AthenaException exception) {
            task.setDone(wasDone);
            throw exception;
        }
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Marks the selected task as not done.
     *
     * @param line Complete unmark command containing the task number.
     * @throws AthenaException If the task number is invalid or the change cannot be saved.
     */
    @Override
    public void unmark(String line) {
        Task task = getTask(line);
        boolean wasDone = task.isDone();
        task.setDone(false);
        try {
            saveTasks();
        } catch (AthenaException exception) {
            task.setDone(wasDone);
            throw exception;
        }
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Deletes the selected task from the task list.
     *
     * @param line Complete delete command containing the task number.
     * @throws AthenaException If the task number is invalid or the change cannot be saved.
     */
    @Override
    public void delete(String line) {
        int taskNumber = getTaskNumberForDelete(line);
        Task deletedTask = tasks.remove(taskNumber - 1);
        try {
            saveTasks();
        } catch (AthenaException exception) {
            tasks.add(taskNumber - 1, deletedTask);
            throw exception;
        }

        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + deletedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Finds tasks whose descriptions contain the supplied keyword.
     *
     * @param line Complete find command containing the keyword.
     * @throws AthenaException If no keyword is provided.
     */
    @Override
    public void find(String line) {
        String keyword = line.substring("find".length()).trim();
        if (keyword.isBlank()) {
            throw new AthenaException("Please enter a keyword after find");
        }

        System.out.println(" Here are the matching tasks in your list:");
        int matchNumber = 1;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                System.out.println(" " + matchNumber + "." + task);
                matchNumber++;
            }
        }
    }

    /**
     * Lists all stored tasks after validating the complete command.
     *
     * @param line Complete list command.
     * @throws AthenaException If the command contains additional arguments.
     */
    @Override
    public void list(String line) {
        if (!line.equals("list")) {
            throw new AthenaException("The list command does not take additional arguments");
        }
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Adds a todo task.
     *
     * @param line Description of the todo task.
     * @throws AthenaException If the description is blank or the task cannot be saved.
     */
    public void addTodo(String line) {
        if (line.isBlank()) {
            throw new AthenaException(" Please tell me what todo task to add");
        }
        addTask(new ToDo(line));
    }

    /**
     * Adds a deadline task from a complete command.
     *
     * @param line Complete deadline command containing a description and due time.
     * @throws AthenaException If the command is incomplete or the task cannot be saved.
     */
    public void addDeadline(String line) {
        String content = line.substring("deadline".length()).trim();
        if (content.isBlank()) {
            throw new AthenaException("Please tell me what deadline task to add");
        }
        int byPosition = findMarker(content, "/by", 0);
        if (byPosition < 0) {
            throw new AthenaException("Invalid syntax. Use: deadline DESCRIPTION /by DATE");
        }
        String description = content.substring(0, byPosition).trim();
        String by = content.substring(byPosition + "/by".length()).trim();
        if (description.isBlank()) {
            throw new AthenaException("Please tell me what deadline task to add");
        }
        if (by.isBlank()) {
            throw new AthenaException("Please add a date for the deadline");
        }
        addTask(new Deadline(description, by));
    }

    /**
     * Adds an event task from a complete command.
     *
     * @param line Complete event command containing a description, start time, and end time.
     * @throws AthenaException If the command is incomplete or the task cannot be saved.
     */
    public void addEvent(String line) {
        String content = line.substring("event".length()).trim();
        if (content.isBlank()) {
            throw new AthenaException("Please tell me what event task to add");
        }
        int fromPosition = findMarker(content, "/from", 0);
        if (fromPosition < 0) {
            throw new AthenaException("Invalid syntax. Use: event DESCRIPTION /from START /to END");
        }
        int toPosition = findMarker(content, "/to", fromPosition + "/from".length());
        if (toPosition < 0) {
            throw new AthenaException("Invalid syntax. Use: event DESCRIPTION /from START /to END");
        }
        String description = content.substring(0, fromPosition).trim();
        String from = content.substring(fromPosition + "/from".length(), toPosition).trim();
        String to = content.substring(toPosition + "/to".length()).trim();
        if (description.isBlank()) {
            throw new AthenaException("Please tell me what event task to add");
        }
        if (from.isBlank()) {
            throw new AthenaException("Please add a start time for the event");
        }
        if (to.isBlank()) {
            throw new AthenaException("Please add an end time for the event");
        }
        addTask(new Event(description, from, to));
    }

    /** Returns the task selected by a command containing a one-based task number. */
    /**
     * Returns the task selected by a command containing a one-based task number.
     *
     * @param line Complete command containing the task number.
     * @return Selected task.
     * @throws AthenaException If the task number is missing, invalid, or outside the task list.
     */
    private Task getTask(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new AthenaException("Please enter exactly one numeric task number");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AthenaException("Task number is too large", exception);
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AthenaException("Task does not exist");
        }
        return tasks.get(taskNumber - 1);
    }

    /** Returns the validated one-based task number from a delete command. */
    /**
     * Returns the validated one-based task number from a delete command.
     *
     * @param line Complete delete command containing the task number.
     * @return Validated one-based task number.
     * @throws AthenaException If the task number is missing, invalid, or outside the task list.
     */
    private int getTaskNumberForDelete(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length != 2 || !parts[1].matches("-?\\d+")) {
            throw new AthenaException("Please enter a valid task number after delete");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AthenaException("Please enter a valid task number after delete");
        }

        if (taskNumber <= 0) {
            throw new AthenaException("Task number must be more than 0");
        }
        if (taskNumber > tasks.size()) {
            throw new AthenaException("Task number " + taskNumber + " does not exist in the list");
        }
        return taskNumber;
    }

    /** Returns the position of a command marker that is surrounded by text boundaries. */
    /**
     * Returns the position of a command marker surrounded by text boundaries.
     *
     * @param text Text to search.
     * @param marker Command marker to find.
     * @param fromIndex Index at which to start searching.
     * @return Marker position, or {@code -1} if no bounded marker is found.
     */
    private static int findMarker(String text, String marker, int fromIndex) {
        int position = text.indexOf(marker, fromIndex);
        while (position >= 0) {
            int endPosition = position + marker.length();
            boolean hasStartBoundary = position == 0 || Character.isWhitespace(text.charAt(position - 1));
            boolean hasEndBoundary = endPosition == text.length()
                    || Character.isWhitespace(text.charAt(endPosition));
            if (hasStartBoundary && hasEndBoundary) {
                return position;
            }
            position = text.indexOf(marker, position + 1);
        }
        return -1;
    }

    /** Adds and saves a task, rolling back the addition if persistence fails. */
    /**
     * Adds and saves a task, rolling back the addition if saving fails.
     *
     * @param task Task to add.
     * @throws AthenaException If the updated task list cannot be saved.
     */
    private void addTask(Task task) {
        tasks.add(task);
        try {
            saveTasks();
        } catch (AthenaException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Saves the current task list to persistent storage. */
    /**
     * Saves the current task list to persistent storage.
     *
     * @throws AthenaException If the task list cannot be saved.
     */
    private void saveTasks() {
        Storage.saveTasks(tasks);
    }

    /**
     * Adds the task described by a complete add command.
     *
     * @param line Complete todo, deadline, or event command.
     * @throws AthenaException If the command is incomplete or the task cannot be saved.
     * @throws IllegalArgumentException If the command uses an unsupported task type.
     */
    @Override
    public void add(String line) {
        String commandWord = line.split("\\s+", 2)[0];
        switch (commandWord) {
            case "todo":
                addTodo(line.substring("todo".length()).trim());
                break;
            case "deadline":
                addDeadline(line);
                break;
            case "event":
                addEvent(line);
                break;
            default:
                throw new IllegalArgumentException("Unsupported add command: " + commandWord);
        }
    }
}

package nga;

import java.util.List;

/** Executes commands against the task list without reading console input. */
public class NgaCommandHandler {
    private static final String HELP = " NGA means No Goobers Allowed.\n\n"
            + " Available commands:\n"
            + "   list\n       Lists all tasks.\n"
            + "   find <keyword>\n       Finds tasks whose descriptions contain the keyword.\n"
            + "   todo <description>\n       Adds a todo task.\n"
            + "   deadline <description> /by <date or time>\n       Adds a deadline. Accepted formats: yyyy-MM-dd,\n"
            + "       d/M/yyyy HHmm, yyyy-MM-dd HHmm, or yyyy-MM-dd HH:mm.\n"
            + "   event <description> /from <start> /to <end>\n       Adds an event.\n"
            + "   mark <task number>\n       Marks a task as done.\n"
            + "   unmark <task number>\n       Marks a task as not done.\n"
            + "   delete <task number>\n       Deletes a task.\n"
            + "   delete all\n       Deletes all tasks.\n"
            + "   clear\n       Deletes all tasks.\n"
            + "   help\n       Shows this command guide.\n"
            + "   bye\n       Exits Nga.\n"
            + "\n Input rules:\n"
            + "   Use spaces to separate commands and their arguments.\n"
            + "   The | character is not allowed in task fields.\n"
            + "\n Tasks are saved automatically in data/nga.txt.\n"
            + "\n To build the executable JAR, run:\n"
            + "   gradle shadowJar\n"
            + "\n To run the JAR from the project root, run:\n"
            + "   java -jar build/libs/nga-all.jar";
    private static final String UNKNOWN_COMMAND =
            " Start command with a prefix/header. Use help for available commands and format.";
    private static final String TAB_DELIMITER_ERROR =
            " Please use spaces as delimiters between commands and their arguments.";
    private final TaskList taskList;
    private final Storage storage;
    private final int invalidTaskCount;
    private boolean hasShownStorageWarning;

    /**
     * Creates a handler and restores saved tasks from storage when possible.
     */
    public NgaCommandHandler() {
        taskList = new TaskList();
        storage = new Storage();
        List<Task> savedTasks = storage.load();
        invalidTaskCount = storage.getInvalidTaskCount();
        for (Task task : savedTasks) {
            if (!taskList.isFull()) {
                taskList.add(task);
            }
        }
    }

    /**
     * Executes one command and returns text for the UI to display.
     *
     * @param input the raw command entered by the user
     * @return the response message and whether the application should exit
     * @throws TaskParseException if a task command has invalid date or field syntax
     */
    public CommandResult handle(String input) throws TaskParseException {
        if (input != null && input.contains("\t")) {
            return withStorageWarning(new CommandResult(false, TAB_DELIMITER_ERROR));
        }
        ParsedCommand command = CommandParser.parse(input);
        CommandResult result = switch (command.keyword()) {
        case "" -> new CommandResult(false, " Please enter something.");
        case "bye" -> new ExitCommand().execute(taskList, storage);
        case "list" -> new CommandResult(false, formatTasks());
        case "find" -> findTasks(command.arguments());
        case "help" -> new CommandResult(false, HELP);
        case "mark" -> updateTask(command.arguments(), true);
        case "unmark" -> updateTask(command.arguments(), false);
        case "delete" -> deleteCommand(command.arguments());
        case "clear" -> clearTasks(command.arguments());
        case "todo", "deadline", "event" -> addTask(command);
        default -> new CommandResult(false, UNKNOWN_COMMAND);
        };
        return withStorageWarning(result);
    }

    /**
     * Adds the storage warning to the first response when invalid rows were skipped during startup.
     *
     * @param result the response for the current command
     * @return the response with a one-time storage warning when needed
     */
    private CommandResult withStorageWarning(CommandResult result) {
        if (invalidTaskCount == 0 || hasShownStorageWarning) {
            return result;
        }
        hasShownStorageWarning = true;
        String rowLabel = invalidTaskCount == 1 ? "row" : "rows";
        String warning = " Warning: I skipped " + invalidTaskCount
                + " invalid task " + rowLabel + " while loading data/nga.txt.";
        return new CommandResult(result.shouldExit(), warning + "\n" + result.message());
    }

    /**
     * Creates and stores a task from a parsed task command.
     *
     * @param command the parsed task command
     * @return the response describing the addition or the validation failure
     * @throws TaskParseException if the task fields are invalid
     */
    private CommandResult addTask(ParsedCommand command) throws TaskParseException {
        Task task = TaskParser.createTask(command.keyword(), command.arguments());
        int duplicateTaskNumber = taskList.findDuplicateTaskNumber(task);
        if (duplicateTaskNumber != -1) {
            return new CommandResult(false, " This task already exists at index " + duplicateTaskNumber
                    + ":\n   " + taskList.getTask(duplicateTaskNumber));
        }
        if (taskList.isFull()) {
            return new CommandResult(false, " The task list is full.");
        }
        taskList.add(task);
        return savedResult(addedTaskMessage(task));
    }

    /**
     * Builds the response shown after a task is added.
     *
     * @param task the newly added task
     * @return the response containing the task and current list size
     */
    private CommandResult addedTaskMessage(Task task) {
        return new CommandResult(false, " Got it. I've added this task:\n   " + task
                + "\n Now you have " + taskList.size() + " tasks in the list.");
    }

    /**
     * Marks or unmarks a task selected by its one-based task number.
     *
     * @param argument the user-provided task number
     * @param isDone whether the task should be marked done
     * @return the response describing the update or validation failure
     */
    private CommandResult updateTask(String argument, boolean isDone) {
        try {
            int taskNumber = Integer.parseInt(argument);
            if (!taskList.hasTaskNumber(taskNumber)) {
                return new CommandResult(false, " That task number does not exist.");
            }
            Task task = taskList.getTask(taskNumber);
            if (isDone) {
                task.markAsDone();
            } else {
                task.markAsUndone();
            }
            String status = isDone ? " marked as done:" : " marked as not done yet:";
            return savedResult(new CommandResult(false, " OK, I've" + status + "\n   " + task));
        } catch (NumberFormatException exception) {
            return new CommandResult(false, " Please provide a task number.");
        }
    }

    /**
     * Deletes one task or delegates to the all-tasks operation.
     *
     * @param argument a task number or the word {@code all}
     * @return the response describing the deletion or validation failure
     */
    private CommandResult deleteCommand(String argument) {
        if ("all".equalsIgnoreCase(argument)) {
            return clearTasks("");
        }
        try {
            int taskNumber = Integer.parseInt(argument);
            if (!taskList.hasTaskNumber(taskNumber)) {
                return new CommandResult(false, " That task number does not exist.");
            }
            Task deletedTask = taskList.removeTask(taskNumber);
            return savedResult(new CommandResult(false, " Noted. I've removed this task:\n   " + deletedTask
                    + "\n Now you have " + taskList.size() + " tasks in the list."));
        } catch (NumberFormatException exception) {
            return new CommandResult(false, " Please provide a task number.");
        }
    }

    /**
     * Removes every task when the clear command has no arguments.
     *
     * @param argument the text after the clear command
     * @return the response describing the deletion or invalid arguments
     */
    private CommandResult clearTasks(String argument) {
        if (!argument.isEmpty()) {
            return new CommandResult(false, " The clear command does not take any arguments.");
        }
        int deletedTaskCount = taskList.size();
        taskList.clear();
        return savedResult(new CommandResult(false, " Noted. I've removed all " + deletedTaskCount
                + " tasks from your list."));
    }

    /**
     * Formats every stored task with its one-based display number.
     *
     * @return the task-list response text
     */
    private String formatTasks() {
        StringBuilder output = new StringBuilder(" Here are the tasks in your list:");
        for (int taskNumber = 1; taskNumber <= taskList.size(); taskNumber++) {
            output.append("\n ").append(taskNumber).append(".").append(taskList.getTask(taskNumber));
        }
        return output.toString();
    }

    /**
     * Finds tasks whose descriptions contain the requested keyword.
     *
     * @param keyword the text to search for
     * @return the matching tasks or a validation/no-results response
     */
    private CommandResult findTasks(String keyword) {
        if (keyword.isBlank()) {
            return new CommandResult(false, " Please provide a keyword to search for.");
        }
        List<Task> matchingTasks = taskList.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return new CommandResult(false, " No tasks found matching \"" + keyword + "\".");
        }
        StringBuilder output = new StringBuilder(" Here are the matching tasks in your list:");
        for (int taskNumber = 0; taskNumber < matchingTasks.size(); taskNumber++) {
            output.append("\n ").append(taskNumber + 1).append(".").append(matchingTasks.get(taskNumber));
        }
        return new CommandResult(false, output.toString());
    }

    /**
     * Adds a warning when a successful task mutation could not be persisted.
     *
     * @param result the response for the in-memory mutation
     * @return the original response, possibly with a save warning appended
     */
    private CommandResult savedResult(CommandResult result) {
        if (storage.save(taskList)) {
            return result;
        }
        return new CommandResult(false, result.message()
                + "\n Warning: I could not save the task list to disk.");
    }
}

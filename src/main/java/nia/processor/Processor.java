package nia.processor;

import nia.exceptions.NiaException;
import nia.exceptions.UnknownCommandException;
import nia.parser.Parser;
import nia.tasks.Deadline;
import nia.tasks.Event;
import nia.tasks.Task;
import nia.tasks.TaskList;
import nia.tasks.Todo;
import nia.ui.Printer;

/**
 * Executes commands against a TaskList using the already-canonical, already-sanitized
 * words Parser produces. No input sanitization or alias resolution happens here -
 * by the time `process` sees `words`, words[0] is guaranteed to be a canonical command.
 */
public class Processor {

    /** Dispatches on words[0]. Returns false when the program should stop running. */
    public static boolean process(String[] words, TaskList taskList) throws NiaException {
        String command = words[0];

        switch (command) {
            case "bye":
                return false;
            case "list":
                handleList(words, taskList);
                return true;
            case "mark":
            case "unmark":
                handleMarkOrUnmark(words, taskList, command.equals("mark"));
                return true;
            case "delete":
                handleDelete(words, taskList);
                return true;
            case "todo":
                handleAddTodo(words, taskList);
                return true;
            case "deadline":
                handleAddDeadline(words, taskList);
                return true;
            case "event":
                handleAddEvent(words, taskList);
                return true;
            case "find":
                handleFind(words, taskList);
                return true;
            case "help":
                handleHelp();
                return true;
            default:
                throw new UnknownCommandException(command);
        }
    }

    /**
     * Prints every task in taskList, optionally filtered to one type by words[1]
     * (a tag from Parser.parseList - "T"/"D"/"E"). Indices printed are always the
     * task's position in the full list, even when filtered, so they stay valid for
     * mark/unmark/delete.
     */
    private static void handleList(String[] words, TaskList taskList) {
        String filterTag = words.length > 1 ? words[1] : null;
        if (taskList.isEmpty()) {
            Printer.printIndent("Nothing here...");
            return;
        }
        boolean printedAny = false;
        for (int i = 1; i <= taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            if (filterTag != null && !task.getTag().equals(filterTag)) {
                continue;
            }
            Printer.printIndent(String.format("%d. %s", i, task));
            printedAny = true;
        }
        if (!printedAny) {
            Printer.printIndent("Nothing here...");
        }
    }

    /**
     * Prints every task whose description contains words[1] (case-insensitive).
     * Parser guarantees words[1] is non-blank before words ever reaches here.
     * Indices printed are the task's position in the full list, same as handleList.
     */
    private static void handleFind(String[] words, TaskList taskList) {
        String keyword = words[1].toLowerCase();
        boolean printedAny = false;
        for (int i = 1; i <= taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            if (!task.getDescription().toLowerCase().contains(keyword)) {
                continue;
            }
            Printer.printIndent(String.format("%d. %s", i, task));
            printedAny = true;
        }
        if (!printedAny) {
            Printer.printIndent("Nothing here...");
        }
    }

    /**
     * Parses and validates `words` (expects exactly ["mark"/"unmark", "<number>"]),
     * then marks/unmarks the referenced task.
     * Guard clauses: throw early on wrong arg count, non-numeric arg, or out-of-range index —
     * this is what keeps this method flat instead of nesting ifs inside ifs.
     */
    private static void handleMarkOrUnmark(String[] words, TaskList taskList, boolean markAsDone)
            throws NiaException {
        int taskToChange = Parser.parseAndValidateTaskIndex(words, taskList);

        if (markAsDone) {
            taskList.getTask(taskToChange).markAsDone();
            Printer.printIndent(String.format("Marked %d as done", taskToChange));
        } else {
            taskList.getTask(taskToChange).markAsNotDone();
            Printer.printIndent(String.format("Marked %d as not done", taskToChange));
        }
    }

    /**
     * Deletes the task at `words` = ["delete", "<number>"]. Prints the removed task
     * and the resulting list size, matching the tone of the "add" confirmation.
     */
    private static void handleDelete(String[] words, TaskList taskList) throws NiaException {
        int taskToRemove = Parser.parseAndValidateTaskIndex(words, taskList);
        Task removedTask = taskList.delete(taskToRemove);

        int remaining = taskList.getSize();
        Printer.printIndent(String.format(
                "Ugh, fine, I've thrown out this task:\n    %s\nNow you have %d task%s left in your list.",
                removedTask, remaining, remaining == 1 ? "" : "s"));
    }

    /**
     * Adds a Todo from `words` = ["todo", description]. Parser guarantees
     * description is non-blank before words ever reaches here.
     */
    private static void handleAddTodo(String[] words, TaskList taskList) {
        addTask(new Todo(words[1]), taskList);
    }

    /**
     * Adds a Deadline from `words` = ["deadline", description, by]. Parser
     * guarantees both fields are non-blank and by is already in canonical
     * date-time format before words ever reaches here.
     */
    private static void handleAddDeadline(String[] words, TaskList taskList) {
        addTask(new Deadline(words[1], Parser.parseCanonical(words[2])), taskList);
    }

    /**
     * Adds an Event from `words` = ["event", description, from, to]. Parser
     * guarantees all three fields are non-blank and from/to are already in
     * canonical date-time format before words ever reaches here.
     */
    private static void handleAddEvent(String[] words, TaskList taskList) {
        addTask(new Event(words[1], Parser.parseCanonical(words[2]), Parser.parseCanonical(words[3])), taskList);
    }

    /** Adds `task` to taskList. */
    private static void addTask(Task task, TaskList taskList) {
        taskList.add(task);
        Printer.printIndent("I've added the task \"" + task + "\" to your task list");
    }

    /** Prints a summary of every command Processor recognizes and how to use it. */
    private static void handleHelp() {
        Printer.printIndent(String.join("\n",
                "Here's everything I can do:",
                "  list [t | d | e]                 - show all tasks, optionally filtered by type "
                        + "(t=todo, d=deadline, e=event)",
                "  todo <description>               - add a todo",
                "  deadline <description> /by <when> - add a deadline",
                "  event <description> /from <when> /to <when> - add an event",
                "  mark <number>                    - mark a task as done",
                "  unmark <number>                  - mark a task as not done",
                "  delete <number>                  - delete a task",
                "  find <keyword>                   - search task descriptions",
                "  help                              - show this message",
                "  bye                               - exit Nia",
                "<when> accepts yyyy-MM-dd, optionally followed by HHmm or HH:mm."));
    }
}

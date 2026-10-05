package ted;

import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import ted.exception.TedException;
import ted.task.Deadline;
import ted.task.Event;
import ted.task.Task;
import ted.task.Todo;

/**
 * Runs the text-based Ted task manager.
 */
public class Ted {
    private static final String SEPARATOR = "    ____________________________________________________________";
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String FIND_COMMAND = "find";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    private TaskList tasks;
    private final TedStorage storage = new TedStorage();

    /**
     * Creates Ted and restores tasks saved by an earlier run.
     */
    public Ted() {
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            System.out.println("     OOPS!!! Could not load saved tasks: " + exception.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Starts Ted and processes commands from standard input.
     *
     * @param args Command-line arguments. Ted ignores these arguments.
     */
    public static void main(String[] args) {
        if (System.console() == null) {
            // Preserve task descriptions when output is redirected to a pipe or file.
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        }
        new Ted().run();
    }

    /**
     * Starts the command loop and processes commands from standard input.
     */
    private void run() {
        printWelcomeMessage();
        Console console = System.console();
        Scanner scanner = console == null
                ? new Scanner(System.in, StandardCharsets.UTF_8)
                : new Scanner(console.reader());

        while (scanner.hasNextLine()) {
            System.out.println(SEPARATOR);
            boolean isBye = processCommand(scanner.nextLine());
            System.out.println(SEPARATOR);
            if (isBye) {
                break;
            }
        }
    }

    /**
     * Prints the initial greeting shown when Ted starts.
     */
    private static void printWelcomeMessage() {
        System.out.println("                                                                                \n" +
                "                                            .  .                                \n" +
                "                                         .....  .       .                       \n" +
                "                          ..     ...... . .................    .                \n" +
                "                       .  ................................+&&&&&&;.             \n" +
                "                   .. ........................:.:.:..::x&$XxxxX&$$&&&.          \n" +
                "              .    .... ................::.:..;:..::.;$x:;+;+++Xxx$XX$&;        \n" +
                "           x&&&&&&&&&&;......::::..:.:+::;;::::;;:::;X;;;Xx+x+:x+x+&Xx&$$       \n" +
                "        .x&&&$X$$X$$XXX$x:::.:X$xx&&&&&&&&&&&&&&&&&&&+. x;x..;:+;..;::+$&&      \n" +
                "      x&Xx$X$XXx+Xx+x+x.x;;&&&&$$&&&&&&&&&&$$$&&&&&&&&&;  :.+:x;    ..:x$$      \n" +
                "     ;Xxx;+xx.:;;+x:;+xx+$&&X&xXXx&$&&&&&&&$&X$$&$&X$&$&$&&++ .   ..    x..     \n" +
                "    +x;::  .:;.    :.:X$&x$X$$X$x+$x$XX&$&&&$$$Xxx$$X$x&$&&&&&.       :xX.      \n" +
                "    $:.   . .        xXXxXx$xX$$xX+xXx;X&&+$$$$$x$X+$&$X&&&&&&&&+  .. :x+:      \n" +
                "    $.             +xXx$$$$xxx$$X$$XX$$xX$$X$xx$XxxX+X$$$X$&&&&&&&+   .x:. ..   \n" +
                "    X...        .x$Xx$;XX$x$:&x$xx+x$x;x$&X$xx+$&&&&xX&$&x$x$$$&&&&&.;.......   \n" +
                "    ;          .++xxXxX$$&XX$$XX$XxXxxxX&&&$Xx&&&$&$&&&&&&$$$&&&&&&&&&&:....    \n" +
                "             +:+xXxXX$XXx$xxXXxXxXXxX$$$XX$$xx&&x&$$&$&&&&&&&$X$&&&&&&&&...     \n" +
                "           .::x+;xxx$XXxXXx$&$$&$&&&$&xxxxxX.:$xX$X$&$$&x$&&&&&&$&&&&&&&&.      \n" +
                "         ..:;;++;:x+$+xxXX$$&&&&&$&$&$+;:x$x;: ;::;x++x$&+&&$&&$&$$$&&&&&$.     \n" +
                "        :+;.;x;;x:xxxxxXxxXXX&$$&$$$xxXXxX+X;x;;:.        ;X$$$xX&$$&&&&&&..    \n" +
                "       ;+;+:+x+x:x;;. +++xxXxx$: :.+:xXxXxX$X+xx;.      .X$Xx$X$$&&$&$&&&&&.    \n" +
                "      XX;::::.;x;;.......:       .x+$xxxxxX$xxx+x+:.    &x$&X$&$&&$$$&&&&&&.    \n" +
                "      x;.;::++:++:.::.          ..:X$XXxx+X$xxxX;xXx   $$&$&&&&&&&&&&&&&&&&x..  \n" +
                "     +++:..:;::;::.....          ..;xx&&&&&&&&&&&&&&$$$&&&&&&&X$&&&X&&&$&&&&.   \n" +
                "     Xx:....::...;......        ;+x$$&&&&&&&&&&&&&&&&&&&&&&&&&&$&&&&&&&&&&&$;   \n" +
                "     +:..:....:.:...:::+;xxXxx+xX$X&&&&&&&$&$x$&&&&&&&&&&&&&&&&$&&$$&&&&&&&$    \n" +
                "     ..:. ....;...;:+x;xxxXXxxxxx$$&&&$$X+; .++X$$&&&&&&&&$$X&$X&$X&$&$&&&&$    \n" +
                "    .+. .....:.:.:;x:;++xxxX++xxxX$&&&X x+++.  +x$&&&&&&&&$xX$X&$&x$&&x$$$$&    \n" +
                "     . ....  . :+:;::::+x+x;.++xxxx$&x.       .&XX$&$&&&&&&:xx+$&$x$XX$$&XX     \n" +
                "     .  .. .....:.:+:;:.:;::++;+xxxXxx$x     ++XX$X$$$X&&&&;..x$x$$$X&$&$&$     \n" +
                "     . ..... ... ...;......;;+;x;x+;xxXxX$X ;;++XX$$$$$&&&&$;XxX;$$&$$$$&&$     \n" +
                "      .    ...  . .. . .. .:+;;+;;::;x;;+&& .; xxxXXX$$&&&&&:.x;x$$$X$&x&X.     \n" +
                "            .. .   .       ;;;;:::.  .+x;x+ .x;+;xXXx$$&&&&xxXxX:&xXx$x+$X      \n" +
                "        .      . ..   ..   :.::::... . .xxx :; ;xxXX&$X$x&+;xxx$X$xXxxX$x       \n" +
                "                            .. .... .   .;    .;.xxx;+.x$x xx$+xX&$xx$.+        \n" +
                "                              .        .            .xX&xx. &x;:+:$x&.          \n" +
                "                               ....              .x&&&& . X  $;x;+.$&XX:        \n" +
                "    .x:+XXX$X+                   ...... ......:+X&$XX:+ ; x  . .;.+;x$$xxX&+    \n" +
                "x+XXx.; ..   .                      ...:::..:.x:;. :X +   .  xX .:.;+xxX$$XX$;  \n" +
                "  ;.;;.++                                ..         x+.        ;xx;+xXxXXX+X$x$.\n" +
                ".;.:+;++x+$x                             .                 .;xxXxX++xx;;xXxXX$$$\n" +
                ". :.:;:.+;+x.;;                        .                 ;;xXX$X$$$XxXxX:;+xxX$$\n" +
                " . ;...:.:.;...;:                                 ..;:xxxxXxX$x$$$$$xx:.;++xxX$$\n" +
                " .. .:+:.:;:;;;;:.                            .;x+;x+xxxxxxX$$&$$&X&$x.:;x;xxX&$\n" +
                "   ..;x+x:+++;;;:..                     ...;+.:xxx+xxXXX$XX$$$&$$$$&&&:x;++XX$X&\n" +
                "   . .x+;++x;.+: :..                 .;+x&xXXxx++xXxX+XXXxX$xx$&X$X$$&x .;xxxXx$\n" +
                "     ..:xxx++;x+:;:..              .;.;$$xX$$$;+xxxxxxXXxX;xXXxX$XX$&x&X..x+xX$X\n" +
                "    ....:;;x+;x;x;+..          . ..;xxxxxx$X$XxX+xxxxx:;xxXXXx$$+X$$xX$&$.++xxx$\n" +
                "       :;x;+;;+x;;..:         ..:++x+xxX+XXx+x+xxxx.;+xxxxXxxXxX$x$$xXxXXX..+xXx\n" +
                "       ..:::+;xx;+...        ..;+xx:xxx+X$X$x;++;xxx+xxxxXXXxxxxxxX$XxXX$Xx:.;++\n" +
                "          ..;+.:;..;.       ..::x:::x.+:X$xxxxxxx;;xxx;xxxxxxxx;xxxxxxXXx;Xx; .:\n" +
                "            .::. ..        ..;:.+;x:;xx+;++xx;xxx;;+++xxxxxxx+++xxxx+xx+xxxx+ ..\n" +
                "                .         ...:;+::;+;::.++.+xx+;;;;+x++:++xx++xxx+x+x+xx+++x+. :\n" +
                "                            ..:::+;:...:. :+::;:;:;+;+:+;:;+++;;;;++;+x+++++x+..\n" +
                "                          .  :.....:. .:..:.:.;.:;++;:;;;+x+;:;;+:;;+;;+;+:;+++.\n");
        System.out.println("Hello! I'm Ted.\n\nWhat can I do for you?\n");
    }

    /**
     * Processes one command and reports whether the command ends the session.
     *
     * @param command The command entered by the user.
     * @return {@code true} when the command is {@code bye}; {@code false} otherwise.
     */
    private boolean processCommand(String command) {
        try {
            if (command.equals(BYE_COMMAND)) {
                System.out.println("     Bye. Hope to see you again soon!");
                return true;
            }

            if (command.equals(LIST_COMMAND)) {
                printTaskList();
            } else if (isCommand(command, FIND_COMMAND)) {
                findTasks(command);
            } else if (isCommand(command, MARK_COMMAND)) {
                updateTaskStatus(command, true);
            } else if (isCommand(command, UNMARK_COMMAND)) {
                updateTaskStatus(command, false);
            } else if (isCommand(command, DELETE_COMMAND)) {
                deleteTask(command);
            } else if (isCommand(command, TODO_COMMAND)) {
                handleTodo(command);
            } else if (isCommand(command, DEADLINE_COMMAND)) {
                handleDeadline(command);
            } else if (isCommand(command, EVENT_COMMAND)) {
                handleEvent(command);
            } else {
                throw new TedException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (TedException exception) {
            printError(exception.getMessage());
        }

        return false;
    }

    /**
     * Returns whether a command matches a command name with an optional argument.
     */
    private static boolean isCommand(String command, String commandName) {
        return command.equals(commandName) || command.startsWith(commandName + " ");
    }

    /**
     * Prints all tasks currently stored by Ted.
     */
    private void printTaskList() {
        System.out.println("     Here are the tasks in your list:");
        printTasks(tasks.getAll());
    }

    /**
     * Prints the tasks whose descriptions contain the supplied search keyword.
     */
    private void findTasks(String command) throws TedException {
        String keyword = command.substring(FIND_COMMAND.length()).trim();
        if (keyword.isEmpty()) {
            throw new TedException("The keyword for find cannot be empty.");
        }

        System.out.println("     Here are the matching tasks in your list:");
        printTasks(tasks.find(keyword));
    }

    /**
     * Prints tasks numbered from one in the supplied list order.
     */
    private void printTasks(List<Task> tasksToPrint) {
        for (int i = 0; i < tasksToPrint.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasksToPrint.get(i));
        }
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     */
    private void updateTaskStatus(String command, boolean shouldMarkAsDone) throws TedException {
        String commandName = shouldMarkAsDone ? MARK_COMMAND : UNMARK_COMMAND;
        String taskNumber = command.substring(commandName.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            Task task = tasks.setDone(taskIndex, shouldMarkAsDone);
            if (shouldMarkAsDone) {
                System.out.println("     Nice! I've marked this task as done:");
            } else {
                System.out.println("     OK, I've marked this task as not done yet:");
            }
            saveTasks();
            System.out.println("       " + task);
        } catch (NumberFormatException exception) {
            throw new TedException("Please provide a valid task number.");
        }
    }

    /**
     * Removes a task selected by its one-based list number.
     */
    private void deleteTask(String command) throws TedException {
        String taskNumber = command.substring(DELETE_COMMAND.length()).trim();
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            Task removedTask = tasks.remove(taskIndex);
            saveTasks();
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + removedTask);
            System.out.println("     Now you have " + tasks.size() + " "
                    + (tasks.size() == 1 ? "task" : "tasks") + " in the list.");
        } catch (NumberFormatException exception) {
            throw new TedException("Please provide a valid task number.");
        }
    }

    /**
     * Handles a todo command.
     */
    private void handleTodo(String command) throws TedException {
        String description = command.substring(TODO_COMMAND.length()).trim();
        if (description.isEmpty()) {
            throw new TedException("The description of a todo cannot be empty.");
        }

        addTask(new Todo(description));
    }

    /**
     * Handles a deadline command.
     */
    private void handleDeadline(String command) throws TedException {
        String details = command.substring(DEADLINE_COMMAND.length()).trim();
        int separatorIndex = details.indexOf(DEADLINE_SEPARATOR);
        if (separatorIndex <= 0
                || separatorIndex + DEADLINE_SEPARATOR.length() >= details.length()) {
            throw new TedException("Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }

        String description = details.substring(0, separatorIndex).trim();
        String by = details.substring(separatorIndex + DEADLINE_SEPARATOR.length()).trim();
        try {
            addTask(new Deadline(description, LocalDate.parse(by)));
        } catch (DateTimeParseException exception) {
            throw new TedException("Please provide a valid deadline date in yyyy-MM-dd format.");
        }
    }

    /**
     * Handles an event command.
     */
    private void handleEvent(String command) throws TedException {
        String details = command.substring(EVENT_COMMAND.length()).trim();
        int fromSeparatorIndex = details.indexOf(EVENT_FROM_SEPARATOR);
        int toSeparatorIndex = details.indexOf(EVENT_TO_SEPARATOR,
                fromSeparatorIndex + EVENT_FROM_SEPARATOR.length());
        boolean hasInvalidEventDetails = fromSeparatorIndex <= 0
                || toSeparatorIndex <= fromSeparatorIndex + EVENT_FROM_SEPARATOR.length()
                || toSeparatorIndex + EVENT_TO_SEPARATOR.length() >= details.length();
        if (hasInvalidEventDetails) {
            throw new TedException("Use: event DESCRIPTION /from START /to END");
        }

        String description = details.substring(0, fromSeparatorIndex).trim();
        String from = details.substring(fromSeparatorIndex + EVENT_FROM_SEPARATOR.length(),
                toSeparatorIndex).trim();
        String to = details.substring(toSeparatorIndex + EVENT_TO_SEPARATOR.length()).trim();
        addTask(new Event(description, from, to));
    }

    /**
     * Adds a task when capacity remains and prints the addition confirmation.
     */
    private void addTask(Task task) throws TedException {
        tasks.add(task);
        saveTasks();
        printTaskAdded(task, tasks.size());
    }

    /**
     * Saves the current task list and reports file errors without ending the command loop.
     */
    private void saveTasks() {
        try {
            storage.save(tasks.getAll());
        } catch (IOException exception) {
            System.out.println("     OOPS!!! Could not save tasks: " + exception.getMessage());
        }
    }

    /**
     * Prints a consistent error response for a command failure.
     *
     * @param message The explanation of the command failure.
     */
    private static void printError(String message) {
        System.out.println("     OOPS!!! " + message);
    }

    /**
     * Prints confirmation that a task was added and reports the new task count.
     *
     * @param task The task that was added.
     * @param taskCount The number of stored tasks after the addition.
     */
    private static void printTaskAdded(Task task, int taskCount) {
        String taskNoun = taskCount == 1 ? "task" : "tasks";
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " " + taskNoun + " in the list.");
    }
}



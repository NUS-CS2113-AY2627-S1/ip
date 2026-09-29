package kento.ui;

import java.util.Scanner;

import kento.Storage;
import kento.TaskList;
import kento.Ui;
import kento.Parser;
import kento.commands.Deadline;
import kento.commands.Event;
import kento.commands.Task;
import kento.commands.Todo;
import kento.exception.CommandException;
import kento.exception.TodoException;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Kento {
    private static String cmd;
    private static String args;
    private static String input;
    private static String task;

    // Prints
    public static final String PAGE_LINE = "____________________________________________________________";

    // Colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private static final Path FILEPATH = Path.of("./.data/kento.txt");

    private static Task t;

    private static int inputTaskIdx = 0;

    private static boolean isRunning = true;

    private static Storage storage;
    private static Ui ui;
    private static TaskList taskList;
    private static Parser parser;

    /**
     * create instance of Kento Class and runs the cli loop
     * 
     * @param args given in the cli
     */
    public static void main(String[] args) {
        new Kento().run();
    }

    private Kento() {
        storage = new Storage(FILEPATH);
        ui = new Ui();
        taskList = new TaskList();
        parser = new Parser();
    }

    private static void run() {

        ui.showKentoGreeting();
        storage.loadTasksFiles(taskList.getTasks());

        while (isRunning) {
            runWithErrorHandling();
        }
    }

    private static void runWithErrorHandling() {
        try {
            Scanner in = new Scanner(System.in);
            input = in.nextLine();
            cmd = parser.parseCommand(input);
            args = parser.parseArguments(input);

            executeCmd();
        } catch (CommandException e) {

            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                     OOPSI!!! That command is so wrong. Please do better!
                    ____________________________________________________________
                                    """ + ANSI_RESET);
        } catch (TodoException e) {

            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                    c'mon man. Your todo command is missing a description!
                    ____________________________________________________________
                                    """ + ANSI_RESET);
        } catch (IndexOutOfBoundsException e) {
            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                     Out of bounds error: Please provide an task index or a valid date.
                    ____________________________________________________________
                                    """ + ANSI_RESET);
        } catch (NumberFormatException e) {

            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                     Please provide a passable number instead of string.
                    ____________________________________________________________
                                    """ + ANSI_RESET);

        } catch (IOException e) {

            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                     File path not found
                    ____________________________________________________________
                                    """ + ANSI_RESET);
        } catch (DateTimeParseException e) {

            System.out.print(ANSI_RED + """
                    ____________________________________________________________
                     Wrong date
                    ____________________________________________________________
                                    """ + ANSI_RESET);

        }
    }

    private static void executeCmd() throws TodoException, IOException, CommandException {
        switch (cmd) {

            case "bye":
                executeCmdBye(); // static method, so this.executeCmdBye() is unnecessary
                break;
            case "list":
                executeCmdList();
                break;

            case "delete":
                executeCmdDelete();
                break;

            case "mark":
                executeCmdMark();
                break;
            case "unmark":
                executeCmdUnmark();
                break;

            case "deadline":
                executeCmdDeadline();
                break;

            case "event":
                executeCmdEvent();
                break;

            case "todo":
                executeCmdTodo();
                break;

            case "find":
                executeCmdFind();
                break;

            default:
                executeCmdDefault();
                break;
        }

    }

    private static void executeCmdBye() throws IOException {

        storage.writeTasksFiles(taskList.getTasks());

        System.out.print("""
                ____________________________________________________________
                 Bye, don't come back without more money!
                ____________________________________________________________
                                """);
        isRunning = false;
    }

    private static void executeCmdList() {
        System.out.println("____________________________________________________________\n");

        int i = 0;
        for (Task task : taskList.getTasks()) {
            i++;
            // TODO: Move to commands classes tostring.
            System.out.println(Integer.toString(i) + ". [" + task.getStatusIcon() + "]"
                    + task.getDescription());
        }
        System.out.println("____________________________________________________________");

    }

    private static void executeCmdFind() {
        System.out.println("____________________________________________________________\n");
        System.out.println("These match your search word");
        int i = 0;
        for (Task task : taskList.getTasks()) {
            if (task.getDescription().contains(args)) {
                i++;
                System.out.println(Integer.toString(i) + ". [" + task.getStatusIcon() + "]"
                        + task.getDescription());

            }
        }
        System.out.println("____________________________________________________________");

    }

    private static void executeCmdDelete() throws IndexOutOfBoundsException, CommandException {
        System.out.println(PAGE_LINE);

        inputTaskIdx = parser.parseIndexArgument(args);

        if (inputTaskIdx >= taskList.getTasksSize())
            throw new IndexOutOfBoundsException();

        t = taskList.getTaskByIndex(inputTaskIdx);

        System.out.println("Removed \n[" + t.getStatusIcon() + "] " + t.getDescription());
        taskList.removeTaskByIndex(inputTaskIdx);
        System.out.println(PAGE_LINE);
    }

    private static void executeCmdMark() throws IndexOutOfBoundsException, CommandException {
        System.out.println(
                "____________________________________________________________");

        inputTaskIdx = parser.parseIndexArgument(args);

        if (inputTaskIdx >= taskList.getTasksSize())
            throw new IndexOutOfBoundsException();
        t = taskList.getTaskByIndex(inputTaskIdx);
        t.setIsDone(true);
        System.out.println("Marked task\n[" + t.getStatusIcon() + "] " + t.getDescription());
        System.out.println("____________________________________________________________");
    }

    private static void executeCmdUnmark() throws IndexOutOfBoundsException, CommandException {
        System.out.println(
                "____________________________________________________________");

        inputTaskIdx = parser.parseIndexArgument(args);

        if (inputTaskIdx >= taskList.getTasksSize())
            throw new IndexOutOfBoundsException();

        t = taskList.getTaskByIndex(inputTaskIdx);
        t.setIsDone(false);
        System.out.println("Unmarked task\n[" + t.getStatusIcon() + "] " + t.getDescription());
        System.out.println("____________________________________________________________");
    }

    private static void executeCmdDeadline() throws CommandException, DateTimeParseException {
        String[] parseRes = parser.parseDeadlineArguments(args);
        LocalDate by = LocalDate.parse(parseRes[1]);

        taskList.addTask(new Deadline(parseRes[0], by));
    }

    private static void executeCmdTodo() throws TodoException, CommandException {
        task = parser.parseTodoArguments(args);
        if (task.length() == 0)
            throw new TodoException();
        taskList.addTask(new Todo(task));
    }

    private static void executeCmdEvent() throws CommandException, DateTimeParseException {
        String[] parseRes = parser.parseEventArguments(args);

        LocalDate from = LocalDate.parse(parseRes[1]);
        LocalDate to = LocalDate.parse(parseRes[2]);
        taskList.addTask(new Event(parseRes[0], from, to));
    }

    private static void executeCmdDefault() {
        System.out.print(String.format("""

                ____________________________________________________________
                Unknown command: %s
                ____________________________________________________________
                                """, cmd));
    }
}

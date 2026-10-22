package milk.main;

import milk.exception.MilkException;
import milk.task.Deadline;
import milk.task.Event;
import milk.task.Task;
import milk.task.Todo;
import milk.ui.Ui;
import milk.data.FileManager;

import java.text.NumberFormat;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.IOException;

/**
 * Main Milk class. Contains all the methods for high-level management by the chatbot.
 */
public class Milk {

    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static int numTasks = 0;
    private static Ui ui = new Ui();
    private static FileManager fileManager = new FileManager();

    /**
     * Creates a save file if required and loads the task file.
     */
    public Milk() {
        FileManager.CreateFile();
        tasks = FileManager.LoadFile();
    }

    /**
     * Main loop of the chatbot.
     * Exits if the user enters a line starting with "bye".
     */
    public void run() {
        for (Task task : tasks) {
            if (task == null) {
                break;
            }
            numTasks++;
        }
        ui.printGreeting();
        String line;
        do {
            line = ui.readCommand();
            handleCommand(line);
        } while (!line.startsWith("bye"));
        ui.printGoodbye();
    }

    private static void handleCommand(String line) {
        String[] words = line.split(" ");
        String command = words[0];
        try {
            switch (command) {
                case "todo":
                    handleTodo(line);
                    fileManager.UpdateFile(tasks, numTasks);
                    break;
                case "deadline":
                    handleDeadline(line);
                    fileManager.UpdateFile(tasks, numTasks);
                    break;
                case "event":
                    handleEvent(line);
                    fileManager.UpdateFile(tasks, numTasks);
                    break;
                case "list":
                    listTasks();
                    break;
                case "mark":
                    markTask(words[1]);
                    fileManager.UpdateFile(tasks, numTasks);
                    break;
                case "unmark":
                    unmarkTask(words[1]);
                    fileManager.UpdateFile(tasks, numTasks);
                    break;
                case "bye":
                    break;
                case "delete":
                    deleteTask(words[1]);
                    break;
                case "find":
                    findTask(line.substring("find".length()).trim());
                    ui.printResponse(line.substring("find".length()).trim());
                    break;
                default:
                    ui.printResponse("\"" + line + "\"...? I don't know this command!!");
                    break;
            }
        } catch (MilkException e) {
            ui.printResponse("An exception was caught...");
        } catch (IOException e) {
            ui.printResponse("An exception was caught...");
        }
    }

    private static void deleteTask(String taskToDelete) throws MilkException {
        try {
            int indexOfTaskToDelete = Integer.parseInt(taskToDelete) - 1;
            if (indexOfTaskToDelete > numTasks - 1) {
                throw new MilkException("But you don't even have that many tasks!");
            }
            if (indexOfTaskToDelete <= -1) {
                throw new MilkException("The index should at least be 1!!");
            }
            ui.printResponse("Okay... deleted task:\n  " + tasks.get(indexOfTaskToDelete).toString());
            tasks.remove(indexOfTaskToDelete);
            numTasks--;
            if (numTasks < 0) {
                numTasks = 0;
            }
            ui.printResponse("Now you only have " + numTasks + " tasks! Congrats...?");
        } catch (NumberFormatException e) {
            ui.printResponse("You need to give me an index!! Like \"delete 1\"!");
        }
    }

    private static void handleTodo(String line) throws MilkException {
        if (line.length() == 4 || line.equals("todo ")) {
            throw new MilkException("I don't understand! You should tell me something like \"todo water the plants\"!!");
        }
        tasks.add(new Todo(line.substring("todo".length()).trim()));
        ++numTasks;
        ui.printResponse("Okay!! Added todo: " + line.substring("todo".length()).trim());
    }

    private static void handleDeadline(String line) throws MilkException {
        String deadlineIn = line.substring("deadline".length()).trim();
        String[] deadlineParams = deadlineIn.split(" /by ");
        if (deadlineParams.length != 2) {
            throw new MilkException("This is invalid format!! Say something like \"deadline finish homework /by tomorrow\"!");
        }
        tasks.add(new Deadline(deadlineParams[0], deadlineParams[1]));
        ++numTasks;
        ui.printResponse("Okay!! Added deadline: " + deadlineParams[0] + " (by " + deadlineParams[1] + ")");
    }

    private static void handleEvent(String line) throws MilkException {
        String eventIn = line.substring("event".length()).trim();
        String[] eventParams = eventIn.split("(?: /from | /to )");
        if (eventParams.length != 3) {
            throw new MilkException("This is invalid format!! Say something like \"event party /from 5 /to 8\"!");
        }
        tasks.add(new Event(eventParams[0], eventParams[1], eventParams[2]));
        ++numTasks;
        ui.printResponse("Okay!! Added event: " + eventParams[0] + " (from " + eventParams[1] + " to " + eventParams[2] + ")");
    }

    private static void markTask(String toMark) throws MilkException {
        try {
            int indexToMark = Integer.parseInt(toMark);
            if (indexToMark > numTasks) {
                throw new MilkException("You don't have that many tasks though...");
            }
            tasks.get(indexToMark - 1).setMarked(true);
            ui.printResponse(tasks.get(indexToMark - 1).getDescription() + " has been completed!");
        } catch (NumberFormatException e) {
            ui.printResponse("You didn't give me a task to mark!! For example, say \"mark 2\"!");
        }

    }

    private static void unmarkTask(String toUnmark) throws MilkException {
        try {
            int indexToUnmark = Integer.parseInt(toUnmark);
            if (indexToUnmark > numTasks) {
                throw new MilkException("You don't have that many tasks though...");
            }
            tasks.get(indexToUnmark - 1).setMarked(false);
            ui.printResponse(tasks.get(indexToUnmark - 1).getDescription() + " has been unmarked!");
        } catch (NumberFormatException e) {
            ui.printResponse("You didn't give me a task to unmark!! For example, say \"unmark 2\"!");
        }
    }

    private static void listTasks() {
        ui.printResponse("Here's your tasks!");
        for (int i = 0; i < numTasks; ++i) {
            ui.printResponse("  " + (i + 1) + ") " + tasks.get(i), false);
        }
    }

    private static void findTask(String phraseToFind) {
        ui.printResponse("Here's your matching tasks!");
        int numMatchingTasks = 0;
        for(int i = 0; i < numTasks; ++i) {
            if (tasks.get(i).getDescription().contains(phraseToFind)) {
                numMatchingTasks++;
                ui.printResponse("  " + numMatchingTasks + ") " + tasks.get(i), false);
            }
        }
    }

    /**
     * Runs the constructor and the main loop methods.
     * @param args args of the program.
     */
    public static void main(String[] args) {
        new Milk().run();
    }
}
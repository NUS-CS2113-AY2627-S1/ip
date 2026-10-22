package dexter;

import dexter.classifications.Command;
import dexter.exceptions.EmptyListException;
import dexter.exceptions.InvalidTaskException;
import dexter.exceptions.MissingDescriptionException;
import dexter.exceptions.MissingIndexException;
import dexter.handler.Function;
import dexter.handler.Parser;
import dexter.tasks.Task;
import dexter.data.Storage;
import dexter.ui.Ui;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/** Coordinates command execution, task storage, and user interaction. */
public class Dexter {
    private Storage storage;
    private ArrayList<Task> tasks;
    private Ui ui;

    /** Initializes the application and loads saved tasks, or starts with an empty list. */
    public Dexter() {
        ui = new Ui();
        storage = new Storage();
        try {
            tasks = storage.readFromDatabase();
        } catch (IOException e) {
            tasks = new ArrayList<>();
        }
    }

    /** Displays the greeting and processes user commands until the user exits. */
    public void run() {
        ui.showWelcome();
        String line;
        Scanner in = new Scanner(System.in);
        int itemCount = tasks.size();
        while (true) {
            line = in.nextLine();
            try {
                if (line.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (line.equals("find") || line.startsWith("find ")) {
                    Function.find(line, tasks, ui);
                } else if (line.startsWith("mark")) {
                    Function.function(line, Command.MARK, tasks, ui);
                    storage.writeToDatabase(tasks);
                } else if (line.startsWith("unmark")) {
                    Function.function(line, Command.UNMARK, tasks, ui);
                    storage.writeToDatabase(tasks);
                } else if(line.startsWith("delete")) {
                    Function.function(line, Command.DELETE, tasks, ui);
                    storage.writeToDatabase(tasks);
                } else if (line.equals("bye")) {
                    ui.showFarewell();
                    return;
                } else {
                    Task newTask = Parser.parseTask(line);
                    tasks.add(newTask);
                    itemCount++;
                    ui.showTaskAdded(newTask, itemCount);
                    storage.writeToDatabase(tasks);

                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use yyyy-MM-dd or d/M/yyyy HHmm (e.g. 2/12/2019 1800).");
            } catch (MissingDescriptionException e) {
                System.out.println("Oh, I think you may have missed out some details. Can you repeat?");
            } catch (InvalidTaskException e) {
                System.out.println("Sorry but I do not understand. Can you repeat?");
            } catch (MissingIndexException e) {
                System.out.println("Oh no! You need to have a number to indicate the item in the list.");
            } catch (EmptyListException e) {
                System.out.println("Hey! Your list is still empty!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please enter a value within the list size!");
            } catch (IOException e) {
                System.out.println("Sorry I have trouble retrieving ur data");
            }
        }
    }

    /** Starts the Dexter application. */
    public static void main(String[] args) {
        new Dexter().run();
    }
}

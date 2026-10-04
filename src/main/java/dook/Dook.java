package dook;

import java.util.Random;

import dook.command.Response;
import dook.exception.DookException;
import dook.service.ChatInterface;
import dook.service.CommandManager;
import dook.service.FileManager;
import dook.service.TaskManager;

/**
 * Represents a CLI program for task management.
 */
public class Dook {
    private static final ChatInterface ui = new ChatInterface();

    private Random random;
    private FileManager io;
    private TaskManager taskManager;
    private CommandManager commandManager;

    /**
     * Constructs a new Dook with specified random generator and file manager.
     *
     * @param  random Random number generator.
     * @param  io     The file manager.
     */
    public Dook(Random random, FileManager io) {
        this.random = random;
        this.io = io;

        taskManager = new TaskManager(io.getTaskFileIO());
        commandManager = new CommandManager(taskManager, random);
    }

    /**
     * Initialises a new Dook with specified seed and data folder name.
     * Starts that new Dook program.
     *
     * @param args Seed for random generator (optional); Data folder name (optional).
     */
    public static void main(String[] args) {
        Random random = new Random();
        if (args.length > 0 && args[0].matches("-?\\d+")) {
            long seed = Long.parseLong(args[0]);
            random.setSeed(seed);
        }

        String dataDirName = args.length > 1 ? args[1] : "data";
        FileManager io = new FileManager(dataDirName);

        Dook dook = new Dook(random, io);
        dook.run();
    }

    /**
     * Starts the program.
     * Loops between user inputs and giving responses.
     */
    private void run() {
        String userQuery = " ";
        Response response;

        while (true) {
            try {
                response = commandManager.processQuery(userQuery);
                ui.printResponse(response);

            } catch (DookException e) {
                response = new Response(e.getMessage());
                ui.printResponse(response);

            } catch (Exception e) {
                ui.printError(e);
            }

            userQuery = ui.getUserQuery();
        }
    }
}

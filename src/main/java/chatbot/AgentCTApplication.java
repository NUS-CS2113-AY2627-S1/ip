package chatbot;

import java.util.Scanner;

/** Owns AgentCT's startup services and runs the command loop. */
public class AgentCTApplication {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private final CommandLoop commandLoop;

    /** Creates an application backed by the given task file.
     *
     * @param filePath path to the task save file
     */
    public AgentCTApplication(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
        commandLoop = new CommandLoop(tasks, ui, storage, new Parser());
    }

    /** Starts the chatbot and processes commands from standard input. */
    public void run() {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        commandLoop.run(scanner);
        scanner.close();
    }
}

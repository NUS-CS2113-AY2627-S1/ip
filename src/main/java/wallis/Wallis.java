package wallis;

/**
 * The Wallis chatbot application.
 * Handles user inputs for managing different types of tasks.
 */
public class Wallis {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Wallis(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (WallisException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        
        while (!isExit) {
            String userInput = ui.readCommand();
            
            if (userInput.equals("bye")) {
                isExit = true;
                continue;
            }
            
            try {
                Parser.parseAndExecute(userInput, tasks, ui, storage);
            } catch (WallisException e) {
                ui.showLine();
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
        
        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Wallis("./data/wallis.txt").run();
    }
}
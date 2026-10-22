import command.Command;
import parser.Parser;
import parser.ParserException;
import ui.Ui;

/**
 * Provides the command-line entry point for the ATHENA task manager.
 */
public class ATHENA {
    /**
     * Starts the command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        try (Ui ui = new Ui()) {
            TaskHandler taskHandler = new TaskHandler();
            taskHandler.loadTasks();
            ui.showGreeting();

            boolean isExit = false;
            while (!isExit && ui.hasNextCommand()) {
                String line = ui.readCommand();
                ui.showHorizontalLine();
                try {
                    Command command = Parser.parse(line);
                    command.execute(taskHandler);
                    isExit = command.isExit();
                    if (isExit) {
                        ui.showFarewell();
                    }
                } catch (AthenaException | ParserException exception) {
                    ui.showError(exception.getMessage());
                } finally {
                    ui.showHorizontalLine();
                }
            }
        } catch (AthenaException exception) {
            System.out.println("Unable to start ATHENA. " + exception.getMessage());
        }
    }
}

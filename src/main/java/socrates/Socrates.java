package socrates;

import socrates.storage.FileHandler;

/**
 * Entry point: a command-line task chatbot that loads tasks from disk on
 * startup, processes commands in a loop, and saves after each change.
 */
public class Socrates {
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList(FileHandler.loadFile());
        CommandHandler commandHandler = new CommandHandler(taskList, ui);

        ui.printBanner();
        String byeMessage = "\t Farewell, friend. Remember - the unexamined task list is not worth keeping!";

        while (true) {
            try {
                String[] formattedInput = CommandHandler.formatInput(ui.readCommand());
                if (formattedInput[0].equals("bye")) {
                    break;
                }
                commandHandler.handleInput(formattedInput);
                FileHandler.saveFile(taskList.asArrayList());
            } catch (SocratesException e) {
                ui.printMessage(e.getMessage());
            } catch (Exception e) {
                ui.printMessage("Error: " + e.getMessage());
            }
        }
        ui.printMessage(byeMessage);
    }
}

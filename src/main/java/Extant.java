/**
 * Entry point for the Extant chatbot application.
 * Handles user commands for adding, listing, and marking tasks.
 */
public class Extant {
    private static final int MAX_TASKS = 100;


    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();
    private static final Storage storage = new Storage("data/Extant.txt");
    private static TaskList tasks = new TaskList(storage);
    private static final String banner =
        """
███████╗██╗  ██╗████████╗ █████╗ ███╗   ██╗████████╗
██╔════╝╚██╗██╔╝╚══██╔══╝██╔══██╗████╗  ██║╚══██╔══╝
█████╗   ╚███╔╝    ██║   ███████║██╔██╗ ██║   ██║   
██╔══╝   ██╔██╗    ██║   ██╔══██║██║╚██╗██║   ██║   
███████╗██╔╝ ██╗   ██║   ██║  ██║██║ ╚████║   ██║   
╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝   """;



    /**
     * Runs the main command loop for the Extant chatbot.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        storage.load(tasks, MAX_TASKS);

        System.out.println(banner);
        ui.showWelcome();
        run();
    }


    /** Reads and executes commands until the user enters {@code bye}. */
    private static void run() {
        outerLoop:
        while (true) {
            try
            {
            String[] input = parser.split(ui.readCommand());
            CommandType keyword = parser.getKeyword(input);
            switch (keyword)
                {   
                case CommandType.BYE:
                    ui.showMessage("Farewell. Until our paths cross again.");
                    break outerLoop; // idk what this does but it works?

                case CommandType.DELETE:
                    int indexDelete = parser.parseAsIndex(keyword, input);
                    Task deletedTask = tasks.remove(indexDelete);
                    ui.showMessage("Deleting " + (indexDelete + 1) + ": \n  " + deletedTask);
                    break;

                case CommandType.LIST:
                    ui.showTaskList(tasks);
                    break;

                case CommandType.FIND:
                    if (input.length < 2 || input[1].isBlank()) {
                        throw new IllegalEventException("Usage: find <string>");
                    }
                    ui.showMatchingTasks(tasks.find(input[1]));
                    break;

                case CommandType.MARK:
                    int indexMark = parser.parseAsIndex(keyword, input);
                    tasks.mark(indexMark);
                    ui.showMessage("Nice! I've marked this task as done:\n  " + tasks.get(indexMark));
                    break;

                case CommandType.UNMARK:
                    int indexUnmark = parser.parseAsIndex(keyword, input);
                    tasks.unmark(indexUnmark);
                    ui.showMessage("OK, I've marked this task as not done yet:\n  " + tasks.get(indexUnmark));
                    break;

                case CommandType.TODO:
                case CommandType.DEADLINE:
                case CommandType.EVENT:
                    tasks.add(parser.parseAsTask(keyword, input));
                    ui.showMessage("added: " + tasks.get(tasks.size() - 1));
                    break;

                default:
                    throw new IllegalKeywordException();
                }                
            } catch (IllegalEventException e) {
                ui.showMessage(e.getMessage());
            } catch (IllegalKeywordException e) {
                ui.showMessage("Command Unrecognized. Please use 'todo', 'deadline', or 'event' to add tasks.");
            } catch (NumberFormatException e) {
                ui.showMessage("Invalid index format. Please provide a valid number.");
            } catch (IndexOutOfBoundsException e) {
                ui.showMessage("Invalid index. Please provide a valid task number.");
            } catch (Exception e) {
                ui.showMessage("An unexpected error occurred: " + e.getMessage());
            }
        }
        ui.close();
    }

}

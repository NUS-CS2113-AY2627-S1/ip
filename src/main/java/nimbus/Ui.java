package nimbus;

/**
 * Displays messages to the Nimbus user.
 */
public class Ui {

    /**
     * Displays the Nimbus mascot and welcome message.
     */
    public void showWelcome() {
        String mascot = """
                +++++++++++++++===============-------:--++=:..    ..=+##=.-=-. ..:-==-::
                ++++++++++++++++++==========--------::-=++=:..    .:+%@=-....:-+*%%%#=:-
                +++++++++++++++++++++=======:..:-==-::--++=:..    :-#@@%*:-=**%%%%%@@@+:
                ++++++++++++++++++++++======-:..:-=++++====--=+=====**+====-:-%%%%%@@@@+
                ++++++++++++++++++++++======-:   ..:-=---======+=====-:::....-#%%%%@@@@@
                +++++++++++++++++++++======---.....:::--::----:----=---:....=%@%**%%%@@@
                ++++++++++++++++++++++====----+*=---:::::::::::--=---:::::-#@@@@=+%%%%@@
                ++++++++++++++++++++++====---==--=#%##*++=-::::--=---====*%@@@@%+**#%%%%
                *****++++++++++++++++++===---+=+*-.*@@@@@@@%+-==+*#%%@@@@@@@@@#++++*####
                *********++++++++++++++====--:+@@#-*@@@@@@@@@*%@@@@@@@@@@@@@@@+++**####=
                ************+++***++++++===-:::%@@@@@@@@@@@@#:.*@@@@@@@@@@@@@@@%%%%%%#=-
                ********************++++==+-.-:+@@@@@@@@@@#+:.:+@@@@@@@@@@%#@@@@@@@@@%**
                *********************++++**-.:-:-*%@@@@@#=-..:-=#@@@@@@@@%#%%%#%%%@%#%@*
                *********************+++*#+:....   .:::.  ...::=-==****+=*@@@%###%%#%%%*
                ******####**************#*+-:::.:-         .----...     =%%@@%%%%%%%@***
                *****######****************=-::::+*-.       ::       .:.*%@%%%###%@%#*++
                %##**#############******++#+--::::+%#=..           =#+.:#%@@@%%%%%%+++==
                @@@%##################**+++*+=-:::.:+%%=:...  ..:==*-. -%%%@@%%@@@+=+==-
                #%@@@%#####%%%%########**+***+=-::::.:*@%%%%%%%*-:.  .:+%%@@@%@@@%=====.
                %%%%@@@%*#%%%%%%%######*+++***+=-::::::*@%@@@@+.     .:#@@@@@@@@***==-==
                @@@@*%@@%+#%%@@%%%##%%#+==+****+=-::::::%@@@%- .    ..*@@@@@@@@%*==+=+++
                @@@@%=*#*+*##%%@@%###%#+--=+****+==--:::#@@@=.......:*@@@@@@@@*+++=--=++
                @@@@@@*+++===+#@@%%%##*=-::=*****++==-=%@@@@*:...::-*%@@@@@@@+====-==++*
                @@@@@@@@@@#+%@@@@@@@%%*+--::+*****++=*@@@@@@@=:::::+@@@@@@@@*=---. -=+*#
                @@%%%@@@@@@@@@@@@@@@@@%*+=:.:=**+++=+@@@@@@@@*==--+%@@@@@@@*=---:.-==+#%
                @@@@@@@@@@@@@@@@@@@@@@@##+-:.:=+++=-#@@@@@@@@%====#@@@@@@@#+=--:::-=+*%@
                @@@@@@@@@@@#:*@@@@@@@@@%##*=:.:-+=-=@@@@@@@@@%+=+*@@@@@@@%+==-:::-==+*#%
                @@@@@@@@@@@+:#@@*%%*%@@%%%%*-:--==-*@@@@@@@@@%++*%@@@@@@@*==--::-=+##*++
                @@@@@@@@@@@+-#@@*#%-%@@@%@%#*=--=--%@@@@@@@@@#++#@@@@@@@#+=-----==#@@@@@
                @@@@@@@@@@@=-#@@***+%@@@%%%%*=--==-@@@@@@@@@@*++%@@@@@@%*=----==+#@@@@@@
                @@@@@@@@@@@=-%@@##%@@@@%%%%#*=--==+@@@@@@@@@@**%@@@@@@@#+==--==+%@@@@@@@
                %@@@@@@@@@@==%@@@@@@@@@@%%%%#+==++%@@@@@@@@@%*@@@@@@@@%#+==-==*%@@@@@@@@
                %#@@@@@@@@@==%@@@@@@@@@@@%%%%#*###@@@@@@@@@@#=%@@@@@@@%*+====*@@@@@@@@@@
                *=+%@@@@@@@==%@%@@@@@@@@%%#%%@@@@@@@@@@@@@@@#+%@@@@@@@#*++++#@@@@@@@@@@@
                *=+@@@@@@@@+=%%#@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%@@@@@@@%#*++*%@@@@@@@@@@@@
                @%@@@@@@@@@%#@%#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%#***%@@@@@@@@@@@@@
                """;
        System.out.println();
        System.out.println(mascot);
        System.out.println("Mr. Bombastic, call me fantastic...");
        System.out.println();
        System.out.println("Oh. You're here.");
        System.out.println("Good. This entrance deserved an audience.");
        System.out.println();
        System.out.println("N I M B U S");
        System.out.println("Sunglasses on. Briefcase loaded. Let's do business.");
        System.out.println();
        System.out.println("Try: todo make an entrance");
        System.out.println("------------------------------------------------------------------------");
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("[mic drop]");
        System.out.println();
        System.out.println("Instructions handled. Exit executed. Nimbus rocked!");
        System.out.println("Too much work done today. Need my beauty sleep. BYEEEEEE!!!");

    }

    /**
     * Displays a response to the user.
     *
     * @param message Response to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the numbered task list.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.size() == 0) {
            showMessage("No tasks. An unusually quiet day at headquarters.");
            return;
        }

        showMessage("Here's the agenda:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays tasks whose descriptions contain the keyword, ignoring case.
     *
     * @param tasks Current task list.
     * @param keyword Text to search for.
     */
    public void showMatchingTasks(TaskList tasks, String keyword) {
        String searchText = keyword.toLowerCase(java.util.Locale.ROOT);
        int matchCount = 0;

        showMessage("Running the investigation...");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String description = task.getDescription().toLowerCase(java.util.Locale.ROOT);

            if (description.contains(searchText)) {
                showMessage((i + 1) + ". " + task);
                matchCount++;
            }
        }

        if (matchCount == 0) {
            showMessage("No matches. Even my sources have nothing on that.");
        } else {
            showMessage("Case closed. Matches found: " + matchCount);
        }
    }
}
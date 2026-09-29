package kevie;

/**
 * Handling the aesthetics of printing messages.
 */
public class UserInterface {
    private final String BANNER = """
                ====================================
                ██╗  ██╗███████╗██╗   ██╗██╗███████╗
                ██║ ██╔╝██╔════╝██║   ██║██║██╔════╝
                █████╔╝ █████╗  ██║   ██║██║█████╗
                ██╔═██╗ ██╔══╝  ╚██╗ ██╔╝██║██╔══╝
                ██║  ██╗███████╗ ╚████╔╝ ██║███████╗
                ╚═╝  ╚═╝╚══════╝  ╚═══╝  ╚═╝╚══════╝
                ====================================""";

    private final String PREFIX_BOT = "[Kevie]";
    private final String PREFIX_USER = "[You]";
    private final String PREFIX_INDENT = "        ";

    /**
     * Prints the welcoming ASCII banner of the Kevie bot.
     */
    public void printBanner(){
        System.out.println(BANNER);
    }

    /**
     * Prints a message from the Kevie bot.
     *
     * @param msg The message to be printed
     * @param indentOnly if true, prints message with <code>[Kevie]</code> prefix,
     *                   if false, prints message with an indent in front only.
     */
    public void botSpeak(String msg, boolean indentOnly) {
        if (!indentOnly) System.out.println(PREFIX_BOT + " " + msg);
        else System.out.println(PREFIX_INDENT + msg);
    }

    /**
     * Prints a message from the Kevie bot with <code>[Kevie]</code> prefix.
     *
     * @param msg The message to be printed
     */
    public void botSpeak(String msg) {
        botSpeak(msg, false);
    }

    /**
     * Prints exactly <code>[User] </code>
     * To be used before asking for user input.
     */
    public void userPrompt(){
        System.out.print(PREFIX_USER + " ");
    }

}

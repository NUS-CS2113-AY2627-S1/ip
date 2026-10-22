package nga;

/**
 * Starts the NGA (No Goobers Allowed) command-line application.
 */
public class Nga {
    private final NgaUi ui;

    /**
     * Creates an NGA application with its command handler and user interface.
     */
    public Nga() {
        ui = new NgaUi(new NgaCommandHandler());
    }

    /**
     * Starts the application and delegates interaction to the UI layer.
     */
    public void run() {
        ui.run();
    }

    /**
     * Starts Nga from the command line.
     *
     * @param args command-line arguments, which are currently ignored
     */
    public static void main(String[] args) {
        new Nga().run();
    }
}

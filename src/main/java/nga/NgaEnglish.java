package nga;

/** Starts Nga using the same separated UI and command-handling layers. */
public class NgaEnglish {
    /** Creates the English-language entry point. */
    public NgaEnglish() {
    }

    /**
     * Starts the English-language entry point.
     *
     * @param args command-line arguments, which are currently ignored
     */
    public static void main(String[] args) {
        new Nga().run();
    }
}
